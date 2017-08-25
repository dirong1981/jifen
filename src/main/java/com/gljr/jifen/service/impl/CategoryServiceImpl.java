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
        criteria.andCParentIdEqualTo(0);

        categoryExample.setOrderByClause("c_id asc");

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
        criteria.andCParentIdNotEqualTo(0);
        categoryExample.setOrderByClause("c_parent_id desc, c_id desc");

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public int deleteClass(Integer id) {
        return categoryMapper.deleteByPrimaryKey(id);
    }


}
