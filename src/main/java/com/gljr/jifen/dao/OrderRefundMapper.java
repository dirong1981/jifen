package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.OrderRefund;
import com.gljr.jifen.pojo.OrderRefundExample;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface OrderRefundMapper {
    long countByExample(OrderRefundExample example);

    int deleteByExample(OrderRefundExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderRefund record);

    int insertSelective(OrderRefund record);

    List<OrderRefund> selectByExample(OrderRefundExample example);

    OrderRefund selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderRefund record, @Param("example") OrderRefundExample example);

    int updateByExample(@Param("record") OrderRefund record, @Param("example") OrderRefundExample example);

    int updateByPrimaryKeySelective(OrderRefund record);

    int updateByPrimaryKey(OrderRefund record);

    @Insert("insert into order_refund(order_id, order_type, trx_id, trx_code, dtchain_block_id, ext_order_id, " +
            "store_id, to_uid, integral, created) values(#{orderId}, #{orderType}, #{trxId}, #{trxCode}, #{dtchainBlockId}, " +
            "#{extOrderId}, #{storeId}, #{toUid}, #{integral}, #{created})")
    int refundOrder(OrderRefund orderRefund);
}