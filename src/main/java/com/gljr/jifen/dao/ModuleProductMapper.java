package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.ModuleProduct;
import com.gljr.jifen.pojo.ModuleProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ModuleProductMapper {
    long countByExample(ModuleProductExample example);

    int deleteByExample(ModuleProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ModuleProduct record);

    int insertSelective(ModuleProduct record);

    List<ModuleProduct> selectByExample(ModuleProductExample example);

    ModuleProduct selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ModuleProduct record, @Param("example") ModuleProductExample example);

    int updateByExample(@Param("record") ModuleProduct record, @Param("example") ModuleProductExample example);

    int updateByPrimaryKeySelective(ModuleProduct record);

    int updateByPrimaryKey(ModuleProduct record);
}