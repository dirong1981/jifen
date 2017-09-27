package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.ModuleAggregationProduct;
import com.gljr.jifen.pojo.ModuleAggregationProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ModuleAggregationProductMapper {
    long countByExample(ModuleAggregationProductExample example);

    int deleteByExample(ModuleAggregationProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ModuleAggregationProduct record);

    int insertSelective(ModuleAggregationProduct record);

    List<ModuleAggregationProduct> selectByExample(ModuleAggregationProductExample example);

    ModuleAggregationProduct selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ModuleAggregationProduct record, @Param("example") ModuleAggregationProductExample example);

    int updateByExample(@Param("record") ModuleAggregationProduct record, @Param("example") ModuleAggregationProductExample example);

    int updateByPrimaryKeySelective(ModuleAggregationProduct record);

    int updateByPrimaryKey(ModuleAggregationProduct record);
}