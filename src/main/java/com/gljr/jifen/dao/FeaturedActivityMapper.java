package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.FeaturedActivity;
import com.gljr.jifen.pojo.FeaturedActivityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FeaturedActivityMapper {
    long countByExample(FeaturedActivityExample example);

    int deleteByExample(FeaturedActivityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FeaturedActivity record);

    int insertSelective(FeaturedActivity record);

    List<FeaturedActivity> selectByExample(FeaturedActivityExample example);

    FeaturedActivity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FeaturedActivity record, @Param("example") FeaturedActivityExample example);

    int updateByExample(@Param("record") FeaturedActivity record, @Param("example") FeaturedActivityExample example);

    int updateByPrimaryKeySelective(FeaturedActivity record);

    int updateByPrimaryKey(FeaturedActivity record);
}