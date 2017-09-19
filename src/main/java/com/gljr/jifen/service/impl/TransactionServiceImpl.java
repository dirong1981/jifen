package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.TransactionMapper;
import com.gljr.jifen.pojo.Transaction;
import com.gljr.jifen.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public int insertTransaction(Transaction transaction) {
        return transactionMapper.insert(transaction);
    }
}
