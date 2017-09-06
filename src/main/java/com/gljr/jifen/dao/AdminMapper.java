package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.Admin;
import com.gljr.jifen.pojo.AdminExample;
import java.util.List;
import java.util.Map;

import com.gljr.jifen.pojo.AdminList;
import org.apache.ibatis.annotations.Param;

public interface AdminMapper {
    long countByExample(AdminExample example);

    int deleteByExample(AdminExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    List<Admin> selectByExample(AdminExample example);

    Admin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByExample(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    List<AdminList> getListAdmin();
}