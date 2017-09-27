package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.ModuleAggregationCondition;
import com.gljr.jifen.pojo.ModuleAggregationConditionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ModuleAggregationConditionMapper {
    long countByExample(ModuleAggregationConditionExample example);

    int deleteByExample(ModuleAggregationConditionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ModuleAggregationCondition record);

    int insertSelective(ModuleAggregationCondition record);

    List<ModuleAggregationCondition> selectByExampleWithBLOBs(ModuleAggregationConditionExample example);

    List<ModuleAggregationCondition> selectByExample(ModuleAggregationConditionExample example);

    ModuleAggregationCondition selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ModuleAggregationCondition record, @Param("example") ModuleAggregationConditionExample example);

    int updateByExampleWithBLOBs(@Param("record") ModuleAggregationCondition record, @Param("example") ModuleAggregationConditionExample example);

    int updateByExample(@Param("record") ModuleAggregationCondition record, @Param("example") ModuleAggregationConditionExample example);

    int updateByPrimaryKeySelective(ModuleAggregationCondition record);

    int updateByPrimaryKeyWithBLOBs(ModuleAggregationCondition record);

    int updateByPrimaryKey(ModuleAggregationCondition record);
}