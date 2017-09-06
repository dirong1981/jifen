package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.StoreOfflineOrder;
import com.gljr.jifen.pojo.StoreOfflineOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StoreOfflineOrderMapper {
    long countByExample(StoreOfflineOrderExample example);

    int deleteByExample(StoreOfflineOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StoreOfflineOrder record);

    int insertSelective(StoreOfflineOrder record);

    List<StoreOfflineOrder> selectByExample(StoreOfflineOrderExample example);

    StoreOfflineOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StoreOfflineOrder record, @Param("example") StoreOfflineOrderExample example);

    int updateByExample(@Param("record") StoreOfflineOrder record, @Param("example") StoreOfflineOrderExample example);

    int updateByPrimaryKeySelective(StoreOfflineOrder record);

    int updateByPrimaryKey(StoreOfflineOrder record);
}