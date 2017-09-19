package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.UserOnline;
import com.gljr.jifen.pojo.UserOnlineExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserOnlineMapper {
    long countByExample(UserOnlineExample example);

    int deleteByExample(UserOnlineExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserOnline record);

    int insertSelective(UserOnline record);

    List<UserOnline> selectByExample(UserOnlineExample example);

    UserOnline selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserOnline record, @Param("example") UserOnlineExample example);

    int updateByExample(@Param("record") UserOnline record, @Param("example") UserOnlineExample example);

    int updateByPrimaryKeySelective(UserOnline record);

    int updateByPrimaryKey(UserOnline record);
}