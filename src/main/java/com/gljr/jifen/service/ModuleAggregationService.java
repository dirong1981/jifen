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

    int insertModuleAggregationProduct(Integer id, String[] productIds, Integer type);




    //查找

    /**
     * 查询所有聚合页
     * @return
     */
    JsonResult selectModuleAggregations(JsonResult jsonResult);


    /**
     * 根据id查找一个聚合页
     * @param id
     * @return
     */
    ModuleAggregation selectModuleAggregationById(Integer id);


    /**
     * 根据聚合页id查找页面下包含的内容
     * @param aid
     * @return
     */
    List<ModuleAggregationProduct> selectModuleAggregationProductByAggregationId(Integer aid);


    /**
     * 按照上下架类型查询聚合页
     * @return
     */
    JsonResult selectModuleAggregationByEnabled(JsonResult jsonResult);

    //更新

    /**
     * 通过id更新一个聚合页
     * @param moduleAggregation
     * @return
     */
    int updateModuleAggregationById(ModuleAggregation moduleAggregation);


}
