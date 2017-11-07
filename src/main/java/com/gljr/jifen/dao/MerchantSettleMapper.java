package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.MerchantSettlement;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface MerchantSettleMapper {

    @Select("SELECT IFNULL(sum(x0.current_amount),0) AS total_amount FROM merchant_settlement x0 WHERE x0.merchant_id=#{storeId} AND x0.`status`=1")
    Long getTotalSettleIntegrals(@Param("storeId") Long storeId);

    @Insert("INSERT INTO merchant_settlement (settlement_no,merchant_id,last_block_id,current_from,current_to," +
            "current_amount,settle_time,total_amount,settle_type,bank_account,settle_cycle,remit_date,bank_receipt_key," +
            "`status`,manager_id) VALUES (#{settlementNo}, #{merchantId}, #{lastBlockId}, #{currentFrom}, #{currentTo}, " +
            "#{currentAmount}, #{settleTime}, #{totalAmount}, #{settleType},#{bankAccount}, #{settleCycle}, " +
            "#{remitDate}, #{bankReceiptKey}, #{status}, #{managerId})")
    int initMerchantSettleInfo(MerchantSettlement ms);

}
