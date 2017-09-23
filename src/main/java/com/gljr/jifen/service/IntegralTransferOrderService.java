package com.gljr.jifen.service;

import com.gljr.jifen.pojo.IntegralTransferOrder;
import com.gljr.jifen.pojo.Message;
import com.gljr.jifen.pojo.Transaction;
import com.gljr.jifen.pojo.UserCredits;

import java.util.List;

public interface IntegralTransferOrderService {


    /**
     * 添加一个积分转增订单
     * @param integralTransferOrder
     * @return
     */
    int insertIntegralOrder(IntegralTransferOrder integralTransferOrder, Transaction transaction, UserCredits userCredits, Message message);


    /**
     * 修改一个积分转增订单
     * @param integralTransferOrder
     * @return
     */
    int updateIntegralOrder(IntegralTransferOrder integralTransferOrder);


    /**
     * 查询该用户所有订单
     * @param uid
     * @return
     */
    List<IntegralTransferOrder> selectIntegralOrderByuid(int uid);



}
