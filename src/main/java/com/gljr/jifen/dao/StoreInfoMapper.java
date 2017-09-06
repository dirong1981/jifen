package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.StoreInfo;
import com.gljr.jifen.pojo.StoreInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StoreInfoMapper {
    long countByExample(StoreInfoExample example);

    int deleteByExample(StoreInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StoreInfo record);

    int insertSelective(StoreInfo record);

    List<StoreInfo> selectByExampleWithBLOBs(StoreInfoExample example);

    List<StoreInfo> selectByExample(StoreInfoExample example);

    StoreInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StoreInfo record, @Param("example") StoreInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") StoreInfo record, @Param("example") StoreInfoExample example);

    int updateByExample(@Param("record") StoreInfo record, @Param("example") StoreInfoExample example);

    int updateByPrimaryKeySelective(StoreInfo record);

    int updateByPrimaryKeyWithBLOBs(StoreInfo record);

    int updateByPrimaryKey(StoreInfo record);
}