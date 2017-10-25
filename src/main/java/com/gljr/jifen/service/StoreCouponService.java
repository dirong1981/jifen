package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.StoreCoupon;

public interface StoreCouponService {

    JsonResult insertStoreCoupon(StoreCoupon storeCoupon);

    JsonResult selectAllStoreCoupon(Integer page, Integer per_page);

    JsonResult startStoreCoupon(Integer couponId);

    JsonResult stopStoreCoupon(Integer couponId);

    JsonResult deleteStoreCoupon(Integer couponId);

    JsonResult selectStoreCouponById(Integer couponId);

    JsonResult selectUserCouponById(Integer couponId, String uid);

    JsonResult selectStoreCouponByUid(String uid, Integer page, Integer per_page, Integer sort, String start_time, String end_time);

    JsonResult insertStoreCouponOrder(Integer scId, String uid);

    JsonResult refundStoreCouponOrder(Integer couponId, String uid);

    JsonResult selectStoreCouponStatusById(Integer couponId);

    JsonResult allowCancelStoreCouponById(Integer couponId, Integer allow);
}
