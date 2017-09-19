package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.UserExtInfo;
import com.gljr.jifen.pojo.UserExtInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserExtInfoMapper {
    long countByExample(UserExtInfoExample example);

    int deleteByExample(UserExtInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserExtInfo record);

    int insertSelective(UserExtInfo record);

    List<UserExtInfo> selectByExample(UserExtInfoExample example);

    UserExtInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserExtInfo record, @Param("example") UserExtInfoExample example);

    int updateByExample(@Param("record") UserExtInfo record, @Param("example") UserExtInfoExample example);

    int updateByPrimaryKeySelective(UserExtInfo record);

    int updateByPrimaryKey(UserExtInfo record);
}