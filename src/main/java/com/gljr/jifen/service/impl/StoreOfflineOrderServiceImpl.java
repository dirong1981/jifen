package com.gljr.jifen.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.SerialNumberService;
import com.gljr.jifen.service.StoreOfflineOrderService;
import com.gljr.jifen.service.TransactionService;
import com.gljr.jifen.service.UserCreditsService;
import com.gljr.jifen.util.DateUtils;
import com.qiniu.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public JsonResult selectOfflineOrderByUid(String uid, int sort, String start_time, String end_time, JsonResult jsonResult) {

        try{
            StoreOfflineOrderExample storeOfflineOrderExample = new StoreOfflineOrderExample();
            StoreOfflineOrderExample.Criteria criteria = storeOfflineOrderExample.or();
            criteria.andUidEqualTo(Integer.parseInt(uid));
            storeOfflineOrderExample.setOrderByClause("id desc");

            List<StoreOfflineOrder> storeOfflineOrders = storeOfflineOrderMapper.selectByExample(storeOfflineOrderExample);

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

}
