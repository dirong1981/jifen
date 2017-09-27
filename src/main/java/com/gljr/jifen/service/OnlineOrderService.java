package com.gljr.jifen.service;

import com.gljr.jifen.pojo.*;

import java.text.ParseException;
import java.util.List;

public interface OnlineOrderService {


    /**
     * 查询某个用户所有在线订单
     * @param uid 用户id，如果为0，查询所有订单，不为0，查询该用户订单
     * @return 订单列表
     */
    List<OnlineOrder> selectOnlineOrdersByUid(Integer uid, int sort, String start_time, String end_time);
    List<OnlineOrder> selectOnlineOrdersByUidNotPay(Integer uid, int sort, String start_time, String end_time);



    /**
     * 添加一个在线订单
     * @param onlineOrder
     * @return
     */
    int insertOnlineOrder(OnlineOrder onlineOrder, Transaction transaction, UserCredits userCredits);


    /**
     * 修改一个在店订单
     * @param onlineOrder
     * @return
     */
    int updateOnlineOrderById(OnlineOrder onlineOrder);


    int deleteOnlineOrder();

    /**
     * 查询所有在线订单
     * @return
     */
    List<OnlineOrder> selectOnlineOrders();


    int deleteOnlineOrderById(int id);

    /**
     * 根据id查询一个在线订单
     * @param id
     * @return
     */
    OnlineOrder selectOnlineOrderById(int id);


    /**
     * 通过订单号和uid查询一个订单
     * @param id
     * @param uid
     * @return
     */
    OnlineOrder selectOnlineOrderById(String id, int uid);


}
