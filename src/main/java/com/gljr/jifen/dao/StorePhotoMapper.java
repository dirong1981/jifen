package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.StorePhoto;
import com.gljr.jifen.pojo.StorePhotoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StorePhotoMapper {
    long countByExample(StorePhotoExample example);

    int deleteByExample(StorePhotoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StorePhoto record);

    int insertSelective(StorePhoto record);

    List<StorePhoto> selectByExample(StorePhotoExample example);

    StorePhoto selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StorePhoto record, @Param("example") StorePhotoExample example);

    int updateByExample(@Param("record") StorePhoto record, @Param("example") StorePhotoExample example);

    int updateByPrimaryKeySelective(StorePhoto record);

    int updateByPrimaryKey(StorePhoto record);
}