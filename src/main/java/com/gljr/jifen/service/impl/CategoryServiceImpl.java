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
        criteria.andCParentIdEqualTo("0");

        categoryExample.setOrderByClause("c_sort asc");

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
        criteria.andCParentIdNotEqualTo("0");
        categoryExample.setOrderByClause("c_sort desc");

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public int deleteClass(String id) {
        return categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Category> selectAllClass() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("c_sort asc");

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public int updateClass(Category category) {
        return categoryMapper.updateByPrimaryKey(category);
    }

    @Override
    public Category selectClass(String id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateClassSort(String sort, String id) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andCIdEqualTo(id);

        Category category = new Category();
        category.setcSort(sort);

        return categoryMapper.updateByExampleSelective(category, categoryExample);
    }


}
