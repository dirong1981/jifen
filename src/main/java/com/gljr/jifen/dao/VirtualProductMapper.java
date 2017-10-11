package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.VirtualProduct;
import com.gljr.jifen.pojo.VirtualProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VirtualProductMapper {
    long countByExample(VirtualProductExample example);

    int deleteByExample(VirtualProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VirtualProduct record);

    int insertSelective(VirtualProduct record);

    List<VirtualProduct> selectByExampleWithBLOBs(VirtualProductExample example);

    List<VirtualProduct> selectByExample(VirtualProductExample example);

    VirtualProduct selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VirtualProduct record, @Param("example") VirtualProductExample example);

    int updateByExampleWithBLOBs(@Param("record") VirtualProduct record, @Param("example") VirtualProductExample example);

    int updateByExample(@Param("record") VirtualProduct record, @Param("example") VirtualProductExample example);

    int updateByPrimaryKeySelective(VirtualProduct record);

    int updateByPrimaryKeyWithBLOBs(VirtualProduct record);

    int updateByPrimaryKey(VirtualProduct record);
}