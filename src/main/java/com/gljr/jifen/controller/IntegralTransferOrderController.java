package com.gljr.jifen.controller;


import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.IntegralTransferOrderService;
import com.gljr.jifen.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@CrossOrigin(origins = "*", maxAge = 3600)

@Controller

@RequestMapping(value = "/v1/integral-orders")
public class IntegralTransferOrderController {

    @Autowired
    private IntegralTransferOrderService integralTransferOrderService;

    @Autowired
    private RedisService redisService;


    /**
     * 添加一个积分转增订单
     * @param integralTransferOrder 订单
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ResponseBody

    public JsonResult insertIntegralOrder(IntegralTransferOrder integralTransferOrder, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        String uid = httpServletRequest.getHeader("uid");
        String random = httpServletRequest.getParameter("random");

        if(StringUtils.isEmpty(uid)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        if(StringUtils.isEmpty(random)){
            CommonResult.failed(jsonResult);
            return jsonResult;
        }

        if(!redisService.get(uid+"random").equals(random)){
            jsonResult.setMessage("非法订单！");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }

        if(Integer.parseInt(uid) == integralTransferOrder.getgUid()){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage("不能给自己赠送积分！");
            return jsonResult;
        }

        if(StringUtils.isEmpty(integralTransferOrder.getgUid())){
            jsonResult.setMessage("接受转增的用户不存在！");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }

        jsonResult = integralTransferOrderService.insertIntegralOrder(integralTransferOrder, uid, jsonResult);

        return jsonResult;
    }


    /**
     * 获取该用户所有订单
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectIntegralOrder(@RequestParam(value = "page", required = false) Integer page,
                                          @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort,
                                          @RequestParam(value = "start_time", required = false) String start_time, @RequestParam(value = "end_time", required = false) String end_time,
                                          HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        String uid = httpServletRequest.getHeader("uid");
        if (StringUtils.isEmpty(uid)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

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

        jsonResult = integralTransferOrderService.selectIntegralOrderByuid(uid, page, per_page, sort, start_time, end_time, jsonResult);

        return jsonResult;
    }



}
