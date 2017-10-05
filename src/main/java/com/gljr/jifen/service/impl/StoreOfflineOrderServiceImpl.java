package com.gljr.jifen.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.exception.ApiServerException;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.*;
import com.gljr.jifen.util.DateUtils;
import com.qiniu.util.Json;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreOfflineOrderServiceImpl implements StoreOfflineOrderService {


    @Autowired
    private StoreOfflineOrderMapper storeOfflineOrderMapper;

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private  SerialNumberService serialNumberService;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private UserCreditsMapper userCreditsMapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Autowired
    private RedisService redisService;

    @Transactional
    @Override
    public JsonResult insertOfflineOrder(StoreOfflineOrder storeOfflineOrder, String uid, JsonResult jsonResult) {

        try {
            //积分不能小于0
            if(storeOfflineOrder.getIntegral() <= 0){
                jsonResult.setMessage("支付的积分不能小于0！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            //积分和总价是否相符
            if(storeOfflineOrder.getIntegral() != storeOfflineOrder.getTotalMoney() * 10) {
                jsonResult.setMessage("积分有误，请重试！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            //商户是否存在
            StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeOfflineOrder.getSiId());
            if(ValidCheck.validPojo(storeInfo)){
                jsonResult.setMessage("商户不存在，请确认商户信息！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            //添加一条通用交易信息
            Transaction transaction = new Transaction();
            transaction.setType(DBConstants.TrxType.OFFLINE.getCode());
            transaction.setOwnerType(DBConstants.OwnerType.CUSTOMER.getCode());
            transaction.setOwnerId(Integer.parseInt(uid));

            transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
            transaction.setCode(this.serialNumberService.getNextTrxCode(DBConstants.TrxType.OFFLINE.getCode()));
            transaction.setStatus(DBConstants.TrxStatus.UNPAID.getCode());

            //判断用户有多少积分，总共要消费多少积分，还差多少积分，转换成人民币
            UserCreditsExample userCreditsExample = new UserCreditsExample();
            UserCreditsExample.Criteria criteria = userCreditsExample.or();
            criteria.andOwnerIdEqualTo(Integer.parseInt(uid));

            List<UserCredits> userCreditss = userCreditsMapper.selectByExample(userCreditsExample);
            if(ValidCheck.validList(userCreditss)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            UserCredits userCredits = userCreditss.get(0);
            int integral = userCredits.getIntegral() - storeOfflineOrder.getIntegral();
            String message = "";
            if (integral >= 0) {
                transaction.setIntegral(-1 * storeOfflineOrder.getIntegral());
                storeOfflineOrder.setExtCash(0);
                message = "将从账户中扣除" + storeOfflineOrder.getIntegral() + "分";
            }else{
                integral = -integral/10;
                transaction.setIntegral(-1 * userCredits.getIntegral());
                storeOfflineOrder.setExtCash(integral);
                storeOfflineOrder.setIntegral(userCredits.getIntegral());
                message = "账户积分不足，将扣除" + userCredits.getIntegral() + "分，并支付人民币" + integral + "元";
            }

            transactionMapper.insert(transaction);


            storeOfflineOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
            storeOfflineOrder.setUid(Integer.parseInt(uid));
            storeOfflineOrder.setStatus(DBConstants.OrderStatus.UNPAID.getCode());
            storeOfflineOrder.setTrxCode(transaction.getCode());
            storeOfflineOrder.setTrxId(transaction.getId());

            storeOfflineOrderMapper.insert(storeOfflineOrder);

            Map map = new HashMap();
            map.put("data", storeOfflineOrder);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setItem(map);
            jsonResult.setMessage(storeOfflineOrder.getTrxCode());

        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }


        return jsonResult;
    }

    @Override
    public JsonResult selectOfflineOrderByUid(String uid, Integer page, Integer per_page, Integer sort, String start_time, String end_time, JsonResult jsonResult) {

        try{
            StoreOfflineOrderExample storeOfflineOrderExample = new StoreOfflineOrderExample();
            StoreOfflineOrderExample.Criteria criteria = storeOfflineOrderExample.or();
            criteria.andUidEqualTo(Integer.parseInt(uid));
            storeOfflineOrderExample.setOrderByClause("id desc");

            PageHelper.startPage(page,per_page);
            List<StoreOfflineOrder> storeOfflineOrders = storeOfflineOrderMapper.selectByExample(storeOfflineOrderExample);
            PageInfo pageInfo = new PageInfo(storeOfflineOrders);

            if(!ValidCheck.validList(storeOfflineOrders)) {

                for (StoreOfflineOrder storeOfflineOrder : storeOfflineOrders) {
                    StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeOfflineOrder.getSiId());
                    storeOfflineOrder.setName(storeInfo.getName());
                    if (storeOfflineOrder.getExtCash() == 0) {
                        storeOfflineOrder.setDescription("抵扣积分" + storeOfflineOrder.getIntegral() + "分");
                    } else {
                        storeOfflineOrder.setDescription("抵扣积分" + storeOfflineOrder.getIntegral() + "分，现金支付" + storeOfflineOrder.getExtCash() + "元");
                    }

                }
            }

            Map map = new HashMap();
            map.put("data", storeOfflineOrders);
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

    @Override
    public JsonResult selectOfflineOrders(JsonResult jsonResult) {

        try {
            StoreOfflineOrderExample storeOfflineOrderExample = new StoreOfflineOrderExample();
            storeOfflineOrderExample.setOrderByClause("id desc");

            List<StoreOfflineOrder> storeOfflineOrders = storeOfflineOrderMapper.selectByExample(storeOfflineOrderExample);
            if(!ValidCheck.validList(storeOfflineOrders)){
                for (StoreOfflineOrder storeOfflineOrder : storeOfflineOrders){
                    StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeOfflineOrder.getSiId());
                    if(ValidCheck.validPojo(storeInfo)){
                        storeOfflineOrder.setName("已删除");
                    }else {
                        storeOfflineOrder.setName(storeInfo.getName());
                    }

                }
            }

            Map map = new HashMap();
            map.put("data", storeOfflineOrders);

            CommonResult.success(jsonResult);
            jsonResult.setItem(map);

        } catch (Exception e) {

            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }


    @Override
    public List<StoreOfflineOrder> selectAllOfflineOrderByExample(int uid, OfflineOrderReqParam reqParam) {
        StoreOfflineOrderExample storeOfflineOrderExample = new StoreOfflineOrderExample();
        StoreOfflineOrderExample.Criteria criteria = storeOfflineOrderExample.or();
        criteria.andSiIdEqualTo(uid);
        //0-全部；1-今天；2-最近7天；3-最近30天；4-未结算；5-按时间查询（）;6-查询可退款的订单（24小时内）
        switch (reqParam.getQueryType()) {
            case 0:
                break;
            case 1:
                criteria.andCreateTimeBetween(DateUtils.getNowDayStart(), DateUtils.getNowDayEnd());
                break;
            case 2:
                criteria.andCreateTimeBetween(DateUtils.getSeveralDaysStart(7), DateUtils.getNowDayEnd());
                break;
            case 3:
                criteria.andCreateTimeBetween(DateUtils.getSeveralDaysStart(30), DateUtils.getNowDayEnd());
                break;
            case 4:
                criteria.andStatusEqualTo(1);
                break;
            case 5:
                //开始时间,格式为yyyyMMdd 20170102
                if (StringUtils.isBlank(reqParam.getStartTime())) {
                    Date startDate = DateUtils.formatToDate(reqParam.getStartTime());
                    criteria.andCreateTimeGreaterThanOrEqualTo(DateUtils.getOneDayStart(startDate));
                }

                //结束时间,格式为yyyyMMdd 20170102
                if (StringUtils.isBlank(reqParam.getEndTime())) {
                    Date endDate = DateUtils.formatToDate(reqParam.getEndTime());
                    criteria.andCreateTimeLessThanOrEqualTo(DateUtils.getOneDayStart(endDate));
                }

                //按照类型塞选交易记录
                switch (reqParam.getUseType()) {
                    case 1:
                        criteria.andIntegralGreaterThan(0);
                        criteria.andExtCashEqualTo(0);
                        break;
                    case 2:
                        criteria.andExtCashGreaterThan(0);
                        break;
                    default:
                        break;
                }

                //按照交易状态塞选
                switch (reqParam.getTransationStat()) {
                    case 1:
                        criteria.andStatusEqualTo(2);
                        break;
                    case 2:
                        criteria.andStatusEqualTo(5);
                        break;
                    default:
                        break;
                }
                break;
            case 6:
                criteria.andCreateTimeGreaterThan(DateUtils.getRallDate(new Date(), -1));
                criteria.andStatusEqualTo(2);
                break;
            default:
                break;
        }

        storeOfflineOrderExample.setOrderByClause("id desc");
        return storeOfflineOrderMapper.selectByExample(storeOfflineOrderExample);
    }

    @Override
    public HashMap getStoreTotalInfo(int uid){
        return storeOfflineOrderMapper.getStoreTotalInfo(uid);
    }

    @Override
    public List<StoreOfflineOrder> getStoreTotalInfo(String trxCode) {
        StoreOfflineOrderExample storeOfflineOrderExample = new StoreOfflineOrderExample();
        StoreOfflineOrderExample.Criteria criteria= storeOfflineOrderExample.or();
        criteria.andTrxCodeEqualTo(trxCode);
        return storeOfflineOrderMapper.selectByExample(storeOfflineOrderExample);
    }

    @Override
    public List<StoreOfflineOrder> getStoreTotalInfo(int uid, String trxCode) {
        StoreOfflineOrderExample storeOfflineOrderExample = new StoreOfflineOrderExample();
        StoreOfflineOrderExample.Criteria criteria= storeOfflineOrderExample.or();
        criteria.andTrxCodeEqualTo(trxCode);
        criteria.andSiIdEqualTo(uid);
        return storeOfflineOrderMapper.selectByExample(storeOfflineOrderExample);
    }

    @Override
    @Transactional
    public void refund(StoreOfflineOrder storeOfflineOrder) {
        Transaction transaction = transactionService.getTransactionById(storeOfflineOrder.getTrxId());
        if (transaction == null) {
            throw new ApiServerException(String.format("通用交易表中不存在该订单[%s]信息。", storeOfflineOrder.getTrxId()));
        }

        //积分返回到用户上
        List<UserCredits> userCreditsList = userCreditsService.selectUserCreditsByUid(storeOfflineOrder.getUid());
        if(userCreditsList.isEmpty()){
            throw new ApiServerException(String.format("无法获取用户[%s]的积分信息。", storeOfflineOrder.getUid()));
        }

        UserCredits userCredits = userCreditsList.get(0);
        userCredits.setIntegral(userCredits.getIntegral() + Math.abs(transaction.getIntegral()));
        userCreditsService.updateUserCreditsById(userCredits);

        //商户扣除得到的积分
        List<UserCredits> storeUserCreditsList = userCreditsService.selectUserCreditsByUid(storeOfflineOrder.getSiId());
        if(storeUserCreditsList.isEmpty()){
            throw new ApiServerException(String.format("无法获取商户[%s]的积分信息。", storeOfflineOrder.getSiId()));
        }

        UserCredits storeUserCredits = storeUserCreditsList.get(0);
        storeUserCredits.setIntegral(storeUserCredits.getIntegral() - Math.abs(transaction.getIntegral()));
        userCreditsService.updateUserCreditsById(storeUserCredits);

        //设置通用交易记录表订单为已退款
        transaction.setStatus(DBConstants.TrxStatus.REFUND.getCode());
        transactionService.updateTransaction(transaction);

        //更新线下订单
        storeOfflineOrder.setStatus(DBConstants.OfflineOrderStatus.REFUND.getCode());
        storeOfflineOrder.setUpdateTime(new Date());
        updateOfflineOrder(storeOfflineOrder);
    }

    @Override
    @Transactional
    public JsonResult integralTrad(UserCredits userCredits, int userId, int storeUid, int integral, JsonResult jsonResult) {

        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria = storeInfoExample.or();
        criteria.andAidEqualTo(storeUid);
        List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);

        StoreInfo storeInfo = storeInfos.get(0);

        if(ValidCheck.validList(storeInfos)){
            throw new ApiServerException("没有找到商户积分信息");
        }



        List<UserCredits> storeUserCreditsList = userCreditsService.selectUserCreditsByUid(storeInfo.getId());
        if (storeUserCreditsList.isEmpty()) {
            throw new ApiServerException("没有找到商户积分信息");
        }
        String orderCode = StrUtil.randomKey(18);
        double cash = 0L;
        if (userCredits.getIntegral() < integral) {
            cash = (integral - userCredits.getIntegral()) / GlobalConstants.INTEGRAL_RMB_EXCHANGE_RATIO;
        }
        //生产通用交易记录
        Transaction transaction = new Transaction();
        transaction.setType(DBConstants.OwnerType.CUSTOMER.getCode());
        transaction.setOwnerType(DBConstants.TrxType.OFFLINE.getCode());
        transaction.setOwnerId(userId);
        transaction.setCode(serialNumberService.getNextTrxCode(DBConstants.TrxType.OFFLINE.getCode()));
        transaction.setIntegral(-integral);
        transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
        transaction.setStatus(DBConstants.TrxStatus.COMPLETED.getCode());

        transactionService.insertTransaction(transaction);
        //生成线下订单
        StoreOfflineOrder storeOfflineOrder = new StoreOfflineOrder();
        storeOfflineOrder.setSiId(storeInfo.getId());
        storeOfflineOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
        storeOfflineOrder.setUid(userId);
        storeOfflineOrder.setStatus(DBConstants.OfflineOrderStatus.PAID.getCode());
        storeOfflineOrder.setTrxCode(transaction.getCode());
        storeOfflineOrder.setTrxId(transaction.getId());
        storeOfflineOrder.setExtCash((int) cash);
        storeOfflineOrder.setIntegral(integral);
        storeOfflineOrder.setTotalMoney((int) (integral / GlobalConstants.INTEGRAL_RMB_EXCHANGE_RATIO + cash));
        storeOfflineOrderMapper.insert(storeOfflineOrder);

        //扣除用户积分
        if (userCredits.getIntegral() <= integral) {
            userCredits.setIntegral(0);
        } else {
            userCredits.setIntegral(userCredits.getIntegral() - integral);
        }
        userCreditsMapper.updateByPrimaryKey(userCredits);

        //增加商户积分
        UserCredits storeUserCredits = storeUserCreditsList.get(0);
        storeUserCredits.setIntegral(storeUserCredits.getIntegral() + integral);
        userCreditsMapper.updateByPrimaryKey(userCredits);

        Map map = new HashMap();
        map.put("trxCode", transaction.getCode());
        jsonResult.setItem(map);

        String cacheKey = GlobalConstants.CHECK_INTEGRAL_RESULT_PREFIX + ":" + userId;
        redisService.evict(cacheKey);

        return  jsonResult;
    }

    @Override
    @Transactional
    public int updateOfflineOrder(StoreOfflineOrder storeOfflineOrder) {
        return storeOfflineOrderMapper.updateByPrimaryKey(storeOfflineOrder);
    }

    @Override
    public List<StoreOfflineOrder> selectAllOfflineOrderByUid(int uid, int sort, String start_time, String end_time) {
        StoreOfflineOrderExample storeOfflineOrderExample = new StoreOfflineOrderExample();
        StoreOfflineOrderExample.Criteria criteria= storeOfflineOrderExample.or();
        criteria.andUidEqualTo(uid);
        storeOfflineOrderExample.setOrderByClause("id desc");
        return storeOfflineOrderMapper.selectByExample(storeOfflineOrderExample);
    }
}
