package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.IntegralTransferOrderMapper;
import com.gljr.jifen.dao.OnlineOrderMapper;
import com.gljr.jifen.dao.ProductMapper;
import com.gljr.jifen.dao.TransactionMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.OnlineOrderService;
import com.gljr.jifen.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

@Service
public class OnlineOrderServiceImpl implements OnlineOrderService {
    @Autowired
    private OnlineOrderMapper onlineOrderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private IntegralTransferOrderMapper integralTransferOrderMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Override
    public List<OnlineOrder> selectParentClass() {
        return null;
    }

    @Override
    public int insertClass(OnlineOrder onlineOrder) {
        return onlineOrderMapper.insert(onlineOrder);
    }

    @Override
    public List<OnlineOrder> selectSonClass() {
        return null;
    }

    @Override
    public int deleteClass(Integer id) {
        return onlineOrderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OnlineOrder> selectAllClass() {
        List<OnlineOrder> onlineOrders = onlineOrderMapper.selectByExample(null);
        LinkedList lls = new LinkedList();
        for(int i=0;i<onlineOrders.size();i++){
            OnlineOrder onlineOrder = onlineOrders.get(i);
            Product product = productMapper.selectByPrimaryKey(onlineOrder.getPid());
            onlineOrder.setpName(product.getName());
            onlineOrder.setCreateTimeText(DateUtils.getTimeStr(onlineOrder.getCreateTime()));
            onlineOrder.setuName("小冰");
            lls.add(onlineOrder);
        }
        return lls;
    }

    @Override
    public List<OnlineOrder> selectAllParamStatuClass(Byte status) {
        OnlineOrderExample example = new OnlineOrderExample();
        example.createCriteria().andStatusEqualTo(status);
        List<OnlineOrder> onlineOrders = onlineOrderMapper.selectByExample(example);
        LinkedList<OnlineOrder> lls = new LinkedList<>();
        for(int i =0;i<onlineOrders.size();i++){
            OnlineOrder onlineOrder = onlineOrders.get(i);
            Product product = productMapper.selectByPrimaryKey(onlineOrder.getPid());
            onlineOrder.setpName(product.getName());
            onlineOrder.setCreateTimeText(DateUtils.getTimeStr(onlineOrder.getCreateTime()));
            onlineOrder.setuName("小冰");
            lls.add(onlineOrder);
        }
        return lls;
    }

    @Override
    public List<OnlineOrder> selectAllParamTimeClass(OnlineOrderSearch onlineOrderSearch) throws ParseException {
        OnlineOrderExample example = new OnlineOrderExample();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        OnlineOrderExample.Criteria criteria = example.createCriteria().andStatusEqualTo(onlineOrderSearch.getStatus());
        if (onlineOrderSearch.getLogmin().contains("-") == true && onlineOrderSearch.getLogmin() != null && onlineOrderSearch.getLogmax().contains("-") == true && onlineOrderSearch.getLogmax() != null) {
            criteria = criteria.andCreateTimeBetween(sdf.parse(onlineOrderSearch.getLogmin()),sdf.parse(onlineOrderSearch.getLogmax()));
            //判断起始时间是否存在
        }
        List<OnlineOrder> onlineOrders = onlineOrderMapper.selectByExample(example);
        LinkedList<OnlineOrder> lls = new LinkedList<>();
        for(int i =0;i<onlineOrders.size();i++){
            OnlineOrder onlineOrder = onlineOrders.get(i);
            Product product = productMapper.selectByPrimaryKey(onlineOrder.getPid());
            onlineOrder.setpName(product.getName());
            onlineOrder.setCreateTimeText(DateUtils.getTimeStr(onlineOrder.getCreateTime()));
            onlineOrder.setuName("小冰");
            lls.add(onlineOrder);
        }
        return lls;
    }




    @Override
    public int updateClass(OnlineOrder onlineOrder) {
        return onlineOrderMapper.updateByPrimaryKey(onlineOrder);
    }

    @Override
    public OnlineOrder selectClass(Integer id) {
        OnlineOrder onlineOrder = onlineOrderMapper.selectByPrimaryKey(id);
        Product product = productMapper.selectByPrimaryKey(onlineOrder.getPid());
        onlineOrder.setpName(product.getName());
        onlineOrder.setCreateTimeText(DateUtils.getTimeStr(onlineOrder.getCreateTime()));
        return onlineOrder;
    }

    @Override
    public int updateClassSort(String sort, String id) {
        return 0;
    }

    @Override
    public Product selectProductClass(int id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insertIntegralTransferOrderClass(IntegralTransferOrder integralTransferOrder) {
        return integralTransferOrderMapper.insert(integralTransferOrder);
    }

    @Override
    public void deleteIntegralTransferOrderClass(Integer id) {
        integralTransferOrderMapper.deleteByPrimaryKey(id);
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
    public Transaction insertTransactionClass(Transaction transaction) {
        transactionMapper.insert(transaction);
        TransactionExample example = new TransactionExample();
        TransactionExample.Criteria criteria = example.createCriteria().andCodeEqualTo(transaction.getCode());
        List<Transaction> transactions = transactionMapper.selectByExample(example);
        return transactions.get(0);
    }

}
