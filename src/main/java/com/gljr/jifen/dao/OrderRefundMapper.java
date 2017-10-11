package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.OrderRefund;
import org.apache.ibatis.annotations.Insert;

public interface OrderRefundMapper {

    @Insert("insert into order_refund(order_id, order_type, trx_id, trx_code, dtchain_block_id, ext_order_id, " +
            "store_id, to_uid, integral, created) values(#{orderId}, #{orderType}, #{trxId}, #{trxCode}, #{dtchainBlockId}, " +
            "#{extOrderId}, #{storeId}, #{toUid}, #{integral}, #{created})")
    int refundOrder(OrderRefund orderRefund);


}
