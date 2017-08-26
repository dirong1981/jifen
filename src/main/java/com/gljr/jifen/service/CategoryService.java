package com.gljr.jifen.service;

import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.pojo.CategoryExample;

import java.util.List;

public interface CategoryService {

    //查询所有父类型
    List<Category> selectParentClass();

    //插入一个分类
    int insertClass(Category category);

    //查询所有子类型
    List<Category> selectSonClass();

    //删除一个分类
    int deleteClass(String id);

    //查询所有分类
    List<Category> selectAllClass();

    //更新一个分类状态
    int updateClass(Category category);

    //查找一个分类
    Category selectClass(String id);

    //更新一个分类的排序
    int updateClassSort(String sort, String id);


}
