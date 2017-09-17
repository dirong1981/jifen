package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.CategoryMapper;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.pojo.CategoryExample;
import com.gljr.jifen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> selectParentClass() {

        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria= categoryExample.createCriteria();

        criteria.andTypeEqualTo(new Byte("1"));
        criteria.andParentCodeEqualTo(0);
        categoryExample.setOrderByClause("sort asc");

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public List<Category> selectStoreParentClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria= categoryExample.createCriteria();

        criteria.andTypeEqualTo(new Byte("2"));
        criteria.andParentCodeEqualTo(0);
        categoryExample.setOrderByClause("sort asc");

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public int insertClass(Category category) {

        return  categoryMapper.insert(category);
    }

    @Override
    public List<Category> selectSonClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andTypeEqualTo(new Byte("1"));
        criteria.andParentCodeNotEqualTo(0);
        categoryExample.setOrderByClause("sort desc");

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public List<Category> selectStoreSonClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andTypeEqualTo(new Byte("2"));
        criteria.andParentCodeNotEqualTo(0);
        categoryExample.setOrderByClause("sort desc");

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public int deleteClass(int id) {
        return categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteSonClass(int parentCode) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andParentCodeEqualTo(parentCode);

        return categoryMapper.deleteByExample(categoryExample);
    }

    @Override
    public List<Category> selectAllClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andTypeEqualTo(new Byte("1"));
        categoryExample.setOrderByClause("sort asc");

        return categoryMapper.selectByExample(categoryExample);
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
    public int updateClassSort(int sort, int id) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andIdEqualTo(id);

        Category category = new Category();
        category.setSort(sort);

        return categoryMapper.updateByExampleSelective(category, categoryExample);
    }

    @Override
    public List<Category> selectShowParentClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andParentCodeEqualTo(0);
        criteria.andStatusEqualTo(new Byte("1"));
        criteria.andTypeEqualTo(new Byte("1"));

        categoryExample.setOrderByClause("sort asc");
        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public List<Category> selectShowSonClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andParentCodeNotEqualTo(0);
        criteria.andStatusEqualTo(new Byte("1"));
        criteria.andTypeEqualTo(new Byte("1"));

        categoryExample.setOrderByClause("sort asc");
        return categoryMapper.selectByExample(categoryExample);
    }


    @Override
    public List<Category> selectShowStoreParentClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andParentCodeEqualTo(0);
        criteria.andStatusEqualTo(new Byte("1"));
        criteria.andTypeEqualTo(new Byte("2"));

        categoryExample.setOrderByClause("sort asc");
        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public List<Category> selectShowStoreSonClass() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andParentCodeNotEqualTo(0);
        criteria.andStatusEqualTo(new Byte("1"));
        criteria.andTypeEqualTo(new Byte("2"));

        categoryExample.setOrderByClause("sort asc");
        return categoryMapper.selectByExample(categoryExample);
    }

}
