package com.gljr.jifen.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/online-orders")
public class OnlineOrderController {

    @Autowired
    private OnlineOrderService onlineOrderService;


    /**
     * 查询用户所有在线订单
     * @param page
     * @param per_page
     * @param sort
     * @param start_time
     * @param end_time
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectAllOnlineOrder(@RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort,
                                           @RequestParam(value = "start_time", required = false) String start_time, @RequestParam(value = "end_time", required = false) String end_time,
                                           HttpServletRequest httpServletRequest) {

        JsonResult jsonResult = new JsonResult();

        String uid = httpServletRequest.getHeader("uid");

        if(StringUtils.isEmpty(uid)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        if(StringUtils.isEmpty(start_time)){
            start_time = "0";
        }

        if(StringUtils.isEmpty(end_time)){
            end_time = "0";
        }

        if(StringUtils.isEmpty(sort) || sort > 4 || sort < 0){
            sort = 0;
        }

        jsonResult = onlineOrderService.selectOnlineOrdersByUid(uid, sort, start_time, end_time, jsonResult);

        return jsonResult;
    }


    /**
     * 查询用户未付款订单
     * @param page
     * @param per_page
     * @param sort
     * @param start_time
     * @param end_time
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/unpaid")
    @ResponseBody
    public JsonResult selectAllOnlineOrderNoPay(@RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort,
                                           @RequestParam(value = "start_time", required = false) String start_time, @RequestParam(value = "end_time", required = false) String end_time,
                                           HttpServletRequest httpServletRequest) {

        JsonResult jsonResult = new JsonResult();

        String uid = httpServletRequest.getHeader("uid");

        if (StringUtils.isEmpty(uid)) {
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        if(StringUtils.isEmpty(start_time)){
            start_time = "0";
        }

        if(StringUtils.isEmpty(end_time)){
            end_time = "0";
        }

        if(StringUtils.isEmpty(sort) || sort > 4 || sort < 0){
            sort = 0;
        }

        onlineOrderService.selectOnlineOrdersByUidNotPay(uid, sort, start_time, end_time, jsonResult);

        return jsonResult;
    }



    /**
     * 添加一条在线订单，尚未付款
     * @param onlineOrder
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult insetOnlineOrder(OnlineOrder onlineOrder, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        String uid = httpServletRequest.getHeader("uid");

        if(StringUtils.isEmpty(uid)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        jsonResult = onlineOrderService.insertOnlineOrder(onlineOrder, uid, jsonResult);

        return jsonResult;
    }


    /**
     * 判断订单状态是否为未付款，更新订单和通用交易状态为已付款，并修改用户积分
     * @param trxCode
     * @param httpServletRequest
     * @return
     */
    @PutMapping(value = "/{trxCode}")
    @ResponseBody
    public JsonResult updateOnlineOrder(@PathVariable(value = "trxCode") String trxCode, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        String uid = httpServletRequest.getHeader("uid");

        if(StringUtils.isEmpty(uid)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }
        if(StringUtils.isEmpty(trxCode)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = onlineOrderService.updateOnlineOrderByTrxCode(trxCode, uid, jsonResult);

        return jsonResult;
    }


    /**
     * 取消一个订单，更新订单，通用交易表，恢复商品库存和销量
     * @param trxCode
     * @param httpServletRequest
     * @return
     */
    @PutMapping(value = "/{trxCode}/cancel")
    @ResponseBody
    public JsonResult cancelOnlineOrder(@PathVariable(value = "trxCode") String trxCode, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        String uid = httpServletRequest.getHeader("uid");
        if(StringUtils.isEmpty(uid)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        if(StringUtils.isEmpty(trxCode)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = onlineOrderService.cancelOnlineOrderByTrxCode(trxCode, uid, jsonResult);


        return jsonResult;
    }

}
