package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.StoreCouponOrder;
import com.gljr.jifen.pojo.StoreCouponOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StoreCouponOrderMapper {
    long countByExample(StoreCouponOrderExample example);

    int deleteByExample(StoreCouponOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StoreCouponOrder record);

    int insertSelective(StoreCouponOrder record);

    List<StoreCouponOrder> selectByExample(StoreCouponOrderExample example);

    StoreCouponOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StoreCouponOrder record, @Param("example") StoreCouponOrderExample example);

    int updateByExample(@Param("record") StoreCouponOrder record, @Param("example") StoreCouponOrderExample example);

    int updateByPrimaryKeySelective(StoreCouponOrder record);

    int updateByPrimaryKey(StoreCouponOrder record);
}