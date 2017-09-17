package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.SystemPermission;
import com.gljr.jifen.pojo.SystemPermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SystemPermissionMapper {
    long countByExample(SystemPermissionExample example);

    int deleteByExample(SystemPermissionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SystemPermission record);

    int insertSelective(SystemPermission record);

    List<SystemPermission> selectByExample(SystemPermissionExample example);

    SystemPermission selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SystemPermission record, @Param("example") SystemPermissionExample example);

    int updateByExample(@Param("record") SystemPermission record, @Param("example") SystemPermissionExample example);

    int updateByPrimaryKeySelective(SystemPermission record);

    int updateByPrimaryKey(SystemPermission record);
}