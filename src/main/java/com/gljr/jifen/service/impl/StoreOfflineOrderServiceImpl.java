package com.gljr.jifen.service.impl;

import com.gljr.jifen.pojo.StoreOfflineOrderSearch;
import com.gljr.jifen.dao.IntegralTransferOrderMapper;
import com.gljr.jifen.dao.StoreInfoMapper;
import com.gljr.jifen.dao.StoreOfflineOrderMapper;
import com.gljr.jifen.dao.TransactionMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.StoreOfflineOrderService;
import com.gljr.jifen.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class StoreOfflineOrderServiceImpl implements StoreOfflineOrderService {
    @Autowired
    private StoreOfflineOrderMapper storeOfflineOrderMapper;
    @Autowired
    private IntegralTransferOrderMapper integralTransferOrderMapper;
    @Autowired
    private StoreInfoMapper storeInfoMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Override
    public List<StoreOfflineOrder> selectParentClass() {
        return null;
    }

    @Override
    public int insertClass(StoreOfflineOrder storeOfflineOrder) {
        return storeOfflineOrderMapper.insert(storeOfflineOrder);
    }

    @Override
    public List<StoreOfflineOrder> selectSonClass() {
        return null;
    }

    @Override
    public int deleteClass(Integer id) {
        return storeOfflineOrderMapper.deleteByPrimaryKey(id);
    }



    @Override
    public List<StoreOfflineOrder> selectAllClass() {
        List<StoreOfflineOrder> storeOfflineOrders = storeOfflineOrderMapper.selectByExample(null);
        for(int i=0;i<storeOfflineOrders.size();i++){
            StoreOfflineOrder storeOfflineOrder = storeOfflineOrders.get(i);
            storeOfflineOrder.setCreateTimeText(DateUtils.getTimeStr(storeOfflineOrder.getCreateTime()));
            storeOfflineOrder.setuName("小冰");
            StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeOfflineOrder.getSiId());
            storeOfflineOrder.setoContext(storeInfo.getName());
        }
        return storeOfflineOrders;
    }

    @Override
    public int updateClass(StoreOfflineOrder storeOfflineOrder) {
        return storeOfflineOrderMapper.updateByPrimaryKey(storeOfflineOrder);
    }

    @Override
    public StoreOfflineOrder selectClass(Integer id) {
        StoreOfflineOrder storeOfflineOrder = storeOfflineOrderMapper.selectByPrimaryKey(id);
        storeOfflineOrder.setCreateTimeText(DateUtils.getTimeStr(storeOfflineOrder.getCreateTime()));
        StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeOfflineOrder.getSiId());
        storeOfflineOrder.setoContext(storeInfo.getName());
        storeOfflineOrder.setuName("小冰");
        return storeOfflineOrder;
    }

    @Override
    public int updateClassSort(String sort, String id) {
        return 0;
    }



    @Override
    public void insertIntegralTransferOrderClass(IntegralTransferOrder integralTO) {
        integralTransferOrderMapper.insert(integralTO);
    }

    @Override
    public List<StoreOfflineOrder> selectAllParamStatuClass(Byte status) {
        StoreOfflineOrderExample example = new StoreOfflineOrderExample();
        example.createCriteria().andStatusEqualTo(status);
        List<StoreOfflineOrder> storeOfflineOrders = storeOfflineOrderMapper.selectByExample(example);
        for(int i=0;i<storeOfflineOrders.size();i++){
            StoreOfflineOrder storeOfflineOrder = storeOfflineOrders.get(i);
            storeOfflineOrder.setCreateTimeText(DateUtils.getTimeStr(storeOfflineOrder.getCreateTime()));
            storeOfflineOrder.setuName("小冰");
            StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeOfflineOrder.getSiId());
            storeOfflineOrder.setoContext(storeInfo.getName());
        }
        return storeOfflineOrders;
    }

    @Override
    public List<StoreOfflineOrder> selectAllParamTimeClass(StoreOfflineOrderSearch storeOfflineOrderSearch) throws ParseException {
        StoreOfflineOrderExample example = new StoreOfflineOrderExample();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StoreOfflineOrderExample.Criteria criteria = example.createCriteria().andStatusEqualTo(storeOfflineOrderSearch.getStatus());
        if (storeOfflineOrderSearch.getLogmin().contains("-") == true && storeOfflineOrderSearch.getLogmin() != null && storeOfflineOrderSearch.getLogmax().contains("-") == true && storeOfflineOrderSearch.getLogmax() != null) {
            criteria = criteria.andCreateTimeBetween(sdf.parse(storeOfflineOrderSearch.getLogmin()),sdf.parse(storeOfflineOrderSearch.getLogmax()));
            //判断起始时间是否存在
        }
        List<StoreOfflineOrder> storeOfflineOrders = storeOfflineOrderMapper.selectByExample(example);
        for(int i=0;i<storeOfflineOrders.size();i++){
            StoreOfflineOrder storeOfflineOrder = storeOfflineOrders.get(i);
            storeOfflineOrder.setCreateTimeText(DateUtils.getTimeStr(storeOfflineOrder.getCreateTime()));
            storeOfflineOrder.setuName("小冰");
            StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeOfflineOrder.getSiId());
            storeOfflineOrder.setoContext(storeInfo.getName());
        }
        return storeOfflineOrders;
    }

    @Override
    public List<StoreOfflineOrder> selectAllParamContextClass(StoreOfflineOrderSearch storeOfflineOrderSearch) throws ParseException {
        StoreOfflineOrderExample example = new StoreOfflineOrderExample();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StoreOfflineOrderExample.Criteria criteria = example.createCriteria().andStatusEqualTo(storeOfflineOrderSearch.getStatus());
        if (storeOfflineOrderSearch.getLogmin().contains("-") == true && storeOfflineOrderSearch.getLogmin() != null && storeOfflineOrderSearch.getLogmax().contains("-") == true && storeOfflineOrderSearch.getLogmax() != null) {
            criteria = criteria.andCreateTimeBetween(sdf.parse(storeOfflineOrderSearch.getLogmin()),sdf.parse(storeOfflineOrderSearch.getLogmax()));
            //判断起始时间是否存在
        }
        List<StoreOfflineOrder> storeOfflineOrder = storeOfflineOrderMapper.selectByExample(example);
        return storeOfflineOrder;
    }

    @Override
    public StoreInfo selectStoreInfoClass(Integer siId) {
        return storeInfoMapper.selectByPrimaryKey(siId);
    }

    @Override
    public Transaction insertTransactionClass(Transaction transaction) {
        transactionMapper.insert(transaction);
        TransactionExample example = new TransactionExample();
        TransactionExample.Criteria criteria = example.createCriteria().andCodeEqualTo(transaction.getCode());
        List<Transaction> transactions = transactionMapper.selectByExample(example);
        return transactions.get(0);
    }

    @Override
    public IntegralTransferOrder selectIntegralTransferOrderClass(Integer id) {
        IntegralTransferOrderExample itoe = new IntegralTransferOrderExample();
        IntegralTransferOrderExample.Criteria criteria = itoe.createCriteria().andTrxIdEqualTo(id);
        List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderMapper.selectByExample(itoe);
        return integralTransferOrders.get(0);
    }

    @Override
    public void updateIntegralTransferOrderClass(IntegralTransferOrder integralTransferOrder) {
        integralTransferOrderMapper.updateByPrimaryKey(integralTransferOrder);
    }

    @Override
    public void deleteIntegralTransferOrderClass(Integer id) {
        integralTransferOrderMapper.deleteByPrimaryKey(id);
    }
}
