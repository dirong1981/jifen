package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.dao.UserCreditsMapper;
import com.gljr.jifen.dao.UserExtInfoMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/manager/orders")
public class OrderManagerController extends BaseController {

    @Autowired
    private OnlineOrderService onlineOrderService;

    @Autowired
    private StoreOfflineOrderService storeOfflineOrderService;

    @Autowired
    private IntegralTransferOrderService integralTransferOrderService;



    /**
     * 获取所有在线订单
     * @return
     */
    @GetMapping(value = "/online")
    @ResponseBody
    public JsonResult selectOnlineOrder(@RequestParam(value = "page", required = false) Integer page,
                                        @RequestParam(value = "per_page", required = false) Integer per_page,
                                        @RequestParam(value = "trxCode", required = false) String trxCode,
                                        @RequestParam(value = "status", required = false) Integer status,
                                        @RequestParam(value = "beginTime", required = false) String beginTime,
                                        @RequestParam(value = "endTime", required = false) String endTime){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(page)){
            page = 1;
        }

        if(StringUtils.isEmpty(per_page)){
            per_page = 10;
        }

        //时间转换
        Date begin;
        Date end;
        try {
            if(!StringUtils.isEmpty(beginTime)){
                String pattern="yyyy-MM-dd";
                SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
                begin = dateFormat.parse(beginTime);
            }else {
                Calendar calendar = new GregorianCalendar(1970,01,01);
                begin = calendar.getTime();
            }


            if(StringUtils.isEmpty(endTime)){
                Calendar calendar = Calendar.getInstance();
                end = calendar.getTime();
            }else {
                String pattern="yyyy-MM-dd";
                SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
                end = dateFormat.parse(endTime);

                //加一天
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(end);
                calendar.add(calendar.DATE, 1);
                end = calendar.getTime();
            }

        }catch (Exception e){
            jsonResult.setMessage("时间设置错误");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }



        jsonResult = onlineOrderService.selectOnlineOrders(page, per_page, trxCode, status, begin, end, jsonResult);

        return  jsonResult;
    }



    /**
     * 查询所有线下订单
     * @return
     */
    @GetMapping(value = "/offline")
    @ResponseBody
    public JsonResult selectOfflineOrder(@RequestParam(value = "page", required = false) Integer page,
                                         @RequestParam(value = "per_page", required = false) Integer per_page,
                                         @RequestParam(value = "trxCode", required = false) String trxCode,
                                         @RequestParam(value = "status", required = false) Integer status,
                                         @RequestParam(value = "beginTime", required = false) String beginTime,
                                         @RequestParam(value = "endTime", required = false) String endTime){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(page)){
            page = 1;
        }

        if(StringUtils.isEmpty(per_page)){
            per_page = 10;
        }

        //时间转换
        Date begin;
        Date end;
        try {
            if(!StringUtils.isEmpty(beginTime)){
                String pattern="yyyy-MM-dd";
                SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
                begin = dateFormat.parse(beginTime);
            }else {
                Calendar calendar = new GregorianCalendar(1970,01,01);
                begin = calendar.getTime();
            }


            if(StringUtils.isEmpty(endTime)){
                Calendar calendar = Calendar.getInstance();
                end = calendar.getTime();
            }else {
                String pattern="yyyy-MM-dd";
                SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
                end = dateFormat.parse(endTime);

                //加一天
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(end);
                calendar.add(calendar.DATE, 1);
                end = calendar.getTime();
            }

        }catch (Exception e){
            jsonResult.setMessage("时间设置错误");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }

        jsonResult = storeOfflineOrderService.selectOfflineOrders(page, per_page, trxCode, status, begin, end, jsonResult);

        return jsonResult;
    }


    /**
     * 获取所有积分转增订单
     * @return
     */
    @GetMapping(value = "/integral")
    @ResponseBody
    public JsonResult selectIntegralOrder(@RequestParam(value = "page", required = false) Integer page,
                                          @RequestParam(value = "per_page", required = false) Integer per_page,
                                          @RequestParam(value = "trxCode", required = false) String trxCode,
                                          @RequestParam(value = "status", required = false) Integer status,
                                          @RequestParam(value = "beginTime", required = false) String beginTime,
                                          @RequestParam(value = "endTime", required = false) String endTime){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(page)){
            page = 1;
        }

        if(StringUtils.isEmpty(per_page)){
            per_page = 10;
        }

        //时间转换
        Date begin;
        Date end;
        try {
            if(!StringUtils.isEmpty(beginTime)){
                String pattern="yyyy-MM-dd";
                SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
                begin = dateFormat.parse(beginTime);
            }else {
                Calendar calendar = new GregorianCalendar(1970,01,01);
                begin = calendar.getTime();
            }


            if(StringUtils.isEmpty(endTime)){
                Calendar calendar = Calendar.getInstance();
                end = calendar.getTime();
            }else {
                String pattern="yyyy-MM-dd";
                SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
                end = dateFormat.parse(endTime);

                //加一天
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(end);
                calendar.add(calendar.DATE, 1);
                end = calendar.getTime();
            }

        }catch (Exception e){
            jsonResult.setMessage("时间设置错误");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }

        jsonResult = integralTransferOrderService.selectIntegralOrders(page, per_page, trxCode, status, begin, end, jsonResult);

        return jsonResult;
    }

}
