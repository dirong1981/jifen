package com.gljr.jifen.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.common.dtchain.vo.CommonOrderResponse;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

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
    private DTChainService chainService;

    @Autowired
    private RedisService redisService;

    public final static int INSUFFICIENT_INTEGRAL_AMOUNT = 401;

    public final static int INVALID_INTEGRAL_AMOUNT = 402;


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

            String gphone = userExtInfo.getCellphone();

            //判断积分是否足够
            UserCredits userCredits = this.userCreditsMapper.getUserCredits(Integer.parseInt(uid));

            if (null == userCredits) {
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage("用户积分信息不存在！");
                return jsonResult;
            }

            //减少积分
            if(userCredits.getIntegral() < integralTransferOrder.getIntegral()){
                jsonResult.setMessage("积分不足！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            integralTransferOrder.setUid(Integer.parseInt(uid));
            GatewayResponse<CommonOrderResponse> response = this.chainService.transferIntegral(integralTransferOrder.getUid() + 0L,
                    integralTransferOrder.getgUid() + 0L, integralTransferOrder.getIntegral());

            if (null == response) {
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            if (INSUFFICIENT_INTEGRAL_AMOUNT == response.getCode()) {
                jsonResult.setMessage("积分不足！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            if (INVALID_INTEGRAL_AMOUNT == response.getCode()) {
                jsonResult.setMessage("无效的转赠积分数额！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            //添加一个交易通用信息
            String trxCode = this.serialNumberService.getNextTrxCode(DBConstants.TrxType.TRANSFER.getCode());

            Transaction transaction = new Transaction();
            transaction.setCode(trxCode);
            transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
            transaction.setIntegral(-1 * integralTransferOrder.getIntegral());
            transaction.setOwnerId(integralTransferOrder.getUid());
            transaction.setOwnerType(DBConstants.OwnerType.CUSTOMER.getCode());
            transaction.setType(DBConstants.TrxType.TRANSFER.getCode());
            transaction.setStatus(DBConstants.TrxStatus.COMPLETED.getCode());
            transactionMapper.insert(transaction);

            transaction = new Transaction();
            transaction.setCode(trxCode);
            transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
            transaction.setIntegral(integralTransferOrder.getIntegral());
            transaction.setOwnerId(integralTransferOrder.getgUid());
            transaction.setOwnerType(DBConstants.OwnerType.CUSTOMER.getCode());
            transaction.setType(DBConstants.TrxType.TRANSFER.getCode());
            transaction.setStatus(DBConstants.TrxStatus.COMPLETED.getCode());
            transactionMapper.insert(transaction);



            //添加一个积分转增订单
            integralTransferOrder.setUid(Integer.parseInt(uid));
            integralTransferOrder.setStatus(DBConstants.OrderStatus.PAID.getCode());
            //integralTransferOrder.setTitle(title);
            integralTransferOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
            integralTransferOrder.setTrxId(transaction.getId());
            integralTransferOrder.setTrxCode(transaction.getCode());

            if (null != response.getContent()) {
                CommonOrderResponse cor = response.getContent();
                integralTransferOrder.setDtchainBlockId(cor.getBlockId());
                integralTransferOrder.setExtOrderId(cor.getExtOrderId());
            }

            integralTransferOrderMapper.insert(integralTransferOrder);

            //创建一个消息
            userExtInfoExample = new UserExtInfoExample();
            criteria = userExtInfoExample.or();
            criteria.andUidEqualTo(Integer.parseInt(uid));
            userExtInfos = userExtInfoMapper.selectByExample(userExtInfoExample);
            userExtInfo = userExtInfos.get(0);

            String title = userExtInfo.getCellphone() + "向您转账" + integralTransferOrder.getIntegral() + "分";
            Message message = new Message();
            message.setReadStatus(0);
            message.setContent(title);
            message.setCreateTime(new Timestamp(System.currentTimeMillis()));
            message.setUid(integralTransferOrder.getgUid());

            messageMapper.insert(message);



            title = "您向" + gphone + "转账" + integralTransferOrder.getIntegral() + "分";
            message = new Message();
            message.setReadStatus(0);
            message.setContent(title);
            message.setCreateTime(new Timestamp(System.currentTimeMillis()));
            message.setUid(integralTransferOrder.getUid());

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

            TransactionExample transactionExample = new TransactionExample();
            TransactionExample.Criteria criteria = transactionExample.or();
            criteria.andOwnerTypeEqualTo(DBConstants.OwnerType.CUSTOMER.getCode());
            criteria.andOwnerIdEqualTo(Integer.parseInt(uid));
            criteria.andTypeEqualTo(DBConstants.TrxType.TRANSFER.getCode());
            if(!StringUtils.isEmpty(start_time) && !StringUtils.isEmpty(end_time)){
                criteria.andCreateTimeBetween(new Date(Long.parseLong(start_time)), new Date(Long.parseLong(end_time)));
            }
            transactionExample.setOrderByClause("id desc");

            PageHelper.startPage(page,per_page);
            List<Transaction> transactions = transactionMapper.selectByExample(transactionExample);
            PageInfo pageInfo = new PageInfo(transactions);

            List<UserOrder> userOrders = new ArrayList<>();
            if(!ValidCheck.validList(transactions)) {
                for (Transaction transaction : transactions) {
                    IntegralTransferOrderExample integralTransferOrderExample = new IntegralTransferOrderExample();
                    IntegralTransferOrderExample.Criteria criteria1 = integralTransferOrderExample.or();
                    criteria1.andTrxCodeEqualTo(transaction.getCode());
                    List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderMapper.selectByExample(integralTransferOrderExample);
                    if (!ValidCheck.validList(integralTransferOrders)) {
                        IntegralTransferOrder integralTransferOrder = integralTransferOrders.get(0);

                        UserOrder userOrder = new UserOrder();

                        userOrder.setTrxType(DBConstants.TrxType.TRANSFER.getCode());
                        userOrder.setTrxCode(integralTransferOrder.getTrxCode());
                        userOrder.setStatus(5);
                        userOrder.setQuantity(0);
                        userOrder.setIntegral(transaction.getIntegral());
                        userOrder.setId(integralTransferOrder.getId());
                        userOrder.setCreateTime(integralTransferOrder.getCreateTime());

                        UserExtInfoExample userExtInfoExample = new UserExtInfoExample();
                        UserExtInfoExample.Criteria criteria3 = userExtInfoExample.or();
                        if (transaction.getIntegral() > 0) {
                            criteria3.andUidEqualTo(integralTransferOrder.getUid());
                        } else {
                            criteria3.andUidEqualTo(integralTransferOrder.getgUid());
                        }

                        List<UserExtInfo> userExtInfos = userExtInfoMapper.selectByExample(userExtInfoExample);
                        if (ValidCheck.validList(userExtInfos)) {
                            userOrder.setName("用户不存在");
                        } else {
                            userOrder.setName(userExtInfos.get(0).getCellphone());
                            if (transaction.getIntegral() > 0) {
                                userOrder.setDescription("积分获赠" + integralTransferOrder.getIntegral() + "分");
                            } else {
                                userOrder.setDescription("积分转出" + integralTransferOrder.getIntegral() + "分");
                            }
                        }
                        userOrders.add(userOrder);
                    }

                }
            }


//            IntegralTransferOrderExample integralTransferOrderExample = new IntegralTransferOrderExample();
//            IntegralTransferOrderExample.Criteria criteria = integralTransferOrderExample.or();
//            criteria.andUidEqualTo(Integer.parseInt(uid));
//            criteria.andGUidEqualTo(Integer.parseInt(uid));
//            if(!StringUtils.isEmpty(start_time) && !StringUtils.isEmpty(end_time)){
//                criteria.andCreateTimeBetween(new Date(Long.parseLong(start_time)), new Date(Long.parseLong(end_time) + 86400000));
//            }
//
//            integralTransferOrderExample.setOrderByClause("id desc");
//
//            PageHelper.startPage(page,per_page);
//            List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderMapper.selectByExample(integralTransferOrderExample);
//            PageInfo pageInfo = new PageInfo(integralTransferOrders);
//
//            if(!ValidCheck.validList(integralTransferOrders)) {
//                for (IntegralTransferOrder integralTransferOrder : integralTransferOrders) {
//                    System.out.println("aa");
//                    UserExtInfoExample userExtInfoExample = new UserExtInfoExample();
//                    UserExtInfoExample.Criteria criteria1 = userExtInfoExample.or();
//                    if(uid.equals(integralTransferOrder.getUid()+"")) {
//                        criteria1.andUidEqualTo(integralTransferOrder.getgUid());
//                    }else {
//                        criteria1.andUidEqualTo(integralTransferOrder.getUid());
//                    }
//
//                    List<UserExtInfo> userExtInfos = userExtInfoMapper.selectByExample(userExtInfoExample);
//                    if(ValidCheck.validList(userExtInfos)){
//                        integralTransferOrder.setName("用户不存在");
//                    }else {
//                        integralTransferOrder.setName(userExtInfos.get(0).getCellphone());
//                        if(uid.equals(integralTransferOrder.getUid()+"")) {
//                            integralTransferOrder.setDescription("转账" + integralTransferOrder.getIntegral() + "分");
//                        }else {
//                            integralTransferOrder.setDescription("接受转账" + integralTransferOrder.getIntegral() + "分");
//                        }
//
//                    }
//
//                    integralTransferOrder.setTrxType(3);
//
//                }
//            }

            Map map = new HashMap();
            map.put("data", userOrders);
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
    public JsonResult selectIntegralOrders(Integer page, Integer per_page, String trxCode, Integer status, Date begin, Date end, JsonResult jsonResult) {

        try {
            IntegralTransferOrderExample integralTransferOrderExample = new IntegralTransferOrderExample();
            IntegralTransferOrderExample.Criteria criteria1 = integralTransferOrderExample.or();
            if(!StringUtils.isEmpty(trxCode)){
                criteria1.andTrxCodeEqualTo(trxCode);
            }
            if(!StringUtils.isEmpty(status)){
                criteria1.andStatusEqualTo(status);
            }
            criteria1.andCreateTimeBetween(begin, end);
            integralTransferOrderExample.setOrderByClause("id desc");

            PageHelper.startPage(page,per_page);
            List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderMapper.selectByExample(integralTransferOrderExample);
            PageInfo pageInfo = new PageInfo(integralTransferOrders);

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
            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

            CommonResult.success(jsonResult);
            jsonResult.setItem(map);
        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }


}
