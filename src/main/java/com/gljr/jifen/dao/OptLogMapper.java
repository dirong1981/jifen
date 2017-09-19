package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.OptLog;
import com.gljr.jifen.pojo.OptLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OptLogMapper {
    long countByExample(OptLogExample example);

    int deleteByExample(OptLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OptLog record);

    int insertSelective(OptLog record);

    List<OptLog> selectByExampleWithBLOBs(OptLogExample example);

    List<OptLog> selectByExample(OptLogExample example);

    OptLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OptLog record, @Param("example") OptLogExample example);

    int updateByExampleWithBLOBs(@Param("record") OptLog record, @Param("example") OptLogExample example);

    int updateByExample(@Param("record") OptLog record, @Param("example") OptLogExample example);

    int updateByPrimaryKeySelective(OptLog record);

    int updateByPrimaryKeyWithBLOBs(OptLog record);

    int updateByPrimaryKey(OptLog record);
}