package com.gljr.jifen.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.*;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.common.dtchain.vo.GouliUserInfo;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.exception.ApiServerException;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.*;
import com.gljr.jifen.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.gljr.jifen.constants.DBConstants.CACHE_USER_PAYMENT_CODE_KEY;


@CrossOrigin(origins = "*", maxAge = 3600)

@Controller
@RequestMapping(value = "/v1/app")
public class APPStoreOfflineOrderController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(APPStoreOfflineOrderController.class);

    @Autowired
    private StoreOfflineOrderService storeOfflineOrderService;

    @Autowired
    private StoreInfoService storeInfoService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DTChainService chainService;

    @Autowired
    private AdminService adminService;

    @Resource
    private StoreCouponService storeCouponService;

    private final String ORDER_DATE_FORMAT = "yyyy/MM/dd HH:mm";

    /**
     * 商户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回状态码
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public JsonResult login(@RequestParam String username, @RequestParam String password) {
        JsonResult jsonResult = new JsonResult();

        //用户名不能为空
        if (org.apache.commons.lang3.StringUtils.isBlank(username)) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_USERNAME_IS_BLANK);
        }

        //用户密码不能为空
        if (org.apache.commons.lang3.StringUtils.isBlank(password)) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_PASSWORD_IS_BLANK);
        }
        String device = request.getHeader("device");
        if (org.springframework.util.StringUtils.isEmpty(device)) {
            CommonResult.failed(jsonResult);
            return jsonResult;
        }

        int _device;
        if (ClientType.checkClientType(device) == 1) {
            _device = DBConstants.ClientType.APP.getCode();
        } else {
            _device = DBConstants.ClientType.WEB.getCode();
        }

        return adminService.doLogin(username, password, _device);
    }

    /**
     * 商户退出登录
     *
     * @return 返回状态码
     */
    @GetMapping(value = "/logout")
    @ResponseBody
    public JsonResult logout() {
        String token = request.getHeader("token");
        String uid = request.getHeader("uid");
        return adminService.doLogout(uid, DBConstants.AdminAccountType.STORE_ADMIN);
    }

    /**
     * 获取商户信息
     *
     * @return 返回状态码
     */
    @GetMapping(value = "/store/info")
    @ResponseBody
    public JsonResult getStoreInfo() {
        String uid = request.getHeader("uid");
        JsonResult jsonResult = new JsonResult();

        List<StoreInfo> storeInfos = storeInfoService.selectStoreInfoByAid(NumberUtils.getInt(uid));

        if (ValidCheck.validList(storeInfos)) {
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        StoreInfo storeInfo = storeInfos.get(0);

        if (storeInfo == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_NOT_EXIST);
        } else {
            Map<Object, Object> storeInfoMap = new HashMap<>(11);

            storeInfoMap.put("uid", uid);//管理员id
            storeInfoMap.put("address", storeInfo.getAddress());//商铺地址
            storeInfoMap.put("logoUrl", storeInfo.getLogoKey());//logo图片地址
            storeInfoMap.put("serialCode", storeInfo.getSerialCode());//商家号
            storeInfoMap.put("name", storeInfo.getName());//商铺名称
            storeInfoMap.put("integralToRmbRatio", GlobalConstants.INTEGRAL_RMB_EXCHANGE_RATIO);//积分兑换人民币比例

            String principalIdNo = "";//证件号
            String principalPhone = "";//联系电话
            String principalName = "";//联系人
            StoreExtInfo storeExtInfo = storeInfoService.selectStoreExtInfoBySiId(storeInfo.getId());
            if (storeExtInfo != null) {
                principalIdNo = storeExtInfo.getPrincipalIdNo();
                principalPhone = storeExtInfo.getPrincipalPhone();
                principalName = storeExtInfo.getPrincipalName();
            }

            String blockNo = "";//钱包地址
            UserCredits userCredits = this.userCreditsService.getStoreCreditsByManagerId(storeInfo.getAid());
            if (null != userCredits && !StringUtils.isEmpty(userCredits.getWalletAddress())) {
                blockNo = userCredits.getWalletAddress();
            }
            storeInfoMap.put("principalIdNo", principalIdNo);
            storeInfoMap.put("principalPhone", principalPhone);
            storeInfoMap.put("principalName", principalName);
            storeInfoMap.put("blockNo", blockNo);

            //商家图片数量和地址
            List<StorePhoto> storePhotoList = storeInfoService.selectStorePhotoById(storeInfo.getId());
            List<String> photoList = new ArrayList<>(16);
            for (StorePhoto storePhoto : storePhotoList) {
                photoList.add(storePhoto.getImgKey());
            }
            storeInfoMap.put("photoNum", storePhotoList.size());
            storeInfoMap.put("photoList", photoList);

            jsonResult.setItem(storeInfoMap);
            jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_OPERATION_SECCESS);
        }
        return jsonResult;
    }


    /**
     * 获取商户交易记录
     *
     * @return 返回状态码
     */
    @PostMapping(value = "/store/orders")
    @ResponseBody
    public JsonResult orders(OfflineOrderReqParam offlineOrderReqParam) {
        String uid = request.getHeader("uid");
        JsonResult jsonResult = new JsonResult();

        Integer pageNum = offlineOrderReqParam.getPageNum();
        Integer pageSize = offlineOrderReqParam.getPageSize();
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }

        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }

        List<StoreInfo> storeInfoList = storeInfoService.selectStoreInfoByAid(NumberUtils.getInt(uid));
        if (ValidCheck.validList(storeInfoList)) {
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        StoreInfo storeInfo = storeInfoList.get(0);

        PageHelper.startPage(pageNum, pageSize);
        List<StoreOfflineOrder> storeOfflineOrderList = storeOfflineOrderService.selectAllOfflineOrderByExample(offlineOrderReqParam, storeInfo.getId());
        PageInfo pageInfo = new PageInfo(storeOfflineOrderList);
        Map<Object, Object> resultMap = new HashMap<>(7);
        HashMap totalInfo = storeOfflineOrderService.getStoreTotalInfo(NumberUtils.getInt(uid));
        int totalIntegral = 0;
        int totalMoney = 0;
        if (totalInfo != null) {
            totalIntegral = NumberUtils.getInt(String.valueOf(totalInfo.get("totalIntegral")));//获得总积分
            totalMoney = NumberUtils.getInt(String.valueOf(totalInfo.get("totalMoney")));//获得总现金
        }
        resultMap.put("totalIntegral", totalIntegral);//获得总积分
        resultMap.put("totalMoney", totalMoney);//获得总现金
        resultMap.put("data", processOffilineOrderDTO(storeOfflineOrderList));
        //页码总数
        resultMap.put("pages", pageInfo.getPages());
        //数据总条数
        resultMap.put("total", pageInfo.getTotal());
        //当前页码
        resultMap.put("pageNum", pageInfo.getPageNum());
        jsonResult.setItem(resultMap);
        jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_OPERATION_SECCESS);

        return jsonResult;
    }

    /**
     * 交易记录详情
     *
     * @return 返回状态码
     */
    @GetMapping(value = "/store/orderDetail")
    @ResponseBody
    public JsonResult orderDetail(@RequestParam String orderId) {
        JsonResult jsonResult = new JsonResult();

        if (StringUtils.isBlank(orderId)) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.REQUEST_PARAMETER_ERROR);
            return jsonResult;
        }

        List<StoreOfflineOrder> storeOfflineOrderList = storeOfflineOrderService.getStoreTotalInfo(orderId);
        if (storeOfflineOrderList.isEmpty()) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.ORDER_INFO_NOT_EXIST);
            return jsonResult;
        }

        StoreOfflineOrder storeOfflineOrder = storeOfflineOrderList.get(0);

        //NEED 交易单号、交易时间、购买用户、支付类型、支付积分、支付现金
        GatewayResponse<GouliUserInfo> response = this.chainService.getUserInfo(String.valueOf(storeOfflineOrder.getUid()));
        if (null == response || null == response.getContent()) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.GET_USER_INFO_FAIL);
            return jsonResult;
        }

        Map<Object, Object> resultMap = new HashMap<>();
        resultMap.put("orderId", storeOfflineOrder.getTrxCode());
        resultMap.put("transationTime", DateUtils.formatToString(storeOfflineOrder.getCreateTime(), ORDER_DATE_FORMAT));
        resultMap.put("buyUser", response.getContent().getUserName());
        String oderType = "积分买单";
        if (storeOfflineOrder.getExtCash() > 0) {
            oderType = "积分+现金买单";
        }
        resultMap.put("oderType", oderType);
        resultMap.put("orderStat", storeOfflineOrder.getStatus());
        resultMap.put("integral", storeOfflineOrder.getIntegral());
        resultMap.put("cash", storeOfflineOrder.getExtCash());

        jsonResult.setItem(resultMap);
        jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_OPERATION_SECCESS);
        return jsonResult;
    }

    /**
     * 商户发起退款 Refund
     *
     * @return 返回状态码
     */
    @GetMapping(value = "/store/refund")
    @ResponseBody
    public JsonResult refund(@RequestParam String orderId) {
        String uid = request.getHeader("uid");
        JsonResult jsonResult = new JsonResult();

        if (StringUtils.isBlank(orderId)) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.REQUEST_PARAMETER_ERROR);
            return jsonResult;
        }

        List<StoreOfflineOrder> storeOfflineOrderList = storeOfflineOrderService
                .getStoreTotalInfo(NumberUtils.getInt(uid), orderId);
        if (storeOfflineOrderList.isEmpty()) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.ORDER_INFO_NOT_EXIST);
            return jsonResult;
        }

        StoreOfflineOrder storeOfflineOrder = storeOfflineOrderList.get(0);

        //只有付款成功订单可退款
        if (storeOfflineOrder.getStatus() != DBConstants.OrderStatus.PAID.getCode()) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.ORDER_CAN_NOT_REFUND);
            return jsonResult;
        }

        //超过24小时不能执行退款操作
        if (DateUtils.getRallDate(storeOfflineOrder.getCreateTime(), 1).before(new Date())) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.MORE_THAN_REFUND_TIME);
            return jsonResult;
        }

        //代金券不可退款
        if (storeOfflineOrder.getUcId() != null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.COUPON_CAN_NOT_REFUND);
            return jsonResult;
        }

        try {
            //退还积分操作
            storeOfflineOrderService.refund(storeOfflineOrder);
        } catch (ApiServerException e) {
            System.out.println(e);
            jsonResult.setErrorCodeAndMessage(GlobalConstants.SYSTEM_EXCEPTION);
            return jsonResult;
        }

        jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_OPERATION_SECCESS);
        return jsonResult;
    }

    /**
     * 校验用户积分是否足够，把结果存到缓存中供用户app查询，向商户app返回积分校验结果
     *
     * @param userId   校验用户积分的用户id
     * @param integral 本次消费积分
     * @return
     */
    @GetMapping(value = "/store/checkUserCredit")
    @ResponseBody
    public JsonResult checkUserCredit(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "integral") Integer integral,
                                      @RequestParam(value = "paymentCode") String paymentCode) {
        String uid = request.getHeader("uid");
        JsonResult jsonResult = new JsonResult();

        List<StoreInfo> storeInfos = storeInfoService.selectStoreInfoByAid(NumberUtils.getInt(uid));
        if (ValidCheck.validList(storeInfos)) {
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        if (!paymentCode.equals(redisService.get(CACHE_USER_PAYMENT_CODE_KEY + userId))) {
            jsonResult.setMessage("用户数据获取失败！");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }

        UserCredits userCredits = this.userCreditsService.getUserCredits(userId);
        if (null == userCredits) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.CAN_NOT_GET_USER_CREDIT);
            return jsonResult;
        }

        //判断用户积分状况
        Map<Object, Object> checkResult = new HashMap<>();
        double cash = 0D;
        int pwCheck = GlobalConstants.PwCheck.YES.getCode();
        int actuallyPayIntegral = integral;
        int canTrad = 1;
        if (userCredits.getIntegral() < integral) {
            cash = (integral - userCredits.getIntegral()) / GlobalConstants.INTEGRAL_RMB_EXCHANGE_RATIO;
            actuallyPayIntegral = userCredits.getIntegral();
            canTrad = GlobalConstants.CanTrad.NO.getCode();
        }
        if (userCredits.getFreePaymentLimit() != null && userCredits.getFreePaymentLimit() < integral) {
            pwCheck = GlobalConstants.PwCheck.NO.getCode();
            canTrad = GlobalConstants.CanTrad.NO.getCode();
        }
        checkResult.put("integral", integral);//消费积分
        checkResult.put("actuallyPayIntegral", actuallyPayIntegral);//实际支付的积分
        checkResult.put("cash", cash);//需补充的现金
        checkResult.put("status", GlobalConstants.OrderStatus.UNPAID.getCode());//交易状态 0：待付款 1：已付款 2：取消
        checkResult.put("pwCheck", pwCheck);//是否校验过密码 0：没有 1：已校验（不超过免密额度默认为1）
        checkResult.put("canTrad", canTrad);//商家端是否可扣款 0：不可 1：可以
        checkResult.put("storeName", storeInfos.get(0).getName());

        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId + ":" + integral;
        String cacheKey1 = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId;
        this.redisService.put(cacheKey, JsonUtil.toJson(checkResult), GlobalConstants.CACHE_DATA_FAILURE_TIME, TimeUnit.SECONDS);
        this.redisService.put(cacheKey1, integral + "", 60, TimeUnit.SECONDS);

        jsonResult.setItem(checkResult);
        jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_OPERATION_SECCESS);
        return jsonResult;
    }

    /**
     * 获取用户积分
     *
     * @return
     */
    @GetMapping(value = "/store/getIntegral")
    @ResponseBody
    public JsonResult getIntetral() {
        JsonResult jsonResult = new JsonResult();

        String uid = request.getHeader("uid");
        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + uid;

        String integral = this.redisService.get(cacheKey);

        if (integral == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.NO_RESULT);
            return jsonResult;
        }

        jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        jsonResult.setMessage(integral);
        //获取到积分后清除redis
        redisService.evict(cacheKey);
        return jsonResult;
    }


    /**
     * 获取用户积分校验结果
     *
     * @param userId   校验用户积分的用户id
     * @param integral 本次消费积分
     * @return
     */
    @GetMapping(value = "/store/integralTradResult")
    @ResponseBody
    public JsonResult integralTradResult(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "integral") Integer integral) {
        JsonResult jsonResult = new JsonResult();

        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId + ":" + integral;
        String cacheResult = this.redisService.get(cacheKey);
        if (cacheResult == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.NO_RESULT);
            return jsonResult;
        }
        Map map = new HashMap();
        map.put("data", JSON.parseObject(this.redisService.get(cacheKey)));

        jsonResult.setItem(map);
        jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_OPERATION_SECCESS);
        return jsonResult;
    }

    /**
     * 用户确认或者取消现金支付
     *
     * @param userId   校验用户积分的用户id
     * @param integral 本次消费积分
     * @return
     */
    @GetMapping(value = "/store/cashPay")
    @ResponseBody
    public JsonResult cashPay(@RequestParam(value = "userId") Integer userId,
                              @RequestParam(value = "integral") Integer integral,
                              @RequestParam(value = "cashPay") int cashPay) {
        JsonResult jsonResult = new JsonResult();

        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId + ":" + integral;
        String cacheResult = this.redisService.get(cacheKey);
        if (cacheResult == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.NO_RESULT);
            return jsonResult;
        }
        JSONObject cacheJsonObj = JSON.parseObject(cacheResult);

        //确认现金支付
        if (cashPay == GlobalConstants.CashPay.YES.getCode() && NumberUtils.getInt(cacheJsonObj.get("pwCheck") + "") == GlobalConstants.PwCheck.YES.getCode()) {
            cacheJsonObj.put("canTrad", GlobalConstants.CanTrad.YES.getCode());
        } else {
            //订单直接取消
            cacheJsonObj.put("status", GlobalConstants.OrderStatus.CANCELED.getCode());
            cacheJsonObj.put("canTrad", GlobalConstants.CanTrad.NO.getCode());
        }

        this.redisService.put(cacheKey, cacheJsonObj.toString(), GlobalConstants.CACHE_DATA_FAILURE_TIME, TimeUnit.SECONDS);
        jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_OPERATION_SECCESS);
        return jsonResult;
    }

    /**
     * 积分交易密码校验
     *
     * @param userId   校验用户积分的用户id
     * @param integral 本次消费积分
     * @return
     */
    @GetMapping(value = "/store/pwCheck")
    @ResponseBody
    public JsonResult pwCheck(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "password") String password, @RequestParam(value = "integral") Integer integral,
                              @RequestParam(value = "code") String code) {
        JsonResult jsonResult = new JsonResult();

        if(!org.springframework.util.StringUtils.isEmpty(code)){
            if(!code.equals(redisService.get("USER_CODE"+userId))){
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage("验证码错误！");
                return jsonResult;
            }

            redisService.evict("USER_CODE"+ userId);
        }


        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId + ":" + integral;
        String cacheResult = this.redisService.get(cacheKey);
        if (cacheResult == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.NO_RESULT);
            return jsonResult;
        }

        GatewayResponse response = this.chainService.checkPassword(userId + 0L, password);
        if (null == response || response.getCode() != 200) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_PASSWORD_ERROR);
            return jsonResult;
        }

        JSONObject cacheJsonObj = JSON.parseObject(cacheResult);
        //修改缓存订单状态
        cacheJsonObj.put("pwCheck", GlobalConstants.PwCheck.YES.getCode());
        cacheJsonObj.put("canTrad", GlobalConstants.CanTrad.YES.getCode());

        this.redisService.put(cacheKey, cacheJsonObj.toString(), GlobalConstants.CACHE_DATA_FAILURE_TIME, TimeUnit.SECONDS);

        jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_OPERATION_SECCESS);
        return jsonResult;

    }

    /**
     * 积分交易
     *
     * @param userId   校验用户积分的用户id
     * @param integral 本次消费积分
     * @return
     */
    @GetMapping(value = "/store/integralTrad")
    @ResponseBody
    public JsonResult integralTrad(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "integral") Integer integral) {
        String uid = request.getHeader("uid");
        integral = Math.abs(integral);
        JsonResult jsonResult = new JsonResult();
        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId + ":" + integral;
        String cacheResult = this.redisService.get(cacheKey);
        if (cacheResult == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.NO_RESULT);
            return jsonResult;
        }

        UserCredits userCredits = this.userCreditsService.getUserCredits(userId);
        if (null == userCredits) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.CAN_NOT_GET_USER_CREDIT);
            return jsonResult;
        }

        JSONObject cacheJsonObj = JSON.parseObject(cacheResult);
        int orderStatus = NumberUtils.getInt(String.valueOf(cacheJsonObj.get("status")));
        if (orderStatus != GlobalConstants.OrderStatus.UNPAID.getCode()) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.ORDER_STATUS_EXCEPTION);
            return jsonResult;
        }

        int canTrad = NumberUtils.getInt(String.valueOf(cacheJsonObj.get("canTrad")));
        if (canTrad != GlobalConstants.CanTrad.YES.getCode()) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.ORDER_CAN_NOT_TRAD);
            return jsonResult;
        }

        try {
            jsonResult = storeOfflineOrderService.integralTrad(userCredits, userId, NumberUtils.getInt(uid), integral, jsonResult);
        } catch (ApiServerException e) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.SYSTEM_EXCEPTION);
            e.printStackTrace();
            return jsonResult;
        }

        //修改缓存订单状态
        cacheJsonObj.put("status", GlobalConstants.OrderStatus.PAID.getCode());
        this.redisService.put(cacheKey, cacheJsonObj.toString(), GlobalConstants.CACHE_DATA_FAILURE_TIME, TimeUnit.SECONDS);

        jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_OPERATION_SECCESS);
        return jsonResult;
    }

    /**
     * 取消订单
     *
     * @param userId   用户id
     * @param integral 本次消费积分
     * @return
     */
    @GetMapping(value = "/store/orderCancel")
    @ResponseBody
    public JsonResult orderCancel(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "integral") Integer integral) {
        JsonResult jsonResult = new JsonResult();

        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId + ":" + integral;
        String cacheResult = this.redisService.get(cacheKey);
        if (cacheResult == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.NO_RESULT);
            return jsonResult;
        }

        //修改缓存订单状态
        JSONObject cacheJsonObj = JSON.parseObject(cacheResult);
        cacheJsonObj.put("status", GlobalConstants.OrderStatus.CANCELED.getCode());
        this.redisService.put(cacheKey, cacheJsonObj.toString(), GlobalConstants.CACHE_DATA_FAILURE_TIME, TimeUnit.SECONDS);

        jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_OPERATION_SECCESS);
        return jsonResult;
    }

    /**
     * 代金券消费
     *
     * @param couponCode 代金券消费
     * @return
     */
    @RequestMapping(value = "/store/consumeCoupon")
    @ResponseBody
    public JsonResult consumeCoupon(@RequestParam(value = "couponCode") String couponCode) {
        String uid = request.getHeader("uid");
        List<StoreInfo> storeInfos = storeInfoService.selectStoreInfoByAid(NumberUtils.getInt(uid));
        JsonResult jsonResult = new JsonResult();

        if (ValidCheck.validList(storeInfos)) {
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }
        StoreInfo storeInfo = storeInfos.get(0);
        UserCoupon userCoupon = storeOfflineOrderService.findUserCouponByCode(couponCode, storeInfo.getId());

        if (userCoupon == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.COUPON_NOT_EXIST);
            return jsonResult;
        }

        if(userCoupon.getStatus() != DBConstants.CouponStatus.VALID.getCode()){
            jsonResult.setErrorCodeAndMessage(GlobalConstants.COUPON_USED_OR_LOSS);
            return jsonResult;
        }

        StoreCoupon storeCoupon = storeOfflineOrderService.findStoreCouponById(userCoupon.getScId());

        if (storeCoupon == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.COUPON_NOT_EXIST);
            return jsonResult;
        }

        Map<Object, Object> data = new HashMap<>(4);
        try {
            String trxCode = storeOfflineOrderService.verifyCoupon(NumberUtils.getInt(uid), storeInfos.get(0), userCoupon, storeCoupon);
            data.put("trxCode", trxCode);
            jsonResult.setItem(data);
        } catch (ApiServerException e) {
            e.printStackTrace();
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            data.put("errorMsg", e.getMessage());
            jsonResult.setItem(data);
            return jsonResult;
        }

        jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_OPERATION_SECCESS);
        return jsonResult;
    }

    /**
     * 代金券校验
     *
     * @param couponCode 代金券代码
     * @return
     */
    @RequestMapping(value = "/store/verifyCoupon")
    @ResponseBody
    public JsonResult verifyCoupon(@RequestParam(value = "couponCode") String couponCode) {
        String uid = request.getHeader("uid");
        List<StoreInfo> storeInfos = storeInfoService.selectStoreInfoByAid(NumberUtils.getInt(uid));
        JsonResult jsonResult = new JsonResult();

        if (ValidCheck.validList(storeInfos)) {
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }
        StoreInfo storeInfo = storeInfos.get(0);
        UserCoupon userCoupon = storeOfflineOrderService.findUserCouponByCode(couponCode, storeInfo.getId());

        if (userCoupon == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.COUPON_NOT_EXIST);
            return jsonResult;
        }

        if(userCoupon.getStatus() != DBConstants.CouponStatus.VALID.getCode()){
            jsonResult.setErrorCodeAndMessage(GlobalConstants.COUPON_USED_OR_LOSS);
            return jsonResult;
        }

        StoreCoupon storeCoupon = storeOfflineOrderService.findStoreCouponById(userCoupon.getScId());

        if (storeCoupon == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.COUPON_NOT_EXIST);
            return jsonResult;
        }

        Map<Object, Object> info = new HashMap<>(11);
        final String dateFormat = "yyyy/MM/dd";

        info.put("address", storeInfo.getAddress());//商铺地址
        info.put("logoUrl", storeInfo.getLogoKey());//logo图片地址
        info.put("name", storeInfo.getName());//商铺名称
        info.put("couponMoney", storeCoupon.getEqualMoney());//卡券等值现金
        info.put("couponCode", userCoupon.getCouponCode());//卡券代码
        info.put("couponIntegral", storeCoupon.getIntegral());//卡券积分

        String startDate;//有效期起始时间
        String endDate;//有效期结束时间
        if (storeCoupon.getValidityType() == DBConstants.CouponValidityType.DAYS_LATER.getCode()) {
            startDate = DateUtils.formatToString(userCoupon.getCreateTime(), dateFormat);
            endDate = DateUtils.formatToString(DateUtils.getRallDate(userCoupon.getCreateTime(), storeCoupon.getValidDays()), dateFormat);
        } else {
            startDate = DateUtils.formatToString(storeCoupon.getValidFrom(), dateFormat);
            endDate = DateUtils.formatToString(storeCoupon.getValidTo(), dateFormat);
        }
        info.put("couponDateRange", startDate + " 至 " + endDate);

        jsonResult.setItem(info);
        jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_OPERATION_SECCESS);
        return jsonResult;
    }

    private List<OffilineOrderDTO> processOffilineOrderDTO(List<StoreOfflineOrder> storeOfflineOrderList) {
        List<OffilineOrderDTO> offilineOrderDTOList = new ArrayList<>();
        for (StoreOfflineOrder soo : storeOfflineOrderList) {
            OffilineOrderDTO offilineOrderDTO = new OffilineOrderDTO();
            offilineOrderDTO.setCash(soo.getExtCash());
            offilineOrderDTO.setIntegral(soo.getIntegral());
            offilineOrderDTO.setOrderId(soo.getTrxCode());
            offilineOrderDTO.setOrderStat(soo.getStatus());
            offilineOrderDTO.setOrderDate(DateUtils.formatToString(soo.getCreateTime(), ORDER_DATE_FORMAT));
            String oderType = "积分买单";
            if (soo.getExtCash() > 0) {
                oderType = "积分+现金买单";
            }
            offilineOrderDTO.setOrderType(oderType);
            offilineOrderDTO.setRemark("");
            offilineOrderDTOList.add(offilineOrderDTO);
        }
        return offilineOrderDTOList;
    }
}
