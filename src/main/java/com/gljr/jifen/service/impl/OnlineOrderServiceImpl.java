package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.OnlineOrderService;
import com.gljr.jifen.service.TransactionService;
import com.gljr.jifen.service.UserCreditsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class OnlineOrderServiceImpl implements OnlineOrderService {

    @Autowired
    private OnlineOrderMapper onlineOrderMapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserCreditsService userCreditsService;



    @Override
    public List<OnlineOrder> selectOnlineOrdersByUid(Integer uid, int sort, String start_time, String end_time) {
        OnlineOrderExample onlineOrderExample = new OnlineOrderExample();
        OnlineOrderExample.Criteria criteria = onlineOrderExample.or();
        if(uid != 0){
            criteria.andUidEqualTo(uid);
        }
        onlineOrderExample.setOrderByClause("id desc");
        return onlineOrderMapper.selectByExample(onlineOrderExample);
    }

    @Override
    public List<OnlineOrder> selectOnlineOrdersByUidNotPay(Integer uid, int sort, String start_time, String end_time) {
        OnlineOrderExample onlineOrderExample = new OnlineOrderExample();
        OnlineOrderExample.Criteria criteria = onlineOrderExample.or();
        if(uid != 0){
            criteria.andUidEqualTo(uid);
        }
        criteria.andStatusEqualTo((byte)0);
        onlineOrderExample.setOrderByClause("id desc");
        return onlineOrderMapper.selectByExample(onlineOrderExample);
    }


    @Transactional
    @Override
    public int insertOnlineOrder(OnlineOrder onlineOrder, Transaction transaction, UserCredits userCredits) {

        onlineOrderMapper.insert(onlineOrder);

        transactionService.insertTransaction(transaction);

        userCreditsService.updateUserCreditsById(userCredits);


        onlineOrder.setTrxId(transaction.getId());
        updateOnlineOrderById(onlineOrder);

        return 0;
    }

    @Override
    public int updateOnlineOrderById(OnlineOrder onlineOrder) {
        return onlineOrderMapper.updateByPrimaryKey(onlineOrder);
    }

    @Override
    public int deleteOnlineOrder() {
        OnlineOrderExample onlineOrderExample = new OnlineOrderExample();
        OnlineOrderExample.Criteria criteria = onlineOrderExample.or();
        criteria.andUidEqualTo(15483);
        return onlineOrderMapper.deleteByExample(onlineOrderExample);
    }

    @Override
    public List<OnlineOrder> selectOnlineOrders() {
        OnlineOrderExample onlineOrderExample = new OnlineOrderExample();
        OnlineOrderExample.Criteria criteria = onlineOrderExample.or();
        criteria.andStatusNotEqualTo((byte)2);
        onlineOrderExample.setOrderByClause("id desc");
        return onlineOrderMapper.selectByExample(onlineOrderExample);
    }

    @Override
    public int deleteOnlineOrderById(int id) {
        OnlineOrderExample onlineOrderExample = new OnlineOrderExample();
        OnlineOrderExample.Criteria criteria = onlineOrderExample.or();
        criteria.andIdEqualTo(id);

        return 0;
    }

    @Override
    public OnlineOrder selectOnlineOrderById(int id) {

        return onlineOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    public OnlineOrder selectOnlineOrderById(String id, int uid) {
        OnlineOrderExample onlineOrderExample = new OnlineOrderExample();
        OnlineOrderExample.Criteria criteria = onlineOrderExample.or();
        criteria.andTrxCodeEqualTo(id);
        criteria.andUidEqualTo(uid);
        return onlineOrderMapper.selectByExample(onlineOrderExample).get(0);
    }


}
