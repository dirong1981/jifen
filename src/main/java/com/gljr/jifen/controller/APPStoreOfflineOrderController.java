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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.gljr.jifen.constants.DBConstants.CACHE_USER_PAYMENT_CODE_KEY;


@CrossOrigin(origins = "*", maxAge = 3600)

@Controller
@RequestMapping(value = "/v1/app")
public class APPStoreOfflineOrderController extends BaseController {


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
            UserCredits userCredits = this.userCreditsService.getUserCredits(NumberUtils.getInt(uid), DBConstants.OwnerType.MERCHANT);
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


        PageHelper.startPage(pageNum, pageSize);
        List<StoreOfflineOrder> storeOfflineOrderList = storeOfflineOrderService.selectAllOfflineOrderByExample(NumberUtils.getInt(uid), offlineOrderReqParam);
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
            oderType = "积分买+现金买单";
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

        List<StoreInfo> storeInfos = storeInfoService.selectStoreInfoByAid(Integer.parseInt(uid));
        if(ValidCheck.validList(storeInfos)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }


        if (!paymentCode.equals(redisService.get(CACHE_USER_PAYMENT_CODE_KEY + userId))) {
            jsonResult.setMessage("用户数据获取失败！");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }

        UserCredits userCredits = this.userCreditsService.getUserCredits(userId, DBConstants.OwnerType.CUSTOMER);
        if (null == userCredits) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.CAN_NOT_GET_USER_CREDIT);
            return jsonResult;
        }

        //判断用户积分状况

        Map<Object, Object> checkResult = new HashMap<>();
        double cash = 0L;
        int exceedLimit = 0;
        int pwCheck = 1;
        if (userCredits.getIntegral() < integral) {
            cash = (integral - userCredits.getIntegral()) / GlobalConstants.INTEGRAL_RMB_EXCHANGE_RATIO;
        }
        if (userCredits.getFreePaymentLimit() != null && userCredits.getFreePaymentLimit() < integral) {
            exceedLimit = 1;
            pwCheck = 0;
        }
        checkResult.put("integral", integral);//消费积分
        checkResult.put("cash", cash);//需补充的现金
        checkResult.put("exceedLimit", exceedLimit);//是否超过免密额度 0：未超过 1：超过
        checkResult.put("status", 0);//交易状态 0：待付款 1：已付款 2：取消
        checkResult.put("pwCheck", pwCheck);//是否校验过密码 0：没有 1：已校验（不超过免密额度默认为1）
        checkResult.put("storeName", storeInfos.get(0).getName());

        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId + ":" + ":" + integral;
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

        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId + ":" + ":" + integral;
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
     * 积分交易密码校验
     *
     * @param userId   校验用户积分的用户id
     * @param integral 本次消费积分
     * @return
     */
    @GetMapping(value = "/store/pwCheck")
    @ResponseBody
    public JsonResult pwCheck(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "password") String password, @RequestParam(value = "integral") Integer integral) {
        JsonResult jsonResult = new JsonResult();
        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId + ":" + ":" + integral;
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
        cacheJsonObj.put("pwCheck", 1);
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
        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId + ":" + ":" + integral;
        String cacheResult = this.redisService.get(cacheKey);
        if (cacheResult == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.NO_RESULT);
            return jsonResult;
        }

        UserCredits userCredits = this.userCreditsService.getUserCredits(userId, DBConstants.OwnerType.CUSTOMER);
        if (null == userCredits) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.CAN_NOT_GET_USER_CREDIT);
            return jsonResult;
        }

        JSONObject cacheJsonObj = JSON.parseObject(cacheResult);
        int orderStatus = NumberUtils.getInt(String.valueOf(cacheJsonObj.get("status")));
        if (orderStatus != 0) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.ORDER_STATUS_EXCEPTION);
            return jsonResult;
        }
        int pwCheck = NumberUtils.getInt(String.valueOf(cacheJsonObj.get("pwCheck")));
        if (pwCheck == 0) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.PASSWORD_NOT_CHECK);
            return jsonResult;
        }

        try {
            jsonResult = storeOfflineOrderService.integralTrad(userCredits, userId, NumberUtils.getInt(uid), integral, jsonResult);
        } catch (ApiServerException e) {
            System.out.println(e);
            jsonResult.setErrorCodeAndMessage(GlobalConstants.SYSTEM_EXCEPTION);
            e.printStackTrace();
            return jsonResult;
        }

        //修改缓存订单状态
        cacheJsonObj.put("status", 1);
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

        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId + ":" + ":" + integral;
        String cacheResult = this.redisService.get(cacheKey);
        if (cacheResult == null) {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.NO_RESULT);
            return jsonResult;
        }

        //修改缓存订单状态
        JSONObject cacheJsonObj = JSON.parseObject(cacheResult);
        cacheJsonObj.put("status", 2);
        this.redisService.put(cacheKey, cacheJsonObj.toString(), GlobalConstants.CACHE_DATA_FAILURE_TIME, TimeUnit.SECONDS);

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
                oderType = "积分买+现金买单";
            }
            offilineOrderDTO.setOrderType(oderType);
            offilineOrderDTO.setRemark("");
            offilineOrderDTOList.add(offilineOrderDTO);
        }
        return offilineOrderDTOList;
    }

}
