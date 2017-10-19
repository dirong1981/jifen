package com.gljr.jifen.controller;


import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.StoreCouponMapper;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.StoreCoupon;
import com.gljr.jifen.service.DTChainService;
import com.gljr.jifen.service.RedisService;
import com.gljr.jifen.service.StoreCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/coupons")
public class CouponController extends BaseController{

    @Autowired
    private StoreCouponService storeCouponService;

    @Autowired
    private RedisService redisService;
    @Autowired
    private DTChainService chainService;



    /**
     * 查询一个商户代金券
     * @param couponId
     * @return
     */
    @GetMapping(value = "/{couponId}")
    @ResponseBody
    public JsonResult selectStoreCouponById(@PathVariable(value = "couponId") Integer couponId){
        JsonResult jsonResult = storeCouponService.selectStoreCouponById(couponId);

        return jsonResult;
    }

    /**
     * 查询一个用户的代金券
     * @param couponId
     * @return
     */
    @GetMapping(value = "/user/{couponId}")
    @ResponseBody
    public JsonResult selectUserCouponById(@PathVariable(value = "couponId") Integer couponId, HttpServletRequest httpServletRequest){
        String uid = httpServletRequest.getHeader("uid");

        JsonResult jsonResult = storeCouponService.selectUserCouponById(couponId, uid);

        return jsonResult;
    }


    /**
     * 查询用户所有代金券
     * @param httpServletRequest
     * @param page
     * @param per_page
     * @param sort
     * @param start_time
     * @param end_time
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectStoreCouponByUid(HttpServletRequest httpServletRequest,
                                             @RequestParam(value = "page", required = false) Integer page,
                                             @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort,
                                             @RequestParam(value = "start_time", required = false) String start_time, @RequestParam(value = "end_time", required = false) String end_time){

        String uid = httpServletRequest.getHeader("uid");

        //设置各个参数的默认值
        if(page == null){
            page = 1;
        }
        if(per_page == null){
            per_page = 10;
        }
        if(sort == null || sort > 4 || sort < 0){
            sort = 0;
        }

        JsonResult jsonResult = storeCouponService.selectStoreCouponByUid(uid, page, per_page, sort, start_time, end_time);

        return jsonResult;
    }


    /**
     * 购买代金券
     * @param scId
     * @param random
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult insertStoreCouponOrder(@RequestParam(value = "sc") Integer scId, @RequestParam(value = "random") Integer random, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(scId) || StringUtils.isEmpty(random)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        String uid = httpServletRequest.getHeader("uid");

//        if(redisService.get(uid + "random") == null){
//            jsonResult.setMessage("非法订单！");
//            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
//            return jsonResult;
//        }
//
//        if(!redisService.get(uid+"random").equals(random)){
//            jsonResult.setMessage("非法订单！");
//            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
//            return jsonResult;
//        }

        jsonResult = storeCouponService.insertStoreCouponOrder(scId, uid);


        redisService.evict(uid+"random");
        return jsonResult;
    }

    @GetMapping(value = "/refund/{id}")
    @ResponseBody
    public JsonResult refundStoreCouponOrder(@PathVariable(value = "id") Integer id, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        String uid = httpServletRequest.getHeader("uid");

        jsonResult = storeCouponService.refundStoreCouponOrder(id, uid);

        return jsonResult;
    }

    @GetMapping(value = "/status/{id}")
    @ResponseBody
    public JsonResult selectUserCouponStatusById(@PathVariable(value = "id") Integer id){
        JsonResult jsonResult = storeCouponService.selectStoreCouponStatusById(id);

        return jsonResult;
    }

}
