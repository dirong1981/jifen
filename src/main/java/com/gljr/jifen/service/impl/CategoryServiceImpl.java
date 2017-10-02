package com.gljr.jifen.service.impl;

import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.dao.CategoryMapper;
import com.gljr.jifen.dao.ProductMapper;
import com.gljr.jifen.dao.StoreInfoMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.CategoryService;
import com.gljr.jifen.service.SerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private SerialNumberService serialNumberService;

    @Override
    public List<Category> selectParentClass() {

        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria= categoryExample.createCriteria();

        criteria.andTypeEqualTo(DBConstants.CategoryType.PRODUCT.getCode());
        criteria.andParentCodeEqualTo(0);
        categoryExample.setOrderByClause("sort asc");

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public List<Category> selectStoreParentClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria= categoryExample.createCriteria();

        criteria.andTypeEqualTo(DBConstants.CategoryType.STORE.getCode());
        criteria.andParentCodeEqualTo(0);
        categoryExample.setOrderByClause("sort asc");

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public int insertClass(Category category) {
        category.setCode(this.serialNumberService.getNextCategoryCode(category));
        category.setStatus(DBConstants.CategoryStatus.INACTIVE.getCode());
        category.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return  categoryMapper.insert(category);
    }

    @Override
    public List<Category> selectSonClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andTypeEqualTo(DBConstants.CategoryType.PRODUCT.getCode());
        criteria.andParentCodeNotEqualTo(0);
        categoryExample.setOrderByClause("sort desc");

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public List<Category> selectStoreSonClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andTypeEqualTo(DBConstants.CategoryType.STORE.getCode());
        criteria.andParentCodeNotEqualTo(0);
        categoryExample.setOrderByClause("sort desc");

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    @Transactional
    public int deleteClass(int code) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.or();
        criteria.andCodeEqualTo(code);
        categoryMapper.deleteByExample(categoryExample);

        categoryExample = new CategoryExample();
        criteria = categoryExample.or();
        criteria.andParentCodeEqualTo(code);
        categoryMapper.deleteByExample(categoryExample);
        return 0;
    }



    @Override
    public int updateClass(Category category) {
        return categoryMapper.updateByPrimaryKey(category);
    }

    @Override
    public Category selectClass(int id) {
        return categoryMapper.selectByPrimaryKey(id);
    }



    @Override
    public List<Category> selectShowParentClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andParentCodeEqualTo(0);
        criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
        criteria.andTypeEqualTo(DBConstants.CategoryType.PRODUCT.getCode());

        categoryExample.setOrderByClause("sort asc");
        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public List<Category> selectShowSonClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andParentCodeNotEqualTo(0);
        criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
        criteria.andTypeEqualTo(DBConstants.CategoryType.PRODUCT.getCode());

        categoryExample.setOrderByClause("sort asc");
        return categoryMapper.selectByExample(categoryExample);
    }


    @Override
    public List<Category> selectShowStoreParentClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andParentCodeEqualTo(0);
        criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
        criteria.andTypeEqualTo(DBConstants.CategoryType.STORE.getCode());

        categoryExample.setOrderByClause("sort asc");
        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public List<Category> selectShowStoreSonClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andParentCodeNotEqualTo(0);
        criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
        criteria.andTypeEqualTo(DBConstants.CategoryType.STORE.getCode());

        categoryExample.setOrderByClause("sort asc");
        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public List<Category> selectShowSonClass(Integer parentcode) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.or();
        criteria.andParentCodeEqualTo(parentcode);
        criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public Long selectProductCountByCode(Integer code) {
        ProductExample productExample = new ProductExample();
        ProductExample.Criteria criteria = productExample.or();
        criteria.andCategoryCodeEqualTo(code);

        return productMapper.countByExample(productExample);
    }

    @Override
    public Long selectStoreCountByCode(Integer code) {
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria = storeInfoExample.or();
        criteria.andCategoryCodeEqualTo(code);

        return storeInfoMapper.countByExample(storeInfoExample);
    }

    @Override
    public List<Category> selectAllShowParentCategory() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.or();
        criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
        criteria.andParentCodeEqualTo(0);
        categoryExample.setOrderByClause("sort asc");
        categoryExample.setOrderByClause("id desc");
        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public List<Category> selectAllShowSonCategory() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.or();
        criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
        criteria.andParentCodeNotEqualTo(0);
        categoryExample.setOrderByClause("sort asc");
        categoryExample.setOrderByClause("id desc");
        return categoryMapper.selectByExample(categoryExample);
    }

}
