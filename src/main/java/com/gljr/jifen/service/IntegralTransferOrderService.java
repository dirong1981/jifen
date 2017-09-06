package com.gljr.jifen.service;

import com.gljr.jifen.pojo.IntegralTransferOrder;
import com.gljr.jifen.pojo.IntegralTransferOrderSearch;

import java.text.ParseException;
import java.util.List;

public interface IntegralTransferOrderService {
    //查询所有父类型
    List<IntegralTransferOrder> selectParentClass();

    //插入一个分类
    int insertClass(IntegralTransferOrder integralTransferOrder);

    //查询所有子类型
    List<IntegralTransferOrder> selectSonClass();

    //删除一个分类
    int deleteClass(Integer id);

    //查询所有分类
    List<IntegralTransferOrder> selectAllClass();

    //更新一个分类状态
    int updateClass(IntegralTransferOrder integralTransferOrder);

    //查找一个分类
    IntegralTransferOrder selectClass(Integer id);

    //更新一个分类的排序
    int updateClassSort(String sort, String id);


    List<IntegralTransferOrder> selectAllParamTimeClass(IntegralTransferOrderSearch integralTransferOrderSearch) throws ParseException;

    List<IntegralTransferOrder> selectAllParamStatuClass(Byte status);
}
