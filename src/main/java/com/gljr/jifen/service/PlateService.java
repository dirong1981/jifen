package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;

public interface PlateService {

    /**
     * 所有首页模块
     * @param jsonResult
     * @return
     */
    JsonResult selectPlates(JsonResult jsonResult);

    /**
     * 生成首页
     * @param jsonResult
     * @return
     */
    JsonResult generatePlates(JsonResult jsonResult);

    JsonResult changePlateOrder(Integer cur, Integer prev, JsonResult jsonResult);

}
