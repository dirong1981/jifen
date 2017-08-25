package com.gljr.jifen.service;

import com.gljr.jifen.pojo.Category;

import java.util.List;

public interface CategoryService {

    //查询所有父类型
    List<Category> selectParentClass();

    //插入一个分类
    int insertClass(Category category);

    //查询所有子类型
    List<Category> selectSonClass();

    //删除一个分类
    int deleteClass(Integer id);

}
