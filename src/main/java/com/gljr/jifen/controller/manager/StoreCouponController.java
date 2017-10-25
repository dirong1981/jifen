package com.gljr.jifen.controller.manager;


import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.StoreCoupon;
import com.gljr.jifen.service.StoreCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/manager/coupons")
public class StoreCouponController {

    @Autowired
    private StoreCouponService storeCouponService;


    @GetMapping(value = "/all")
    @ResponseBody
    public JsonResult selectAllStoreCoupon(@RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "per_page", required = false) Integer per_page){


        if(StringUtils.isEmpty(page)){
            page = 1;
        }

        if(StringUtils.isEmpty(per_page)){
            per_page = 10;
        }

        JsonResult jsonResult = storeCouponService.selectAllStoreCoupon(page, per_page);

        return jsonResult;
    }

    @PostMapping
    @ResponseBody
    public JsonResult insertStoreCoupon(StoreCoupon storeCoupon, HttpServletRequest httpServletRequest){

        JsonResult jsonResult = new JsonResult();

        //获取开始结束时间
        String start = httpServletRequest.getParameter("start");
        String end = httpServletRequest.getParameter("end");

        if(storeCoupon.getValidityType() == 1) {
            if (StringUtils.isEmpty(start) || StringUtils.isEmpty(end)) {
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage("请选择开始和结束时间！");
                return jsonResult;
            }

            //时间转换
            try {
                if (!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) {
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                    storeCoupon.setValidFrom(dateFormat.parse(start));
                    storeCoupon.setValidTo(dateFormat.parse(end));
                }
            } catch (Exception e) {
                jsonResult.setMessage("时间设置错误");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }
        }else{
            if(StringUtils.isEmpty(storeCoupon.getValidDays())) {
                jsonResult.setMessage("请填写有效期！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }
        }

        storeCoupon.setCreateTime(new Date());

        jsonResult = storeCouponService.insertStoreCoupon(storeCoupon);

        return jsonResult;

    }

    @GetMapping(value = "/{id}/rejection")
    @ResponseBody
    public JsonResult stopStoreCouponById(@PathVariable("id") Integer couponId){

        JsonResult jsonResult = storeCouponService.stopStoreCoupon(couponId);

        return jsonResult;
    }


    @GetMapping(value = "/{id}/acceptance")
    @ResponseBody
    public JsonResult startStoreCouponById(@PathVariable("id") Integer couponId){

        JsonResult jsonResult = storeCouponService.startStoreCoupon(couponId);

        return jsonResult;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult deleteStoreCouponById(@PathVariable("id") Integer couponId){

        JsonResult jsonResult = storeCouponService.deleteStoreCoupon(couponId);

        return jsonResult;
    }

    @GetMapping(value = "/allow")
    @ResponseBody
    public JsonResult allowCancelStoreCouponById(@RequestParam(value = "id") Integer couponId, @RequestParam(value = "allow") Integer allow){
        JsonResult jsonResult = storeCouponService.allowCancelStoreCouponById(couponId, allow);
        return jsonResult;
    }

}
