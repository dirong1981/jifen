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
import com.gljr.jifen.service.*;
import com.gljr.jifen.util.DateUtils;
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
public class IntegralTransferOrderServiceImpl implements IntegralTransferOrderService {

    @Autowired
    private IntegralTransferOrderMapper integralTransferOrderMapper;

    @Autowired
    private UserExtInfoMapper userExtInfoMapper;

    @Autowired
    private UserCreditsMapper userCreditsMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private RedisService redisService;

    @Transactional
    @Override
    public JsonResult insertIntegralOrder(IntegralTransferOrder integralTransferOrder, String uid, JsonResult jsonResult)  {


        try {
            //判断对方是否存在
            UserExtInfoExample userExtInfoExample = new UserExtInfoExample();
            UserExtInfoExample.Criteria criteria = userExtInfoExample.or();
            criteria.andUidEqualTo(integralTransferOrder.getgUid());

            List<UserExtInfo> userExtInfos = userExtInfoMapper.selectByExample(userExtInfoExample);
            if(ValidCheck.validList(userExtInfos)){
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage("转账的用户不存在，请查询后再试！");
                return jsonResult;
            }
            UserExtInfo userExtInfo = userExtInfos.get(0);


            //判断积分是否足够
            UserCreditsExample userCreditsExample = new UserCreditsExample();
            UserCreditsExample.Criteria criteria1 = userCreditsExample.or();
            criteria1.andOwnerIdEqualTo(Integer.parseInt(uid));

            List<UserCredits> userCreditss = userCreditsMapper.selectByExample(userCreditsExample);
            if(ValidCheck.validList(userCreditss)){
                CommonResult.userNotExit(jsonResult);
                return jsonResult;
            }

            UserCredits userCredits = userCreditss.get(0);

            //减少积分
            if(userCredits.getIntegral() < integralTransferOrder.getIntegral()){
                jsonResult.setMessage("积分不足！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }else{
                userCredits.setIntegral(userCredits.getIntegral() - integralTransferOrder.getIntegral());
            }

            userCreditsMapper.updateByPrimaryKey(userCredits);

            //增加受转账积分
            userCreditsExample = new UserCreditsExample();
            criteria1 = userCreditsExample.or();
            criteria1.andOwnerIdEqualTo(integralTransferOrder.getgUid());

            userCreditss = userCreditsMapper.selectByExample(userCreditsExample);
            userCredits = userCreditss.get(0);
            userCredits.setIntegral(userCredits.getIntegral() + integralTransferOrder.getIntegral());

            userCreditsMapper.updateByPrimaryKey(userCredits);


            //添加一个交易通用信息
            Transaction transaction = new Transaction();
            transaction.setCode(this.serialNumberService.getNextTrxCode(DBConstants.TrxType.TRANSFER.getCode()));
            transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
            transaction.setIntegral(-1 * integralTransferOrder.getIntegral());
            transaction.setOwnerId(Integer.parseInt(uid));
            transaction.setOwnerType(DBConstants.OwnerType.CUSTOMER.getCode());
            transaction.setType(DBConstants.TrxType.TRANSFER.getCode());
            transaction.setStatus(DBConstants.TrxStatus.COMPLETED.getCode());

            transactionMapper.insert(transaction);

            String title = "向" + userExtInfo.getCellphone() + "转账" + integralTransferOrder.getIntegral() + "分";
            //添加一个积分转增订单
            integralTransferOrder.setUid(Integer.parseInt(uid));
            integralTransferOrder.setStatus(DBConstants.OrderStatus.PAID.getCode());
            integralTransferOrder.setTitle(title);
            integralTransferOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
            integralTransferOrder.setTrxId(transaction.getId());
            integralTransferOrder.setTrxCode(transaction.getCode());

            integralTransferOrderMapper.insert(integralTransferOrder);


            //创建一个消息
            userExtInfoExample = new UserExtInfoExample();
            criteria = userExtInfoExample.or();
            criteria.andUidEqualTo(Integer.parseInt(uid));
            userExtInfos = userExtInfoMapper.selectByExample(userExtInfoExample);
            userExtInfo = userExtInfos.get(0);

            title = userExtInfo.getCellphone() + "向您转账" + integralTransferOrder.getIntegral() + "分";
            Message message = new Message();
            message.setReadStatus(0);
            message.setContent(title);
            message.setCreateTime(new Timestamp(System.currentTimeMillis()));
            message.setUid(integralTransferOrder.getgUid());

            messageMapper.insert(message);

            redisService.evict(uid+"random");

            jsonResult.setMessage(integralTransferOrder.getTrxCode());
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }


        return jsonResult;
    }

    @Override
    public JsonResult selectIntegralOrderByuid(String uid, Integer page, Integer per_page, Integer sort, String start_time, String end_time, JsonResult jsonResult) {

        try {
            IntegralTransferOrderExample integralTransferOrderExample = new IntegralTransferOrderExample();
            IntegralTransferOrderExample.Criteria criteria = integralTransferOrderExample.or();
            criteria.andUidEqualTo(Integer.parseInt(uid));
            integralTransferOrderExample.setOrderByClause("id desc");

            PageHelper.startPage(page,per_page);
            List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderMapper.selectByExample(integralTransferOrderExample);
            PageInfo pageInfo = new PageInfo(integralTransferOrders);

            if(!ValidCheck.validList(integralTransferOrders)) {
                for (IntegralTransferOrder integralTransferOrder : integralTransferOrders) {
                    UserExtInfoExample userExtInfoExample = new UserExtInfoExample();
                    UserExtInfoExample.Criteria criteria1 = userExtInfoExample.or();
                    criteria1.andUidEqualTo(integralTransferOrder.getgUid());

                    List<UserExtInfo> userExtInfos = userExtInfoMapper.selectByExample(userExtInfoExample);
                    if(ValidCheck.validList(userExtInfos)){
                        integralTransferOrder.setName("用户不存在");
                    }else {
                        integralTransferOrder.setName(userExtInfos.get(0).getCellphone());
                        integralTransferOrder.setDescription("转账" + integralTransferOrder.getIntegral() + "分");
                    }

                }
            }

            Map map = new HashMap();
            map.put("data", integralTransferOrders);
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
    public JsonResult selectIntegralOrders(JsonResult jsonResult) {

        try {
            IntegralTransferOrderExample integralTransferOrderExample = new IntegralTransferOrderExample();
            integralTransferOrderExample.setOrderByClause("id desc");
            List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderMapper.selectByExample(integralTransferOrderExample);

            if(!ValidCheck.validList(integralTransferOrders)){
                for (IntegralTransferOrder integralTransferOrder : integralTransferOrders){
                    UserExtInfoExample userExtInfoExample = new UserExtInfoExample();
                    UserExtInfoExample.Criteria criteria = userExtInfoExample.or();
                    criteria.andUidEqualTo(integralTransferOrder.getUid());
                    List<UserExtInfo> userExtInfos = userExtInfoMapper.selectByExample(userExtInfoExample);
                    if(ValidCheck.validList(userExtInfos)){
                        integralTransferOrder.setName("用户已删除");
                    }else{
                        integralTransferOrder.setName(userExtInfos.get(0).getCellphone());
                    }

                    userExtInfoExample = new UserExtInfoExample();
                    criteria = userExtInfoExample.or();
                    criteria.andUidEqualTo(integralTransferOrder.getgUid());
                    userExtInfos = userExtInfoMapper.selectByExample(userExtInfoExample);
                    if(ValidCheck.validList(userExtInfos)){
                        integralTransferOrder.setDescription("用户已删除");
                    }else{
                        integralTransferOrder.setDescription(userExtInfos.get(0).getCellphone());
                    }
                }
            }

            Map map = new HashMap();
            map.put("data", integralTransferOrders);

            CommonResult.success(jsonResult);
            jsonResult.setItem(map);
        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }


}
