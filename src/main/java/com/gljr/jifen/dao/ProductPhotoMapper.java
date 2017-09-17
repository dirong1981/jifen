package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.ProductPhoto;
import com.gljr.jifen.pojo.ProductPhotoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductPhotoMapper {
    long countByExample(ProductPhotoExample example);

    int deleteByExample(ProductPhotoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProductPhoto record);

    int insertSelective(ProductPhoto record);

    List<ProductPhoto> selectByExample(ProductPhotoExample example);

    ProductPhoto selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProductPhoto record, @Param("example") ProductPhotoExample example);

    int updateByExample(@Param("record") ProductPhoto record, @Param("example") ProductPhotoExample example);

    int updateByPrimaryKeySelective(ProductPhoto record);

    int updateByPrimaryKey(ProductPhoto record);
}