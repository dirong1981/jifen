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
import java.util.Date;
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

            //判断积分是否足够
            UserCredits userCredits = this.userCreditsMapper.getUserCredits(Integer.parseInt(uid),
                    DBConstants.OwnerType.CUSTOMER.getCode());

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


            String title = "向" + userExtInfo.getCellphone() + "转账" + integralTransferOrder.getIntegral() + "分";
            //添加一个积分转增订单
            integralTransferOrder.setUid(Integer.parseInt(uid));
            integralTransferOrder.setStatus(DBConstants.OrderStatus.PAID.getCode());
            integralTransferOrder.setTitle(title);
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

                    integralTransferOrder.setTrxType(3);

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
