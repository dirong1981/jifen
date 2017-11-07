package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.OnlineOrderDelivery;
import com.gljr.jifen.pojo.OnlineOrderDeliveryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OnlineOrderDeliveryMapper {
    long countByExample(OnlineOrderDeliveryExample example);

    int deleteByExample(OnlineOrderDeliveryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OnlineOrderDelivery record);

    int insertSelective(OnlineOrderDelivery record);

    List<OnlineOrderDelivery> selectByExample(OnlineOrderDeliveryExample example);

    OnlineOrderDelivery selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OnlineOrderDelivery record, @Param("example") OnlineOrderDeliveryExample example);

    int updateByExample(@Param("record") OnlineOrderDelivery record, @Param("example") OnlineOrderDeliveryExample example);

    int updateByPrimaryKeySelective(OnlineOrderDelivery record);

    int updateByPrimaryKey(OnlineOrderDelivery record);
}