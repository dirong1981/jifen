package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.StoreCoupon;
import com.gljr.jifen.pojo.StoreCouponExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StoreCouponMapper {
    long countByExample(StoreCouponExample example);

    int deleteByExample(StoreCouponExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StoreCoupon record);

    int insertSelective(StoreCoupon record);

    List<StoreCoupon> selectByExample(StoreCouponExample example);

    StoreCoupon selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StoreCoupon record, @Param("example") StoreCouponExample example);

    int updateByExample(@Param("record") StoreCoupon record, @Param("example") StoreCouponExample example);

    int updateByPrimaryKeySelective(StoreCoupon record);

    int updateByPrimaryKey(StoreCoupon record);
}