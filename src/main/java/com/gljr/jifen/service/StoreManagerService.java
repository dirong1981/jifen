package com.gljr.jifen.service;


import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.Admin;

public interface StoreManagerService {

    Integer selectStoreId(String uid);

    /**
     * 获取商户信息
     * @param jsonResult
     * @return
     */
    JsonResult getStoreInfo(String aid, JsonResult jsonResult);

    /**
     * 查询该商户下的商品
     * @param aid
     * @param jsonResult
     * @return
     */
    JsonResult selectAllProduct(String aid, JsonResult jsonResult);

    /**
     * 商户登录
     * @param admin
     * @param jsonResult
     * @return
     */
    JsonResult login(Admin admin, JsonResult jsonResult);

    /**
     * 查询该用户线上订单
     * @param aid
     * @param jsonResult
     * @return
     */
    JsonResult selectOnlineOrders(String aid, JsonResult jsonResult);

}
