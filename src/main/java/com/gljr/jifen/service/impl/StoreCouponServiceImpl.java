package com.gljr.jifen.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.common.dtchain.vo.CommonOrderResponse;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.DTChainService;
import com.gljr.jifen.service.SerialNumberService;
import com.gljr.jifen.service.StoreCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class StoreCouponServiceImpl extends BaseService implements StoreCouponService {

    @Autowired
    private StoreCouponMapper storeCouponMapper;

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private UserCreditsMapper userCreditsMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private OnlineOrderMapper onlineOrderMapper;

    @Autowired
    private StoreOfflineOrderMapper storeOfflineOrderMapper;

    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private DTChainService chainService;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private OrderRefundMapper orderRefundMapper;

    @Autowired
    private StoreCouponOrderMapper storeCouponOrderMapper;

    private final static int INVALID_INTEGRAL_AMOUNT = 402;

    private final static int STORE_COUPON_NOT_FOUND = 416;

    private final static int INVALID_STORE_COUPON_STATUS = 417;

    private final static int INVALID_PRODUCT_INTEGRAL = 404;

    private final static int INVALID_STORE_COUPON_EXPIRE_CONFIG = 418;

    private final static int INSUFFICIENT_INTEGRAL_AMOUNT = 401;

    @Override
    public JsonResult insertStoreCoupon(StoreCoupon storeCoupon) {

        try {
            storeCouponMapper.insert(storeCoupon);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    @Override
    public JsonResult selectAllStoreCoupon(Integer page, Integer per_page) {
        try {
            StoreCouponExample storeCouponExample = new StoreCouponExample();
            StoreCouponExample.Criteria criteria = storeCouponExample.or();
            criteria.andStatusNotEqualTo(DBConstants.ProductStatus.DELETED.getCode());
            storeCouponExample.setOrderByClause("id desc");

            PageHelper.startPage(page,per_page);
            List<StoreCoupon> storeCoupons = storeCouponMapper.selectByExample(storeCouponExample);
            PageInfo pageInfo = new PageInfo(storeCoupons);

            if(!ValidCheck.validList(storeCoupons)) {
                for (StoreCoupon storeCoupon : storeCoupons) {
                    StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeCoupon.getSiId());

                    if(!ValidCheck.validPojo(storeInfo)){
                        storeCoupon.setStoreName(storeInfo.getName());
                    }

                    if(storeCoupon.getValidityType() == 1){
                        long df = storeCoupon.getValidFrom().getTime();
                        long dt = storeCoupon.getValidTo().getTime();
                        long mi = dt - df;
                        if(mi == 0){
                            storeCoupon.setValidDays(1);
                        }else {
                            storeCoupon.setValidDays(Integer.parseInt(TimeUnit.MILLISECONDS.toDays(mi) + ""));
                        }
                    }

                    UserCouponExample userCouponExample = new UserCouponExample();
                    UserCouponExample.Criteria criteria1 = userCouponExample.or();
                    criteria1.andScIdEqualTo(storeCoupon.getId());
                    criteria1.andSiIdEqualTo(storeCoupon.getSiId());
                    criteria1.andStatusNotEqualTo(DBConstants.CouponStatus.REFUND.getCode());

                    long count = userCouponMapper.countByExample(userCouponExample);

                    storeCoupon.setRemainingAmount(storeCoupon.getMaxGenerated() - Integer.parseInt(count+""));
                }
            }

            Map map = new HashMap();
            map.put("data", storeCoupons);
            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult startStoreCoupon(Integer couponId) {
        try{
            StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(couponId);

            if(storeCoupon.getStatus() == 0){
                storeCoupon.setStatus(1);

                storeCouponMapper.updateByPrimaryKey(storeCoupon);
            }

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult stopStoreCoupon(Integer couponId) {
        try{
            StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(couponId);

            if(storeCoupon.getStatus() == 1){
                storeCoupon.setStatus(0);

                storeCouponMapper.updateByPrimaryKey(storeCoupon);
            }

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult deleteStoreCoupon(Integer couponId) {
        try {
            StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(couponId);

            if(storeCoupon.getStatus() != -1){
                storeCoupon.setStatus(-1);

                storeCouponMapper.updateByPrimaryKey(storeCoupon);
            }

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectStoreCouponById(Integer couponId) {
        try {
            StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(couponId);

            StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeCoupon.getSiId());
            storeCoupon.setStoreName(storeInfo.getName());

            UserCouponExample userCouponExample = new UserCouponExample();
            UserCouponExample.Criteria criteria = userCouponExample.or();
            criteria.andSiIdEqualTo(storeCoupon.getSiId());
            criteria.andScIdEqualTo(couponId);

            long count = userCouponMapper.countByExample(userCouponExample);
            storeCoupon.setRemainingAmount(storeCoupon.getMaxGenerated() - Integer.parseInt(count + ""));

            Map map = new HashMap();
            map.put("data", storeCoupon);
            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectUserCouponById(Integer couponId, String uid) {
        try{
            UserCouponExample userCouponExample = new UserCouponExample();
            UserCouponExample.Criteria criteria = userCouponExample.or();
            criteria.andUidEqualTo(Integer.parseInt(uid));
            criteria.andIdEqualTo(couponId);

            List<UserCoupon> userCoupons = userCouponMapper.selectByExample(userCouponExample);
            if(ValidCheck.validList(userCoupons)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            UserCoupon userCoupon = userCoupons.get(0);

            if(userCoupon.getValidFrom().after(new Date())){
                userCoupon.setStatus(0);
            }

            if(userCoupon.getValidTo().before(new Date())){
                userCoupon.setStatus(DBConstants.CouponStatus.EXPIRED.getCode());
            }

            StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(userCoupon.getScId());

            StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeCoupon.getSiId());

            userCoupon.setStoreName(storeInfo.getName());
            userCoupon.setMinConsumption(storeCoupon.getMinConsumption());
            userCoupon.setIntegral(storeCoupon.getIntegral());
            userCoupon.setEqualMoney(storeCoupon.getEqualMoney());


            LocationExample locationExample = new LocationExample();
            LocationExample.Criteria criteria1 = locationExample.or();
            criteria1.andCodeEqualTo(storeInfo.getLocationCode());

            List<Location> locations = locationMapper.selectByExample(locationExample);

            String address = locations.get(0).getName() + storeInfo.getAddress();

            criteria1 = locationExample.or();
            criteria1.andCodeEqualTo(locations.get(0).getParentCode());
            locations = locationMapper.selectByExample(locationExample);

            address = locations.get(0).getName() + address;
//
//            criteria1.andCodeEqualTo(locations.get(0).getParentCode());
//            locations = locationMapper.selectByExample(locationExample);

//            address = locations.get(0).getName() + address;
            userCoupon.setStoreAddress(address);


            Map map = new HashMap();
            map.put("data", userCoupon);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setItem(null);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectStoreCouponByUid(String uid, Integer page, Integer per_page, Integer sort, String start_time, String end_time) {

        try {
            UserCouponExample userCouponExample = new UserCouponExample();
            UserCouponExample.Criteria criteria = userCouponExample.or();
            criteria.andUidEqualTo(Integer.parseInt(uid));
            criteria.andStatusNotEqualTo(DBConstants.CouponStatus.USED.getCode());
            criteria.andStatusNotEqualTo(DBConstants.CouponStatus.REFUND.getCode());

            if(!StringUtils.isEmpty(start_time) && !StringUtils.isEmpty(end_time)){
                criteria.andCreateTimeBetween(new Date(Long.parseLong(start_time)), new Date(Long.parseLong(end_time)+86400000));
            }
            userCouponExample.setOrderByClause("id desc");

            PageHelper.startPage(page,per_page);
            List<UserCoupon> userCoupons = userCouponMapper.selectByExample(userCouponExample);
            PageInfo pageInfo = new PageInfo(userCoupons);

            for (UserCoupon userCoupon : userCoupons){


                StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(userCoupon.getScId());

                userCoupon.setEqualMoney(storeCoupon.getEqualMoney());
                userCoupon.setIntegral(storeCoupon.getIntegral());
                userCoupon.setMinConsumption(storeCoupon.getMinConsumption());

                StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeCoupon.getSiId());
                userCoupon.setStoreName(storeInfo.getName());

                if(userCoupon.getValidFrom().after(new Date())){
                    userCoupon.setStatus(0);
                }

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                calendar.add(calendar.DATE, -1);
                Date end = calendar.getTime();

                if(userCoupon.getValidTo().before(end)){
                    userCoupon.setStatus(DBConstants.CouponStatus.EXPIRED.getCode());
                }
            }

            Map map = new HashMap();
            map.put("data", userCoupons);
            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setItem(null);
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult insertStoreCouponOrder(Integer scId, String uid) {

        try {
            StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(scId);

            if(ValidCheck.validPojo(storeCoupon)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            UserCouponExample userCouponExample = new UserCouponExample();
            UserCouponExample.Criteria criteria = userCouponExample.or();
            criteria.andSiIdEqualTo(storeCoupon.getSiId());
            criteria.andScIdEqualTo(storeCoupon.getId());
            criteria.andStatusNotEqualTo(DBConstants.CouponStatus.REFUND.getCode());

            long count = userCouponMapper.countByExample(userCouponExample);
            int remain = storeCoupon.getMaxGenerated() - Integer.parseInt(count + "");
            if(remain <= 0){
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage("库存不足！");
                return jsonResult;
            }

            UserCredits userCredits = userCreditsMapper.getUserCredits(Integer.parseInt(uid), DBConstants.OwnerType.CUSTOMER.getCode());

            if(userCredits.getIntegral() < storeCoupon.getIntegral()){
                jsonResult.setMessage("用户积分不足！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            GatewayResponse<CommonOrderResponse> response = this.chainService.postOrders(Integer.parseInt(uid) + 0L,
                    storeCoupon.getId() + 0L, DBConstants.OnlineProductType.STORE_COUPON.getCode(), storeCoupon.getIntegral(), 1);

            if (null == response) {
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            if (INSUFFICIENT_INTEGRAL_AMOUNT == response.getCode()) {
                jsonResult.setMessage("用户积分不足！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            if (INVALID_INTEGRAL_AMOUNT == response.getCode()) {
                jsonResult.setMessage("要支付的积分数额不正确！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            if (STORE_COUPON_NOT_FOUND == response.getCode()) {
                jsonResult.setMessage("该商户代金券不存在！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            if (INVALID_STORE_COUPON_STATUS == response.getCode()) {
                jsonResult.setMessage("该商品已下架！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            if (INVALID_PRODUCT_INTEGRAL == response.getCode()) {
                jsonResult.setMessage("要支付的积分与代金券积分不匹配！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            if (INVALID_STORE_COUPON_EXPIRE_CONFIG == response.getCode()) {
                jsonResult.setMessage("该商户代金券的失效时间配置有误，请联系商户处理！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            if(response.getCode() != 200) {
                jsonResult.setMessage("代金券购买失败，请联系客服！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            CommonOrderResponse cor = response.getContent();

            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setScId(storeCoupon.getId());
            userCoupon.setSiId(storeCoupon.getSiId());
            userCoupon.setUid(Integer.parseInt(uid));
            userCoupon.setCouponCode(cor.getRefData());

            if(storeCoupon.getValidityType() == 1){
                userCoupon.setValidFrom(storeCoupon.getValidFrom());
                userCoupon.setValidTo(storeCoupon.getValidTo());
            }else {
                userCoupon.setValidFrom(new Timestamp(System.currentTimeMillis()));
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                calendar.add(calendar.DAY_OF_YEAR, storeCoupon.getValidDays());
                userCoupon.setValidTo(calendar.getTime());
            }

            userCoupon.setStatus(DBConstants.CouponStatus.VALID.getCode());
            userCouponMapper.insert(userCoupon);

//            StoreOfflineOrder storeOfflineOrder = new StoreOfflineOrder();
//            storeOfflineOrder.setSiId(storeCoupon.getSiId());
//            storeOfflineOrder.setUcId(userCoupon.getId());
//            storeOfflineOrder.setUid(Integer.parseInt(uid));
//            storeOfflineOrder.setDtchainBlockId(cor.getBlockId());
//            storeOfflineOrder.setExtOrderId(cor.getExtOrderId());
//            storeOfflineOrder.setIntegral(storeCoupon.getIntegral());
//            storeOfflineOrder.setExtCash(0);
//            storeOfflineOrder.setTotalMoney(0);
//            storeOfflineOrder.setStatus(DBConstants.OrderStatus.PAID.getCode());
//            storeOfflineOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
//            storeOfflineOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));

            //添加一条通用交易信息
            Transaction transaction = new Transaction();
            transaction.setType(DBConstants.TrxType.OFFLINE.getCode());
            transaction.setOwnerType(DBConstants.OwnerType.CUSTOMER.getCode());
            transaction.setOwnerId(Integer.parseInt(uid));
            transaction.setIntegral(-1 * storeCoupon.getIntegral());
            transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
            transaction.setStatus(DBConstants.TrxStatus.COMPLETED.getCode());
            transaction.setCode(serialNumberService.getNextTrxCode(DBConstants.TrxType.OFFLINE.getCode()));

            transactionMapper.insert(transaction);

            StoreCouponOrder storeCouponOrder = new StoreCouponOrder();
            storeCouponOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
            storeCouponOrder.setIntegral(storeCoupon.getIntegral());
            storeCouponOrder.setSiId(storeCoupon.getSiId());
            storeCouponOrder.setTrxCode(transaction.getCode());
            storeCouponOrder.setTrxId(transaction.getId());
            storeCouponOrder.setUcId(userCoupon.getId());
            storeCouponOrder.setDtchainBlockId(response.getContent().getBlockId());

            storeCouponOrderMapper.insert(storeCouponOrder);

//            storeOfflineOrder.setTrxId(transaction.getId());
//            storeOfflineOrder.setTrxCode(transaction.getCode());
//
//            storeOfflineOrderMapper.insert(storeOfflineOrder);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(transaction.getCode());
            jsonResult.setItem(null);
        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult refundStoreCouponOrder(Integer couponId, String uid) {
        try {
//            StoreOfflineOrderExample storeOfflineOrderExample = new StoreOfflineOrderExample();
//            StoreOfflineOrderExample.Criteria criteria = storeOfflineOrderExample.or();
//            criteria.andUcIdEqualTo(couponId);
//            criteria.andUidEqualTo(Integer.parseInt(uid));
//            List<StoreOfflineOrder> storeOfflineOrders = storeOfflineOrderMapper.selectByExample(storeOfflineOrderExample);
//            if(ValidCheck.validList(storeOfflineOrders)){
//                CommonResult.noObject(jsonResult);
//                return jsonResult;
//            }
//            StoreOfflineOrder storeOfflineOrder = storeOfflineOrders.get(0);
//            if(storeOfflineOrder.getStatus() == DBConstants.OrderStatus.REFUND.getCode()){
//                return jsonResult;
//            }

            UserCouponExample userCouponExample = new UserCouponExample();
            UserCouponExample.Criteria criteria = userCouponExample.or();
            criteria.andStatusNotEqualTo(DBConstants.CouponStatus.USED.getCode());
            criteria.andIdEqualTo(couponId);
            criteria.andUidEqualTo(Integer.parseInt(uid));

            List<UserCoupon> userCoupons = userCouponMapper.selectByExample(userCouponExample);
            if(ValidCheck.validList(userCoupons)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            UserCoupon userCoupon = userCoupons.get(0);

            GatewayResponse<CommonOrderResponse> response = this.chainService.couponRefund(userCoupon.getSiId() + 0L, userCoupon.getCouponCode());

            if(response.getCode() != 200) {
                jsonResult.setMessage("该代金券暂不支持退款，请联系管理员！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(userCoupon.getScId());

            if(storeCoupon.getStatus() == DBConstants.StoreCouponStatus.SOLD_OUT.getCode()){
                storeCoupon.setStatus(DBConstants.StoreCouponStatus.ACTIVED.getCode());
                storeCouponMapper.updateByPrimaryKey(storeCoupon);
            }

            //添加一条通用交易信息
            Transaction transaction = new Transaction();
            transaction.setType(DBConstants.TrxType.REFUND.getCode());
            transaction.setOwnerType(DBConstants.OwnerType.CUSTOMER.getCode());
            transaction.setOwnerId(Integer.parseInt(uid));
            transaction.setIntegral(storeCoupon.getIntegral());
            transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
            transaction.setStatus(DBConstants.TrxStatus.REFUND.getCode());
            transaction.setCode(serialNumberService.getNextTrxCode(DBConstants.TrxType.REFUND.getCode()));

            transactionMapper.insert(transaction);

            OrderRefund orderRefund = new OrderRefund();
            orderRefund.setCreated(new Date());
            orderRefund.setDtchainBlockId(response.getContent().getBlockId());
            orderRefund.setExtOrderId(response.getContent().getExtOrderId());
            orderRefund.setIntegral(transaction.getIntegral());
            orderRefund.setOrderId(userCoupon.getId());
            orderRefund.setOrderType(1);
            orderRefund.setStoreId(storeCoupon.getSiId());
            orderRefund.setToUid(Integer.parseInt(uid));
            orderRefund.setTrxId(transaction.getId());
            orderRefund.setTrxCode(transaction.getCode());

            orderRefundMapper.refundOrder(orderRefund);

//            storeOfflineOrder.setStatus(DBConstants.OrderStatus.REFUND.getCode());
//            storeOfflineOrderMapper.updateByPrimaryKey(storeOfflineOrder);

            String title = "代金券退还" + storeCoupon.getIntegral() + "积分";
            Message message = new Message();
            message.setReadStatus(0);
            message.setContent(title);
            message.setCreateTime(new Timestamp(System.currentTimeMillis()));
            message.setUid(Integer.parseInt(uid));

            messageMapper.insert(message);

            CommonResult.success(jsonResult);

        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectStoreCouponStatusById(Integer couponId) {
        try{
            UserCoupon userCoupon = userCouponMapper.selectByPrimaryKey(couponId);
            Map map = new HashMap();
            map.put("data", userCoupon);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult allowCancelStoreCouponById(Integer couponId, Integer allow) {
        try{
            StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(couponId);

            storeCoupon.setAllowCancel(allow);

            storeCouponMapper.updateByPrimaryKey(storeCoupon);
            CommonResult.success(jsonResult);
            jsonResult.setItem(null);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
            jsonResult.setItem(null);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectCouponOrders(Integer page, Integer per_page, String trxCode, Integer status, Date begin, Date end, JsonResult jsonResult) {
        try{
            StoreCouponOrderExample storeCouponOrderExample = new StoreCouponOrderExample();
            StoreCouponOrderExample.Criteria criteria = storeCouponOrderExample.or();

            if(!StringUtils.isEmpty(trxCode)){
                criteria.andTrxCodeEqualTo(trxCode);
            }
//            if(!StringUtils.isEmpty(status)){
//                criteria.andStatusEqualTo(status);
//            }
            criteria.andCreateTimeBetween(begin, end);
            storeCouponOrderExample.setOrderByClause("id desc");

            PageHelper.startPage(page,per_page);
            List<StoreCouponOrder> storeCouponOrders = storeCouponOrderMapper.selectByExample(storeCouponOrderExample);
            PageInfo pageInfo = new PageInfo(storeCouponOrders);

            List<UserCoupon> userCoupons = new ArrayList<>();
            List<UserCoupon> del = new ArrayList<>();

            if(!ValidCheck.validList(storeCouponOrders)){
                for (StoreCouponOrder storeCouponOrder : storeCouponOrders) {
                    UserCoupon userCoupon = new UserCoupon();

                    StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeCouponOrder.getSiId());
                    userCoupon.setStoreName(storeInfo.getName());

                    UserCoupon userCoupon1 = userCouponMapper.selectByPrimaryKey(storeCouponOrder.getUcId());

                    userCoupon.setStatus(userCoupon1.getStatus());

                    if(!StringUtils.isEmpty(status)){
                        if(userCoupon1.getStatus() != status){
                            del.add(userCoupon);
                        }
                    }

                    StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(userCoupon1.getScId());
                    userCoupon.setEqualMoney(storeCoupon.getEqualMoney());
                    userCoupon.setIntegral(storeCoupon.getIntegral());

                    userCoupon.setStoreAddress(storeCouponOrder.getTrxCode());

                    userCoupon.setCreateTime(storeCouponOrder.getCreateTime());

                    userCoupons.add(userCoupon);
                }
            }

            userCoupons.removeAll(del);

            Map map = new HashMap();
            map.put("data", userCoupons);
            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

            CommonResult.success(jsonResult);
            jsonResult.setItem(map);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }
}
