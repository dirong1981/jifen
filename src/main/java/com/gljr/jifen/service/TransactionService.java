package com.gljr.jifen.service;

import com.gljr.jifen.pojo.Transaction;

public interface TransactionService {


    /**
     * 增加通用交易信息
     * @param transaction
     * @return
     */
    int insertTransaction(Transaction transaction);

    /**
     * 通过id查找通用交易记录
     * @param id
     * @return
     */
    Transaction selectTransactionById(Integer id);

}
