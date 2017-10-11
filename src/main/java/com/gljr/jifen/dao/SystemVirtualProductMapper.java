package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.SystemVirtualProduct;
import com.gljr.jifen.pojo.SystemVirtualProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SystemVirtualProductMapper {
    long countByExample(SystemVirtualProductExample example);

    int deleteByExample(SystemVirtualProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SystemVirtualProduct record);

    int insertSelective(SystemVirtualProduct record);

    List<SystemVirtualProduct> selectByExample(SystemVirtualProductExample example);

    SystemVirtualProduct selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SystemVirtualProduct record, @Param("example") SystemVirtualProductExample example);

    int updateByExample(@Param("record") SystemVirtualProduct record, @Param("example") SystemVirtualProductExample example);

    int updateByPrimaryKeySelective(SystemVirtualProduct record);

    int updateByPrimaryKey(SystemVirtualProduct record);
}