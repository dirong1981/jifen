package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.pojo.CategoryExample;
import com.qiniu.util.Json;

import javax.xml.bind.util.JAXBSource;
import java.util.List;

public interface CategoryService {

    /**
     * 通过id查询一个分类
     * @param id
     * @return
     */
    JsonResult selectCategoryById(Integer id);

    /**
     * 所有分类包括商品和商户
     * @return
     */
    JsonResult selectCategories();

    /**
     * 更改分类顺序
     * @param cur
     * @param prev
     * @param jsonResult
     * @return
     */
    JsonResult changeCategoryOrder(Integer cur, Integer prev, JsonResult jsonResult);

    /**
     * 查找通过审核商品分类
     * @return
     */
    JsonResult selectProductCategories();

    /**
     * 查询通过审核的商户分类
     * @return
     */
    JsonResult selectStorecategories();


    /**
     * 启用分类
     * @param id
     * @return
     */
    JsonResult startCategoryById(Integer id);

    /**
     * 停用分类
     * @param id
     * @return
     */
    JsonResult stopCategoryById(Integer id);

    /**
     * 添加分类
     * @param category
     * @return
     */
    JsonResult insertCategory(Category category);

    /**
     * 删除分类
     * @param id
     * @return
     */
    JsonResult deleteCategoryById(int id);

    /**
     * 查询所有商品分类
     * @return
     */
    JsonResult selectAllProductCategories();

    /**
     * 查询所有商户分类
     * @return
     */
    JsonResult selectAllStoreCategories();

}
