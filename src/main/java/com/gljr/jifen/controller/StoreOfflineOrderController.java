package com.gljr.jifen.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.*;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.common.dtchain.GouliUserInfo;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.exception.ApiServerException;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.*;
import com.gljr.jifen.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*", maxAge = 3600)

@Controller

@RequestMapping(value = "/v1/offline-orders")
public class StoreOfflineOrderController extends BaseController{

    @Autowired
    private StoreOfflineOrderService storeOfflineOrderService;


    /**
     * 添加一条线下订单，添加一条通用交易信息
     * @param storeOfflineOrder
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult insetOfflineOrder(StoreOfflineOrder storeOfflineOrder, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        String uid = httpServletRequest.getHeader("uid");
        if(StringUtils.isEmpty(uid)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        jsonResult = storeOfflineOrderService.insertOfflineOrder(storeOfflineOrder, uid, jsonResult);
        return jsonResult;
    }


    /**
     * 获取某个用户线下订单
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult allOfflineOrder(@RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort,
                                      @RequestParam(value = "start_time", required = false) String start_time, @RequestParam(value = "end_time", required = false) String end_time,
                                      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        String uid = httpServletRequest.getHeader("uid");
        if(StringUtils.isEmpty(uid)){
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

        jsonResult = storeOfflineOrderService.selectOfflineOrderByUid(uid, page, per_page, sort, start_time, end_time, jsonResult);

        return  jsonResult;
    }



}
