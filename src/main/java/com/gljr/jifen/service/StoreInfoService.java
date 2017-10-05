package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.Admin;
import com.gljr.jifen.pojo.StoreExtInfo;
import com.gljr.jifen.pojo.StoreInfo;
import com.gljr.jifen.pojo.StorePhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreInfoService
{

    /**
     * 查询所有商户
     * @param jsonResult
     * @return
     */
    JsonResult selectAllStoreInfo(JsonResult jsonResult);

    /**
     * 通过审核
     * @param storeId
     * @param jsonResult
     * @return
     */
    JsonResult startStoreInfo(Integer storeId, JsonResult jsonResult);


    /**
     * 取消审核
     * @param storeId
     * @param jsonResult
     * @return
     */
    JsonResult stopStoreInfo(Integer storeId, JsonResult jsonResult);


    /**
     * 删除商户
     * @param storeId
     * @param jsonResult
     * @return
     */
    JsonResult deleteStoreInfo(Integer storeId, JsonResult jsonResult);

    /**
     * 添加商户
     * @param storeInfo
     * @param file
     * @param random
     * @param jsonResult
     * @return
     */
    JsonResult insertStoreInfo(StoreInfo storeInfo, MultipartFile file, String username, Integer random, JsonResult jsonResult);

    /**
     * 上传图片
     * @param random
     * @param file
     * @param jsonResult
     * @return
     */
    JsonResult uploadFile(Integer random, MultipartFile file, JsonResult jsonResult);


    /**
     * 按照id查找商户
     * @param storeId
     * @param jsonResult
     * @return
     */
    JsonResult selectStoreInfoById(Integer storeId, JsonResult jsonResult);


    /**
     * 按照关键字查询商户
     * @param keyword
     * @param page
     * @param per_page
     * @param sort
     * @param jsonResult
     * @return
     */
    JsonResult selectStoreInfoByKeyword(String keyword, Integer page, Integer per_page, Integer sort, JsonResult jsonResult);


    JsonResult selectStoreInfoByCode(Integer code, Integer page, Integer per_page, Integer sort, JsonResult jsonResult);

    StoreExtInfo selectStoreExtInfoBySiId(Integer siId);

    List<StoreInfo> selectStoreInfoByAid(Integer id);

    List<StorePhoto> selectStorePhotoById(Integer id);
}
