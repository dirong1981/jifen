package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.*;
import com.qiniu.util.Json;

import java.text.ParseException;
import java.util.List;

public interface StoreOfflineOrderService {

    /**
     * 添加一个线下订单
     * @param storeOfflineOrder
     * @return
     */
    JsonResult insertOfflineOrder(StoreOfflineOrder storeOfflineOrder, String uid, JsonResult jsonResult);



    /**
     * 查询某个用户的线下订单
     * @return
     */
    JsonResult selectOfflineOrderByUid(String uid, int sort, String start_time, String end_time, JsonResult jsonResult);


    /**
     * 查询所有线下订单
     * @return
     */
    JsonResult selectOfflineOrders(JsonResult jsonResult);


}
