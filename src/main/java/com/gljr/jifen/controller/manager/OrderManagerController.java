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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public JsonResult selectOnlineOrder(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = onlineOrderService.selectOnlineOrders(jsonResult);

        return  jsonResult;
    }



    /**
     * 查询所有线下订单
     * @return
     */
    @GetMapping(value = "/offline")
    @ResponseBody
    public JsonResult selectOfflineOrder(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = storeOfflineOrderService.selectOfflineOrders(jsonResult);

        return jsonResult;
    }


    /**
     * 获取所有积分转增订单
     * @return
     */
    @GetMapping(value = "/integral")
    @ResponseBody
    public JsonResult selectIntegralOrder(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = integralTransferOrderService.selectIntegralOrders(jsonResult);

        return jsonResult;
    }

}
