package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.AdminOnline;
import com.gljr.jifen.pojo.AdminOnlineExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminOnlineMapper {
    long countByExample(AdminOnlineExample example);

    int deleteByExample(AdminOnlineExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AdminOnline record);

    int insertSelective(AdminOnline record);

    List<AdminOnline> selectByExample(AdminOnlineExample example);

    AdminOnline selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AdminOnline record, @Param("example") AdminOnlineExample example);

    int updateByExample(@Param("record") AdminOnline record, @Param("example") AdminOnlineExample example);

    int updateByPrimaryKeySelective(AdminOnline record);

    int updateByPrimaryKey(AdminOnline record);
}