package com.gljr.jifen.service;

import com.gljr.jifen.pojo.*;

import java.text.ParseException;
import java.util.List;

public interface OnlineOrderService {


    /**
     * 查询所有在线订单
     * @param uid 用户id，如果为0，查询所有订单，不为0，查询该用户订单
     * @return 订单列表
     */
    List<OnlineOrder> selectOnlineOrdersByUid(Integer uid);


    /**
     * 添加一个在线订单
     * @param onlineOrder
     * @return
     */
    int insertOnlineOrder(OnlineOrder onlineOrder);


    /**
     * 修改一个在店订单
     * @param onlineOrder
     * @return
     */
    int updateOnlineOrderById(OnlineOrder onlineOrder);








//    /**
//     * 查询所有父类型
//     * @return OnlineOrder对象
//     */
//    List<OnlineOrder> selectParentClass();
//
//
//    /**
//     * 插入一个线上商品订单
//     * @param onlineOrder 订单详情
//     * @return
//     */
//    int insertClass(OnlineOrder onlineOrder);
//
//    /**
//     * 查询所有子类型
//     * @return
//     */
//    List<OnlineOrder> selectSonClass();
//
//    /**
//     * 删除一个线上订单
//     * @param id
//     * @return
//     */
//    int deleteClass(Integer id);
//
//    /**
//     * 查询所有线上订单
//     * @return
//     */
//    //
//
//
//    /**
//     * 查询指定状态
//     * @param status
//     * @return
//     */
//    List<OnlineOrder> selectAllParamStatuClass(Byte status);
//
//    /**
//     * 查询指定时间
//     * @param
//     * @return
//     */
//    List<OnlineOrder> selectAllParamTimeClass(OnlineOrderSearch onlineOrderSearch) throws ParseException;
//
//
//
//    /**
//     * 更新一个线上订单状态
//     * @param onlineOrder
//     * @return
//     */
//    int updateClass(OnlineOrder onlineOrder);
//
//
//    /**
//     * 查找一个线上订单
//     * @param id
//     * @return
//     */
//    OnlineOrder selectClass(Integer id);
//
//    /**
//     * 更新一个线上订单的排序
//     * @param sort
//     * @param id
//     * @return
//     */
//    int updateClassSort(String sort, String id);
//
//
////    /**
////     * @param id
////     * @return
////     */
////    List<Admin> getAid(String id);
//
////    Product getProduct(String id);
//
////    List<Admin> fuzzySearch(String condition);
//
//
//
//    /**
//     * 查找一个线上商品
//     * @param id
//     * @return
//     */
//    Product selectProductClass(int id);
//
//    /**
//     * 插入一个积分转增订单
//     * @param integralTransferOrder 积分转增详情
//     * @return
//     */
//    int insertIntegralTransferOrderClass(IntegralTransferOrder integralTransferOrder);
//
//    void deleteIntegralTransferOrderClass(Integer id);
//
//    IntegralTransferOrder selectIntegralTransferOrderClass(Integer id);
//
//    void updateIntegralTransferOrderClass(IntegralTransferOrder integralTransferOrder);
//
//    Transaction insertTransactionClass(Transaction transaction);


}
