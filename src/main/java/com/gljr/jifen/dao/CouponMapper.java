package com.gljr.jifen.dao;

import com.gljr.jifen.pojo.UserCoupon;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface CouponMapper {

    @Select("select count(0) > 0 from `user_coupon` where `coupon_code` = #{couponCode}")
    boolean isCouponCodeExist(@Param("couponCode") String couponCode);


    @Select("select * from `user_coupon` where `coupon_code` = #{couponCode}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "uid", property = "uid"),
            @Result(column = "si_id", property = "siId"),
            @Result(column = "sc_id", property = "scId"),
            @Result(column = "coupon_code", property = "couponCode"),
            @Result(column = "soo_id", property = "sooId"),
            @Result(column = "valid_from", property = "validFrom"),
            @Result(column = "valid_to", property = "validTo"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime")
    })
    UserCoupon getUserCouponByCode(@Param("couponCode") String couponCode);

}
