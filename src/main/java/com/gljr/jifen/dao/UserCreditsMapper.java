package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.UserCredits;
import com.gljr.jifen.pojo.UserCreditsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface UserCreditsMapper {

    @Select("SELECT * FROM user_credits WHERE owner_id = #{ownerId} and owner_type = 1" )
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "owner_type", property = "ownerType"),
            @Result(column = "owner_id", property = "ownerId"),
            @Result(column = "wallet_address", property = "walletAddress"),
            @Result(column = "integral", property = "integral"),
            @Result(column = "free_payment_limit", property = "freePaymentLimit"),
            @Result(column = "frozen_integral", property = "frozenIntegral"),
            @Result(column = "create_time", property = "createTime")
    })
    UserCredits getUserCredits(@Param("ownerId") Integer ownerId);

    @Select("SELECT * FROM user_credits x0 WHERE x0.owner_id IN ( " +
            "   SELECT x1.id FROM store_info x0 JOIN store_info x1 ON x0.aid=x1.aid " +
            "       WHERE x0.id=#{ownerId}) AND x0.owner_type=2")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "owner_type", property = "ownerType"),
            @Result(column = "owner_id", property = "ownerId"),
            @Result(column = "wallet_address", property = "walletAddress"),
            @Result(column = "integral", property = "integral"),
            @Result(column = "free_payment_limit", property = "freePaymentLimit"),
            @Result(column = "frozen_integral", property = "frozenIntegral"),
            @Result(column = "create_time", property = "createTime")
    })
    UserCredits getMerchantCredits(@Param("ownerId") Integer ownerId);

    @Select("SELECT x0.* FROM user_credits x0,store_info x1 WHERE x0.owner_type=2 AND x0.owner_id=x1.id AND x1.aid=#{managerId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "owner_type", property = "ownerType"),
            @Result(column = "owner_id", property = "ownerId"),
            @Result(column = "wallet_address", property = "walletAddress"),
            @Result(column = "integral", property = "integral"),
            @Result(column = "free_payment_limit", property = "freePaymentLimit"),
            @Result(column = "frozen_integral", property = "frozenIntegral"),
            @Result(column = "create_time", property = "createTime")
    })
    UserCredits getStoreCreditsByManagerId(@Param("managerId") Integer managerId);

}