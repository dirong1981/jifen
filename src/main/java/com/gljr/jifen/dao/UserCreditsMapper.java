package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.UserCredits;
import com.gljr.jifen.pojo.UserCreditsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface UserCreditsMapper {

    @Select("SELECT * FROM user_credits WHERE owner_id = #{ownerId} and owner_type = #{ownerType}")
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
    UserCredits getUserCredits(@Param("ownerId") Integer ownerId, @Param("ownerType") Integer ownerType);

}