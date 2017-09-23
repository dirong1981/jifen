package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.TransactionMapper;
import com.gljr.jifen.pojo.Transaction;
import com.gljr.jifen.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    @Transactional
    public int insertTransaction(Transaction transaction) throws RuntimeException  {
        return transactionMapper.insert(transaction);
    }
}
