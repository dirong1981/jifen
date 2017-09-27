package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.pojo.IntegralTransferOrder;
import com.gljr.jifen.pojo.OnlineOrder;
import com.gljr.jifen.pojo.StoreInfo;
import com.gljr.jifen.pojo.StoreOfflineOrder;
import com.gljr.jifen.service.IntegralTransferOrderService;
import com.gljr.jifen.service.OnlineOrderService;
import com.gljr.jifen.service.StoreInfoService;
import com.gljr.jifen.service.StoreOfflineOrderService;
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

    @Autowired
    private StoreInfoService storeInfoService;


    /**
     * 获取所有在线订单
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping(value = "/online")
    @ResponseBody
    public JsonResult selectOnlineOrder(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try{
            List<OnlineOrder> onlineOrders = onlineOrderService.selectOnlineOrders();


            Map map = new HashMap();
            map.put("data", onlineOrders);

            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setItem(map);
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return  jsonResult;
    }


    /**
     * 删除一个订单，把订单状态修改为2
     * @param id
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping(value = "/online/{id}")
    @ResponseBody
    public JsonResult deleteOnlineOrderById(@PathVariable(value = "id") int id,  HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            OnlineOrder onlineOrder = onlineOrderService.selectOnlineOrderById(id);
            onlineOrder.setStatus((byte)2);
            onlineOrderService.updateOnlineOrderById(onlineOrder);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
        }catch (Exception e){
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jsonResult;
    }


    /**
     * 查询所有线下订单
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping(value = "/offline")
    @ResponseBody
    public JsonResult selectOfflineOrder(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();


        try {
            Map map = new HashMap();

            List<StoreOfflineOrder> storeOfflineOrders = storeOfflineOrderService.selectOfflineOrders();
            if(storeOfflineOrders != null && storeOfflineOrders.size() != 0){
                for (StoreOfflineOrder storeOfflineOrder : storeOfflineOrders){
                    StoreInfo storeInfo = storeInfoService.selectStoreInfoById(storeOfflineOrder.getSiId());
                    storeOfflineOrder.setName(storeInfo.getName());
                }
            }

            map.put("data", storeOfflineOrders);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            jsonResult.setItem(map);

        } catch (Exception e) {

            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }


    /**
     * 获取所有积分转增订单
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/integral")
    @ResponseBody
    public JsonResult selectIntegralOrder(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderService.selectIntegralOrders();

            Map map = new HashMap();
            map.put("data", integralTransferOrders);

            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setItem(map);
        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }

}
