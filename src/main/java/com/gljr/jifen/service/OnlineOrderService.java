package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.*;
import com.qiniu.util.Json;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface OnlineOrderService {


    /**
     * 查询用户所有订单信息
     * @param uid 用户id
     * @param sort 排序
     * @param start_time 开始时间
     * @param end_time 结束时间
     * @param jsonResult
     * @return
     */
    JsonResult selectOrdersByUid(String uid, Integer page, Integer per_page, Integer sort, String start_time, String end_time, JsonResult jsonResult);

    /**
     * 查询用户所有在线订单信息
     * @param uid 用户id
     * @param sort 排序
     * @param start_time 开始时间
     * @param end_time 结束时间
     * @param jsonResult
     * @return
     */
    JsonResult selectOnlineOrdersByUid(String uid, Integer page, Integer per_page, Integer sort, String start_time, String end_time, JsonResult jsonResult);

    /**
     * 查询用户未付款在线订单
     * @param uid 用户id
     * @param sort 排序
     * @param start_time 开始时间
     * @param end_time 结束时间
     * @param jsonResult
     * @return
     */
    JsonResult selectOnlineOrdersByUidNotPay(String uid, Integer page, Integer per_page, Integer sort, String start_time, String end_time, JsonResult jsonResult);


    /**
     * 添加一个在线订单
     * @param onlineOrder 订单信息
     * @param uid 用户id
     * @param jsonResult
     * @return
     */
    JsonResult insertOnlineOrder(OnlineOrder onlineOrder, String uid, JsonResult jsonResult);


    /**
     * 更新订单和通用交易状态为已付款，减去用户积分
     * @param trxCode 交易单号
     * @param uid 用户id
     * @param jsonResult
     * @return
     */
    JsonResult updateOnlineOrderByTrxCode(String trxCode, String uid, JsonResult jsonResult);


    /**
     * 查询所有在线订单，不包含删除
     * @return
     */
    JsonResult selectOnlineOrders(Integer page, Integer per_page, String trxCode, Integer status, Date begin, Date end, JsonResult jsonResult);


    /**
     * 取消一个订单
     * @param trxCode 订单号
     * @param uid 用户id
     * @param jsonResult
     * @return
     */
    JsonResult cancelOnlineOrderByTrxCode(String trxCode, String uid, JsonResult jsonResult);

    /**
     * 添加一个在线虚拟订单
     * @param onlineOrder 订单信息
     * @param uid 用户id
     * @param jsonResult
     * @return
     */
    JsonResult insetVirtualProductOnlineOrder(OnlineOrder onlineOrder, String uid, JsonResult jsonResult);
    JsonResult updateVirtualProductOnlineOrderByTrxCode(String trxCode, String uid, JsonResult jsonResult);
    JsonResult cancelVirtualProductOnlineOrderByTrxCode(String trxCode, String uid, JsonResult jsonResult);

    JsonResult selectOnlineOrderById(Integer id);

}
