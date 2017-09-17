package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.StoreExtInfo;
import com.gljr.jifen.pojo.StoreExtInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StoreExtInfoMapper {
    long countByExample(StoreExtInfoExample example);

    int deleteByExample(StoreExtInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StoreExtInfo record);

    int insertSelective(StoreExtInfo record);

    List<StoreExtInfo> selectByExample(StoreExtInfoExample example);

    StoreExtInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StoreExtInfo record, @Param("example") StoreExtInfoExample example);

    int updateByExample(@Param("record") StoreExtInfo record, @Param("example") StoreExtInfoExample example);

    int updateByPrimaryKeySelective(StoreExtInfo record);

    int updateByPrimaryKey(StoreExtInfo record);
}