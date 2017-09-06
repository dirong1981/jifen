package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.IntegralTransferOrder;
import com.gljr.jifen.pojo.IntegralTransferOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IntegralTransferOrderMapper {
    long countByExample(IntegralTransferOrderExample example);

    int deleteByExample(IntegralTransferOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(IntegralTransferOrder record);

    int insertSelective(IntegralTransferOrder record);

    List<IntegralTransferOrder> selectByExample(IntegralTransferOrderExample example);

    IntegralTransferOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") IntegralTransferOrder record, @Param("example") IntegralTransferOrderExample example);

    int updateByExample(@Param("record") IntegralTransferOrder record, @Param("example") IntegralTransferOrderExample example);

    int updateByPrimaryKeySelective(IntegralTransferOrder record);

    int updateByPrimaryKey(IntegralTransferOrder record);
}