package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.ModulePicture;
import com.gljr.jifen.pojo.ModulePictureExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ModulePictureMapper {
    long countByExample(ModulePictureExample example);

    int deleteByExample(ModulePictureExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ModulePicture record);

    int insertSelective(ModulePicture record);

    List<ModulePicture> selectByExample(ModulePictureExample example);

    ModulePicture selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ModulePicture record, @Param("example") ModulePictureExample example);

    int updateByExample(@Param("record") ModulePicture record, @Param("example") ModulePictureExample example);

    int updateByPrimaryKeySelective(ModulePicture record);

    int updateByPrimaryKey(ModulePicture record);
}