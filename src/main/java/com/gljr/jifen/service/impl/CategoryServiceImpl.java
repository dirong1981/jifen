package com.gljr.jifen.service.impl;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.CategoryMapper;
import com.gljr.jifen.dao.ProductMapper;
import com.gljr.jifen.dao.StoreInfoMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.CategoryService;
import com.gljr.jifen.service.SerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl extends BaseService implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    @Transactional
    public JsonResult insertCategory(Category category) {

        try {

            categoryMapper.insert(category);

            category.setSort(category.getId());

            categoryMapper.updateByPrimaryKey(category);

            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }

        return  jsonResult;
    }


    @Override
    public JsonResult selectCategoryById(Integer id) {
        try {
            Category category = categoryMapper.selectByPrimaryKey(id);
            Map map = new HashMap();
            map.put("data", category);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectCategories() {
        try {
            CategoryExample categoryExample = new CategoryExample();
            CategoryExample.Criteria criteria = categoryExample.or();
            criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
            criteria.andParentCodeEqualTo(0);
            categoryExample.setOrderByClause("sort asc");

            List<Category> categories = categoryMapper.selectByExample(categoryExample);

            for (Category category : categories){
                category.setLogoKey(category.getLogoKey() + "!popular");
            }

            Map map = new HashMap();
            map.put("parents", categories);


            categoryExample = new CategoryExample();
            criteria = categoryExample.or();
            criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
            criteria.andParentCodeNotEqualTo(0);
            categoryExample.setOrderByClause("sort asc");

            categories = categoryMapper.selectByExample(categoryExample);

            for (Category category : categories){
                category.setLogoKey(category.getLogoKey() + "!popular");
            }

            map.put("sons", categories);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult changeCategoryOrder(Integer cur, Integer prev, JsonResult jsonResult) {

        try {
            Category category = categoryMapper.selectByPrimaryKey(cur);

            Category category1 = categoryMapper.selectByPrimaryKey(prev);

            cur = category.getSort();

            prev = category1.getSort();

            category.setSort(prev);
            category1.setSort(cur);

            categoryMapper.updateByPrimaryKey(category);
            categoryMapper.updateByPrimaryKey(category1);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectProductCategories() {
        try {
            Map map = new HashMap();

            CategoryExample categoryExample = new CategoryExample();
            CategoryExample.Criteria criteria = categoryExample.or();
            criteria.andParentCodeEqualTo(0);
            criteria.andTypeEqualTo(DBConstants.CategoryType.PRODUCT.getCode());
            criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
            categoryExample.setOrderByClause("sort asc");
            List<Category> categories = categoryMapper.selectByExample(categoryExample);

            map.put("parents", categories);

            criteria = categoryExample.or();
            criteria.andParentCodeNotEqualTo(0);
            criteria.andTypeEqualTo(DBConstants.CategoryType.PRODUCT.getCode());
            criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
            categoryExample.setOrderByClause("sort asc");
            categories = categoryMapper.selectByExample(categoryExample);

            map.put("sons", categories);
            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectStorecategories() {
        try {
            Map map = new HashMap();

            CategoryExample categoryExample = new CategoryExample();
            CategoryExample.Criteria criteria = categoryExample.or();
            criteria.andParentCodeEqualTo(0);
            criteria.andTypeEqualTo(DBConstants.CategoryType.STORE.getCode());
            criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
            categoryExample.setOrderByClause("sort asc");
            List<Category> categories = categoryMapper.selectByExample(categoryExample);

            map.put("parents", categories);

            categoryExample = new CategoryExample();
            criteria = categoryExample.or();
            criteria.andParentCodeNotEqualTo(0);
            criteria.andTypeEqualTo(DBConstants.CategoryType.STORE.getCode());
            criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
            categoryExample.setOrderByClause("sort asc");
            categories = categoryMapper.selectByExample(categoryExample);

            map.put("sons", categories);
            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }


    @Override
    public JsonResult startCategoryById(Integer id) {

        try {
            //通过id查询该分类
            Category category = categoryMapper.selectByPrimaryKey(id);

            if(ValidCheck.validPojo(category)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            category.setStatus(DBConstants.CategoryStatus.ACTIVED.getCode());
            categoryMapper.updateByPrimaryKey(category);

            CommonResult.success(jsonResult);
        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult stopCategoryById(Integer id) {

        try {
            //通过id查询该分类
            Category category = categoryMapper.selectByPrimaryKey(id);

            if(ValidCheck.validPojo(category)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            if(category.getParentCode() == 0){
                CategoryExample categoryExample = new CategoryExample();
                CategoryExample.Criteria criteria = categoryExample.or();
                criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
                criteria.andParentCodeEqualTo(category.getCode());

                List<Category> categories = categoryMapper.selectByExample(categoryExample);

                if(!ValidCheck.validList(categories)){
                    jsonResult.setMessage("请先停用该分类下的子类！");
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                    return jsonResult;
                }
            }

            category.setStatus(DBConstants.CategoryStatus.INACTIVE.getCode());
            categoryMapper.updateByPrimaryKey(category);

            CommonResult.success(jsonResult);
        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }


    @Override
    @Transactional
    public JsonResult deleteCategoryById(int id) {

        try {

            //通过id查询该分类
            Category category = categoryMapper.selectByPrimaryKey(id);
            if(ValidCheck.validPojo(category)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            category.setStatus(-1);
            categoryMapper.updateByPrimaryKey(category);

            //删除子分类
            CategoryExample categoryExample = new CategoryExample();
            CategoryExample.Criteria criteria = categoryExample.or();
            criteria.andParentCodeEqualTo(category.getCode());

            List<Category> categories = categoryMapper.selectByExample(categoryExample);

            for(Category category1 : categories){
                category1.setStatus(-1);
                categoryMapper.updateByPrimaryKey(category1);
            }

            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }


        return jsonResult;
    }

    @Override
    public JsonResult selectAllProductCategories() {

        try{
            CategoryExample categoryExample = new CategoryExample();
            CategoryExample.Criteria criteria = categoryExample.or();
            criteria.andParentCodeEqualTo(0);
            criteria.andStatusNotEqualTo(-1);
            criteria.andTypeEqualTo(DBConstants.CategoryType.PRODUCT.getCode());
            categoryExample.setOrderByClause("sort desc");

            List<Category> categories = categoryMapper.selectByExample(categoryExample);

            Map map = new HashMap();
            map.put("parents", categories);

            categoryExample = new CategoryExample();
            criteria = categoryExample.or();
            criteria.andParentCodeNotEqualTo(0);
            criteria.andStatusNotEqualTo(-1);
            criteria.andTypeEqualTo(DBConstants.CategoryType.PRODUCT.getCode());
            categoryExample.setOrderByClause("sort desc");

            categories = categoryMapper.selectByExample(categoryExample);
            map.put("sons", categories);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    @Override
    public JsonResult selectAllStoreCategories() {
        try{
            CategoryExample categoryExample = new CategoryExample();
            CategoryExample.Criteria criteria = categoryExample.or();
            criteria.andParentCodeEqualTo(0);
            criteria.andStatusNotEqualTo(-1);
            criteria.andTypeEqualTo(DBConstants.CategoryType.STORE.getCode());
            categoryExample.setOrderByClause("sort desc");

            List<Category> categories = categoryMapper.selectByExample(categoryExample);

            Map map = new HashMap();
            map.put("parents", categories);

            categoryExample = new CategoryExample();
            criteria = categoryExample.or();
            criteria.andParentCodeNotEqualTo(0);
            criteria.andStatusNotEqualTo(-1);
            criteria.andTypeEqualTo(DBConstants.CategoryType.STORE.getCode());
            categoryExample.setOrderByClause("sort desc");

            categories = categoryMapper.selectByExample(categoryExample);
            map.put("sons", categories);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

}
