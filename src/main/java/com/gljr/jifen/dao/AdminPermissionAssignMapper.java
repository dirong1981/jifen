package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.AdminPermissionAssign;
import com.gljr.jifen.pojo.AdminPermissionAssignExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminPermissionAssignMapper {
    long countByExample(AdminPermissionAssignExample example);

    int deleteByExample(AdminPermissionAssignExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AdminPermissionAssign record);

    int insertSelective(AdminPermissionAssign record);

    List<AdminPermissionAssign> selectByExample(AdminPermissionAssignExample example);

    AdminPermissionAssign selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AdminPermissionAssign record, @Param("example") AdminPermissionAssignExample example);

    int updateByExample(@Param("record") AdminPermissionAssign record, @Param("example") AdminPermissionAssignExample example);

    int updateByPrimaryKeySelective(AdminPermissionAssign record);

    int updateByPrimaryKey(AdminPermissionAssign record);
}