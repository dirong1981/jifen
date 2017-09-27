package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.IntegralTransferOrderMapper;
import com.gljr.jifen.dao.StoreInfoMapper;
import com.gljr.jifen.dao.StoreOfflineOrderMapper;
import com.gljr.jifen.dao.TransactionMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.StoreOfflineOrderService;
import com.gljr.jifen.service.TransactionService;
import com.gljr.jifen.service.UserCreditsService;
import com.gljr.jifen.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class StoreOfflineOrderServiceImpl implements StoreOfflineOrderService {


    @Autowired
    private StoreOfflineOrderMapper storeOfflineOrderMapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Transactional
    @Override
    public int insertOfflineOrder(StoreOfflineOrder storeOfflineOrder, Transaction transaction, UserCredits userCredits) {

        storeOfflineOrderMapper.insert(storeOfflineOrder);

        transactionService.insertTransaction(transaction);

        userCreditsService.updateUserCreditsById(userCredits);

        storeOfflineOrder.setTrxId(transaction.getId());
        updateOfflineOrder(storeOfflineOrder);


        return 0;
    }

    @Override
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

    @Override
    public List<StoreOfflineOrder> selectOfflineOrders() {

        StoreOfflineOrderExample storeOfflineOrderExample = new StoreOfflineOrderExample();
        storeOfflineOrderExample.setOrderByClause("id desc");
        return storeOfflineOrderMapper.selectByExample(storeOfflineOrderExample);
    }


}
