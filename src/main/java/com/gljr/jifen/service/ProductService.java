package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.ProductPhoto;
import com.qiniu.util.Json;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    /**
     * 查找素有商品，包括未审核，但不显示删除商品
     * @return
     */
    JsonResult selectAllProduct(JsonResult jsonResult);

    /**
     * 下架
     * @param productId
     * @param jsonResult
     * @return
     */
    JsonResult stopProductById(Integer productId, JsonResult jsonResult);

    /**
     * 上架
     * @param productId
     * @param jsonResult
     * @return
     */
    JsonResult startProductById(Integer productId, JsonResult jsonResult);

    /**
     * 删除商品
     * @param productId
     * @param jsonResult
     * @return
     */
    JsonResult deleteProductById(Integer productId, JsonResult jsonResult);

    /**
     * 添加商品
     * @param product
     * @param file
     * @param random
     * @param jsonResult
     * @return
     */
    JsonResult insertProduct(Product product, MultipartFile file, Integer random, JsonResult jsonResult);


    /**
     * 上传图片
     * @param file
     * @param random
     * @param jsonResult
     * @return
     */
    JsonResult uploadFile(MultipartFile file, Integer random, JsonResult jsonResult);


    /**
     * 通过id查询一个商品
     * @param productId
     * @param jsonResult
     * @return
     */
    JsonResult selectProductById(Integer productId, JsonResult jsonResult);


    /**
     * 通过关键字查询商品
     * @param keyword
     * @param page
     * @param per_page
     * @param sort
     * @param jsonResult
     * @return
     */
    JsonResult selectProductByKeyword(String keyword, Integer page, Integer per_page, Integer sort, JsonResult jsonResult);


    /**
     * 通过分类code查询商品
     * @param code
     * @param jsonResult
     * @return
     */
    JsonResult selectProductByCode(Integer code, Integer page, Integer per_page, Integer sort, JsonResult jsonResult);
}
