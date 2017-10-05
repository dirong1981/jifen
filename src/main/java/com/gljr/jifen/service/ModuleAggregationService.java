package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.ModuleAggregation;
import com.gljr.jifen.pojo.ModuleAggregationProduct;
import redis.clients.jedis.Jedis;

import java.util.List;

public interface ModuleAggregationService {


    //添加

    /**
     * 插入一个聚合页
     * @param moduleAggregation
     * @return
     */
    JsonResult insertModuleAggregation(ModuleAggregation moduleAggregation, JsonResult jsonResult);

    /**
     * 添加商品进聚合页
     * @param moduleAggregationId 聚合页id
     * @param productIds 商品id集合
     * @param type 类型1商品， 2商户
     * @return
     */
    JsonResult insertModuleAggregationProduct(Integer moduleAggregationId, String[] productIds, Integer type, JsonResult jsonResult);


    //查找

    /**
     * 查询所有聚合页
     * @return
     */
    JsonResult selectModuleAggregations(JsonResult jsonResult);


    /**
     * 根据id查找一个聚合页
     * @param moduleAggregationId
     * @return
     */
    JsonResult selectModuleAggregationById(Integer moduleAggregationId, JsonResult jsonResult);


    /**
     * 查询上架聚合页
     * @return
     */
    JsonResult selectModuleAggregationByEnabled(JsonResult jsonResult);

    /**
     * 重新生成一个redis缓存
     * @param link
     * @param jsonResult
     * @return
     */
    JsonResult restartmoduleAggregationByLink(String link, JsonResult jsonResult);

    //更新

    /**
     * 下架聚合页
     * @param moduleAggregationId
     * @param jsonResult
     * @return
     */
    JsonResult rejectionModuleAggregationById(Integer moduleAggregationId, JsonResult jsonResult);

    /**
     * 上架一个聚合页，生成5总排序方式
     * @param moduleAggregationId
     * @param jsonResult
     * @return
     */
    JsonResult acceptanceModuleAggregationById(Integer moduleAggregationId, JsonResult jsonResult);


}
