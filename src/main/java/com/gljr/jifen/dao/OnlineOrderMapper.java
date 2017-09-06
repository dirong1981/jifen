package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.OnlineOrder;
import com.gljr.jifen.pojo.OnlineOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OnlineOrderMapper {
    long countByExample(OnlineOrderExample example);

    int deleteByExample(OnlineOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OnlineOrder record);

    int insertSelective(OnlineOrder record);

    List<OnlineOrder> selectByExample(OnlineOrderExample example);

    OnlineOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OnlineOrder record, @Param("example") OnlineOrderExample example);

    int updateByExample(@Param("record") OnlineOrder record, @Param("example") OnlineOrderExample example);

    int updateByPrimaryKeySelective(OnlineOrder record);

    int updateByPrimaryKey(OnlineOrder record);
}