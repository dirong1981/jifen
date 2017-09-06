package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.IntegralTransferOrderMapper;
import com.gljr.jifen.dao.TransactionMapper;
import com.gljr.jifen.pojo.IntegralTransferOrder;
import com.gljr.jifen.pojo.IntegralTransferOrderExample;
import com.gljr.jifen.pojo.IntegralTransferOrderSearch;
import com.gljr.jifen.pojo.Transaction;
import com.gljr.jifen.service.IntegralTransferOrderService;
import com.gljr.jifen.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class IntegralTransferOrderServiceImpl implements IntegralTransferOrderService {
    @Autowired
    private IntegralTransferOrderMapper integralTransferOrderMapper;

    @Override
    public List<IntegralTransferOrder> selectParentClass() {
        return null;
    }

    @Override
    public int insertClass(IntegralTransferOrder integralTransferOrder) {
        return 0;
    }

    @Override
    public List<IntegralTransferOrder> selectSonClass() {
        return null;
    }

    @Override
    public int deleteClass(Integer id) {
        return integralTransferOrderMapper.deleteByPrimaryKey(id);
    }


    @Override
    public List<IntegralTransferOrder> selectAllClass() {
        List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderMapper.selectByExample(null);
        for(int i=0;i<integralTransferOrders.size();i++){
            IntegralTransferOrder integralTransferOrder = integralTransferOrders.get(i);
            integralTransferOrder.setCreateTimeText(DateUtils.getTimeStr(integralTransferOrder.getCreateTime()));
            integralTransferOrder.setuName("小冰");
            integralTransferOrder.setgUName("小米");
        }
        return integralTransferOrders;
    }

    @Override
    public int updateClass(IntegralTransferOrder integralTransferOrder) {
        return integralTransferOrderMapper.updateByPrimaryKey(integralTransferOrder);
    }

    @Override
    public IntegralTransferOrder selectClass(Integer id) {
        IntegralTransferOrder integralTransferOrder = integralTransferOrderMapper.selectByPrimaryKey(id);
        integralTransferOrder.setCreateTimeText(DateUtils.getTimeStr(integralTransferOrder.getCreateTime()));
        integralTransferOrder.setuName("小冰");
        integralTransferOrder.setgUName("小米");
        return integralTransferOrder;
    }

    @Override
    public int updateClassSort(String sort, String id) {
        return 0;
    }


    @Override
    public List<IntegralTransferOrder> selectAllParamTimeClass(IntegralTransferOrderSearch integralTransferOrderSearch) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        IntegralTransferOrderExample example = new IntegralTransferOrderExample();
        IntegralTransferOrderExample.Criteria criteria = example.createCriteria().andStatusEqualTo(integralTransferOrderSearch.getStatus());
        if (integralTransferOrderSearch.getLogmin().contains("-") == true && integralTransferOrderSearch.getLogmin() != null && integralTransferOrderSearch.getLogmax().contains("-") == true && integralTransferOrderSearch.getLogmax() != null) {
            criteria = criteria.andCreateTimeBetween(sdf.parse(integralTransferOrderSearch.getLogmin()),sdf.parse(integralTransferOrderSearch.getLogmax()));
            //判断起始时间是否存在
        }
        List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderMapper.selectByExample(example);
        for(int i=0;i<integralTransferOrders.size();i++){
            IntegralTransferOrder integralTransferOrder = integralTransferOrders.get(i);
            integralTransferOrder.setCreateTimeText(DateUtils.getTimeStr(integralTransferOrder.getCreateTime()));
            integralTransferOrder.setuName("小冰");
            integralTransferOrder.setgUName("小米");
        }
        return integralTransferOrders;
    }

    @Override
    public List<IntegralTransferOrder> selectAllParamStatuClass(Byte status) {
        IntegralTransferOrderExample example = new IntegralTransferOrderExample();
        example.createCriteria().andStatusEqualTo(status);
        List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderMapper.selectByExample(example);
        for(int i=0;i<integralTransferOrders.size();i++){
            IntegralTransferOrder integralTransferOrder = integralTransferOrders.get(i);
            integralTransferOrder.setCreateTimeText(DateUtils.getTimeStr(integralTransferOrder.getCreateTime()));
            integralTransferOrder.setuName("小冰");
            integralTransferOrder.setgUName("小米");
        }
        return integralTransferOrders;
    }

}
