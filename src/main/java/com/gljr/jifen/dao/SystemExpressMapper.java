package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.SystemExpress;
import com.gljr.jifen.pojo.SystemExpressExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SystemExpressMapper {
    long countByExample(SystemExpressExample example);

    int deleteByExample(SystemExpressExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SystemExpress record);

    int insertSelective(SystemExpress record);

    List<SystemExpress> selectByExample(SystemExpressExample example);

    SystemExpress selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SystemExpress record, @Param("example") SystemExpressExample example);

    int updateByExample(@Param("record") SystemExpress record, @Param("example") SystemExpressExample example);

    int updateByPrimaryKeySelective(SystemExpress record);

    int updateByPrimaryKey(SystemExpress record);
}