package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.UserCredits;
import com.gljr.jifen.pojo.UserCreditsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserCreditsMapper {
    long countByExample(UserCreditsExample example);

    int deleteByExample(UserCreditsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserCredits record);

    int insertSelective(UserCredits record);

    List<UserCredits> selectByExample(UserCreditsExample example);

    UserCredits selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserCredits record, @Param("example") UserCreditsExample example);

    int updateByExample(@Param("record") UserCredits record, @Param("example") UserCreditsExample example);

    int updateByPrimaryKeySelective(UserCredits record);

    int updateByPrimaryKey(UserCredits record);
}