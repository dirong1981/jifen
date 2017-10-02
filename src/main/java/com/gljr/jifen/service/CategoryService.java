package com.gljr.jifen.service;

import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.pojo.CategoryExample;

import java.util.List;

public interface CategoryService {

    //查询所有父类型
    List<Category> selectParentClass();

    //查询所有父类型
    List<Category> selectStoreParentClass();

    //插入一个分类
    int insertClass(Category category);

    //查询所有子类型
    List<Category> selectSonClass();

    //查询所有子类型
    List<Category> selectStoreSonClass();

    //删除一个分类,同时删除子分类
    int deleteClass(int code);



    //更新一个分类状态
    int updateClass(Category category);

    //查找一个分类
    Category selectClass(int id);


    //查询所有通过审核的父分类
    List<Category> selectShowParentClass();

    //查询所有通过审核的子分类
    List<Category> selectShowSonClass();


    //查询所有通过审核的父分类
    List<Category> selectShowStoreParentClass();

    //查询所有通过审核的子分类
    List<Category> selectShowStoreSonClass();

    //通过父分类code查询通过审核的子分类
    List<Category> selectShowSonClass(Integer parentcode);

    //通过code查询该分类下是否有商品
    Long selectProductCountByCode(Integer code);

    //通过code查询该分类下是否有商户
    Long selectStoreCountByCode(Integer code);

    //查询所有启用的分类，包括商品，商户
    List<Category> selectAllShowParentCategory();
    List<Category> selectAllShowSonCategory();

}
