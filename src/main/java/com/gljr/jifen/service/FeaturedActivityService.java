package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.FeaturedActivity;
import org.springframework.web.multipart.MultipartFile;


public interface FeaturedActivityService {

    //查询


    /**
     * 查询所有精选内容，状态不为-1
     * @return
     */
    JsonResult selectFeaturedActivitys(JsonResult jsonResult);

    /**
     * 按照状态查询精选页
     * @return
     */
    JsonResult selectFeaturedActivitysEnabled(Integer page, Integer per_page, JsonResult jsonResult);


    //修改

    /**
     * 启用
     * @param featuredId
     * @param jsonResult
     * @return
     */
    JsonResult stopFeaturedActivity(Integer featuredId, JsonResult jsonResult);

    /**
     * 停用
     * @param featuredId
     * @param jsonResult
     * @return
     */
    JsonResult startFeaturedActivity(Integer featuredId, JsonResult jsonResult);


    //删除


    /**
     * 删除一个精选页
     * @return
     */
    JsonResult deleteFeaturedActivitysById(Integer featuredId, JsonResult jsonResult);


    //添加

    /**
     * 添加一个精选页
     * @param featuredActivity
     * @return
     */
    JsonResult insertFeaturedActivity(FeaturedActivity featuredActivity, String aid, MultipartFile file, JsonResult jsonResult);


    /**
     * 上下调换排序
     * @param cur
     * @param prev
     * @param jsonResult
     * @return
     */
    JsonResult changeFeaturedActivitysOrder(Integer cur, Integer prev, JsonResult jsonResult);
}
