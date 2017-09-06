package com.gljr.jifen.service;

import com.gljr.jifen.pojo.StoreOfflineOrderSearch;
import com.gljr.jifen.pojo.IntegralTransferOrder;
import com.gljr.jifen.pojo.StoreInfo;
import com.gljr.jifen.pojo.StoreOfflineOrder;
import com.gljr.jifen.pojo.Transaction;

import java.text.ParseException;
import java.util.List;

public interface StoreOfflineOrderService {


    //查询所有父类型
    List<StoreOfflineOrder> selectParentClass();

    //插入一个分类
    int insertClass(StoreOfflineOrder storeOfflineOrder);

    //查询所有子类型
    List<StoreOfflineOrder> selectSonClass();

    //删除一个分类
    int deleteClass(Integer id);

    //查询所有分类
    List<StoreOfflineOrder> selectAllClass();

    //更新一个分类状态
    int updateClass(StoreOfflineOrder storeOfflineOrder);

    //查找一个分类
    StoreOfflineOrder selectClass(Integer id);

    //更新一个分类的排序
    int updateClassSort(String sort, String id);


    void insertIntegralTransferOrderClass(IntegralTransferOrder integralTO);

    List<StoreOfflineOrder> selectAllParamStatuClass(Byte status);

    List<StoreOfflineOrder> selectAllParamTimeClass(StoreOfflineOrderSearch storeOfflineOrderSearch) throws ParseException;

    List<StoreOfflineOrder> selectAllParamContextClass(StoreOfflineOrderSearch storeOfflineOrderSearch) throws ParseException;

    StoreInfo selectStoreInfoClass(Integer siId);

    Transaction insertTransactionClass(Transaction transaction);

    IntegralTransferOrder selectIntegralTransferOrderClass(Integer trxId);

    void updateIntegralTransferOrderClass(IntegralTransferOrder integralTransferOrder);

    void deleteIntegralTransferOrderClass(Integer id);
}
