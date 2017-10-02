package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.IntegralTransferOrder;
import com.gljr.jifen.pojo.Message;
import com.gljr.jifen.pojo.Transaction;
import com.gljr.jifen.pojo.UserCredits;
import redis.clients.jedis.Jedis;

import java.util.List;

public interface IntegralTransferOrderService {


    /**
     * 添加一个积分转增订单
     * @param integralTransferOrder
     * @return
     */
    JsonResult insertIntegralOrder(IntegralTransferOrder integralTransferOrder, String uid, JsonResult jsonResult);


    /**
     * 查询该用户所有订单
     * @param uid
     * @return
     */
    JsonResult selectIntegralOrderByuid(String uid, int sort, String start_time, String end_time, JsonResult jsonResult);


    /**
     * 查询所有积分转增订单
     * @return
     */
    JsonResult selectIntegralOrders(JsonResult jsonResult);


}
