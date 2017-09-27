package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.IntegralTransferOrderMapper;
import com.gljr.jifen.dao.TransactionMapper;
import com.gljr.jifen.dao.UserCreditsMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.IntegralTransferOrderService;
import com.gljr.jifen.service.MessageService;
import com.gljr.jifen.service.TransactionService;
import com.gljr.jifen.service.UserCreditsService;
import com.gljr.jifen.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class IntegralTransferOrderServiceImpl implements IntegralTransferOrderService {

    @Autowired
    private IntegralTransferOrderMapper integralTransferOrderMapper;

    @Autowired
    private MessageService messageService;


    @Autowired
    private UserCreditsService userCreditsService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    @Override
    public int insertIntegralOrder(IntegralTransferOrder integralTransferOrder, Transaction transaction, UserCredits userCredits, Message message)  {

        //添加订单
        integralTransferOrderMapper.insert(integralTransferOrder);

        //添加通用交易
        transactionService.insertTransaction(transaction);

        //修改用户积分
        userCreditsService.updateUserCreditsById(userCredits);

        //把通用交易信息id更新到积分转增表
        integralTransferOrder.setTrxId(transaction.getId());

        updateIntegralOrder(integralTransferOrder);

        //添加消息
        messageService.insertMessage(message);


        return 0;
    }

    @Override
    public int updateIntegralOrder(IntegralTransferOrder integralTransferOrder) {
        return integralTransferOrderMapper.updateByPrimaryKey(integralTransferOrder);
    }

    @Override
    public List<IntegralTransferOrder> selectIntegralOrderByuid(int uid, int sort, String start_time, String end_time) {
        IntegralTransferOrderExample integralTransferOrderExample = new IntegralTransferOrderExample();
        IntegralTransferOrderExample.Criteria criteria = integralTransferOrderExample.or();
        criteria.andUidEqualTo(uid);
        integralTransferOrderExample.setOrderByClause("id desc");
        return integralTransferOrderMapper.selectByExample(integralTransferOrderExample);
    }

    @Override
    public List<IntegralTransferOrder> selectIntegralOrders() {
        IntegralTransferOrderExample integralTransferOrderExample = new IntegralTransferOrderExample();
        integralTransferOrderExample.setOrderByClause("id desc");
        return integralTransferOrderMapper.selectByExample(integralTransferOrderExample);
    }


}
