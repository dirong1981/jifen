package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.ModuleAggregation;
import com.gljr.jifen.pojo.ModuleAggregationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ModuleAggregationMapper {
    long countByExample(ModuleAggregationExample example);

    int deleteByExample(ModuleAggregationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ModuleAggregation record);

    int insertSelective(ModuleAggregation record);

    List<ModuleAggregation> selectByExample(ModuleAggregationExample example);

    ModuleAggregation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ModuleAggregation record, @Param("example") ModuleAggregationExample example);

    int updateByExample(@Param("record") ModuleAggregation record, @Param("example") ModuleAggregationExample example);

    int updateByPrimaryKeySelective(ModuleAggregation record);

    int updateByPrimaryKey(ModuleAggregation record);
}