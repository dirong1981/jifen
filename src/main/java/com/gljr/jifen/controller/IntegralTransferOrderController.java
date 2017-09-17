package com.gljr.jifen.controller;


import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.IntegralTransferOrder;
import com.gljr.jifen.pojo.IntegralTransferOrderSearch;
import com.gljr.jifen.pojo.StoreOfflineOrder;
import com.gljr.jifen.service.IntegralTransferOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "*", maxAge = 3600)

@Controller

@RequestMapping(value = "/v1")
public class IntegralTransferOrderController {

    @Autowired
    private IntegralTransferOrderService integralTransferOrderService;
    /**
     * 查询全部线下订单数据
     *
     * @return
     */
    @RequestMapping(value = "/integrals", method = RequestMethod.GET)
    @ResponseBody
    @AuthPassport(permission_code = "5")
    public JsonResult selectAllOnlineOrder() {
        JsonResult jr = new JsonResult();
        try {
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderService.selectAllClass();
            Map map = new HashMap();
            map.put("data", integralTransferOrders);
            jr.setItem(map);
            return jr;
        } catch (Exception e) {
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jr;
    }

    /**
     * 查询指定id线上订单数据
     *
     * @return
     */
    @RequestMapping(value = "/integrals/update/{id}", method = RequestMethod.GET)
    @ResponseBody
    @AuthPassport(permission_code = "5")
    public JsonResult selectOnlineOrder(@PathVariable("id") String id) {
        JsonResult jr = new JsonResult();
        try {
            IntegralTransferOrder integralTransferOrder = integralTransferOrderService.selectClass(Integer.parseInt(id));
            Map map = new HashMap();
            map.put("integrals", integralTransferOrder);
            jr.setItem(map);
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        } catch (Exception e) {
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jr;
    }

    /**
     * 查询指定id线上订单数据
     *
     * @return
     */
    @RequestMapping(value = "/integrals/update/status/{id}", method = RequestMethod.GET)
    @ResponseBody
    @AuthPassport(permission_code = "5")
    public JsonResult updateStateOnlineOrder(@PathVariable("id") String id) {
        JsonResult jr = new JsonResult();
        try {
            IntegralTransferOrder integralTransferOrder = integralTransferOrderService.selectClass(Integer.parseInt(id));
            integralTransferOrder.setStatus(new Byte("1"));
            integralTransferOrderService.updateClass(integralTransferOrder);
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        } catch (Exception e) {
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jr;
    }
    /**
     * 查询带条件参数的线上订单数据
     *
     * @return
     */
    @RequestMapping(value = "/integrals/param", method = RequestMethod.GET)
    @ResponseBody
    @AuthPassport(permission_code = "5")
    public JsonResult selectAllParamOnlineOrder(IntegralTransferOrderSearch integralTransferOrderSearch)  {
        JsonResult jr = new JsonResult();
        try {
            ArrayList arrayList = new ArrayList();
            List<IntegralTransferOrder> integralTransferOrders = null;
            //判断只有是否处理存在
            if (!integralTransferOrderSearch.getStatus().equals("") && integralTransferOrderSearch.getStatus() != null) {
                 if (integralTransferOrderSearch.getLogmin().contains("-") != false && integralTransferOrderSearch.getLogmin() != null && integralTransferOrderSearch.getLogmax().contains("-") != false && integralTransferOrderSearch.getLogmax() != null) {
                    integralTransferOrders = integralTransferOrderService.selectAllParamTimeClass(integralTransferOrderSearch);
                    //判断起始时间是否存在
                } else if (integralTransferOrderSearch.getLogmin().contains("-") != false && integralTransferOrderSearch.getLogmin() != null) {
                    jr.setMessage("结束时间不能为空");
                    jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
                    //判断结束时间是否存在
                } else if (integralTransferOrderSearch.getLogmax().contains("-") != false && integralTransferOrderSearch.getLogmax() != null) {
                    jr.setMessage("起始时间不能为空");
                    jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
                    return null;
                } else {
                    //查询指定状态的
                    integralTransferOrders = integralTransferOrderService.selectAllParamStatuClass(integralTransferOrderSearch.getStatus());
                }
                if (integralTransferOrders.size() > 0) {
                    for (int i = 0; i < integralTransferOrders.size(); i++) {
                        IntegralTransferOrder integralTransferOrder = integralTransferOrders.get(i);
                        arrayList.add(integralTransferOrder);
                    }
                }
            }
            Map map = new HashMap();
            map.put("data", arrayList);
            jr.setItem(map);
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            return jr;
        } catch (Exception e) {
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED + e.getLocalizedMessage());
        }

        return jr;
    }
    /**
     * 删除指定id线上订单数据
     *
     * @return
     */
    @RequestMapping(value = "/integrals/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    @AuthPassport(permission_code = "5")
    public JsonResult deleteOnlineOrder(@PathVariable("id") String id) {
        JsonResult jr = new JsonResult();
        try {
            integralTransferOrderService.deleteClass(Integer.parseInt(id));
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        } catch (Exception e) {
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jr;
    }
}
