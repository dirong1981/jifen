package com.gljr.jifen.controller;


import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.IntegralTransferOrderService;
import com.gljr.jifen.service.MessageService;
import com.gljr.jifen.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTML;
import javax.validation.Valid;
import java.sql.Timestamp;
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

    @Autowired
    private MessageService messageService;

    @Autowired
    private TransactionService transactionService;


    @Autowired
    private StrUtil strUtil;


    /**
     * 添加一个积分转增订单
     * @param integralTransferOrder 订单
     * @param bindingResult
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "/integral-orders", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult insertIntegralOrder(@Valid IntegralTransferOrder integralTransferOrder, BindingResult bindingResult,
                                          HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        if(bindingResult.hasErrors()){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.NOTNULL);
            return jsonResult;
        }

        try {
            //添加一个积分转增订单
            String uid = httpServletRequest.getHeader("uid");
            integralTransferOrder.setUid(Integer.parseInt(uid));
            integralTransferOrder.setStatus(new Byte("0"));
            integralTransferOrder.setTrxCode(strUtil.randomKey(18));
            String title = "xx用户向您赠送" + integralTransferOrder.getIntegral() + "积分";
            integralTransferOrder.setTitle(title);
            integralTransferOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
            integralTransferOrder.setTrxId(343434);


            integralTransferOrderService.insertIntegralOrder(integralTransferOrder);


            //添加一个交易通用信息
            Transaction transaction = new Transaction();
            transaction.setCode(integralTransferOrder.getTrxCode());
            transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
            transaction.setIntegral(integralTransferOrder.getIntegral());
            transaction.setOwnerId(integralTransferOrder.getUid());
            transaction.setOwnerType(new Byte("1"));
            transaction.setType(3);

            transactionService.insertTransaction(transaction);


            //把通用交易信息id更新到积分转增表
            integralTransferOrder.setTrxId(transaction.getId());

            integralTransferOrderService.updateIntegralOrder(integralTransferOrder);

            //创建一个消息
            Message message = new Message();
            message.setReadStatus(new Byte("0"));
            message.setContent(title);
            message.setCreateTime(new Timestamp(System.currentTimeMillis()));
            message.setUid(integralTransferOrder.getgUid());

            messageService.insertMessage(message);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }
















//    /**
//     * 查询全部线下订单数据
//     *
//     * @return
//     */
//    @RequestMapping(value = "/integrals", method = RequestMethod.GET)
//    @ResponseBody
//    @AuthPassport(permission_code = "5")
//    public JsonResult selectAllOnlineOrder() {
//        JsonResult jr = new JsonResult();
//        try {
//            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//            List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderService.selectAllClass();
//            Map map = new HashMap();
//            map.put("data", integralTransferOrders);
//            jr.setItem(map);
//            return jr;
//        } catch (Exception e) {
//            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//        }
//
//        return jr;
//    }
//
//    /**
//     * 查询指定id线上订单数据
//     *
//     * @return
//     */
//    @RequestMapping(value = "/integrals/update/{id}", method = RequestMethod.GET)
//    @ResponseBody
//    @AuthPassport(permission_code = "5")
//    public JsonResult selectOnlineOrder(@PathVariable("id") String id) {
//        JsonResult jr = new JsonResult();
//        try {
//            IntegralTransferOrder integralTransferOrder = integralTransferOrderService.selectClass(Integer.parseInt(id));
//            Map map = new HashMap();
//            map.put("integrals", integralTransferOrder);
//            jr.setItem(map);
//            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//        } catch (Exception e) {
//            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//        }
//
//        return jr;
//    }
//
//    /**
//     * 查询指定id线上订单数据
//     *
//     * @return
//     */
//    @RequestMapping(value = "/integrals/update/status/{id}", method = RequestMethod.GET)
//    @ResponseBody
//    @AuthPassport(permission_code = "5")
//    public JsonResult updateStateOnlineOrder(@PathVariable("id") String id) {
//        JsonResult jr = new JsonResult();
//        try {
//            IntegralTransferOrder integralTransferOrder = integralTransferOrderService.selectClass(Integer.parseInt(id));
//            integralTransferOrder.setStatus(new Byte("1"));
//            integralTransferOrderService.updateClass(integralTransferOrder);
//            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//        } catch (Exception e) {
//            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//        }
//
//        return jr;
//    }
//    /**
//     * 查询带条件参数的线上订单数据
//     *
//     * @return
//     */
//    @RequestMapping(value = "/integrals/param", method = RequestMethod.GET)
//    @ResponseBody
//    @AuthPassport(permission_code = "5")
//    public JsonResult selectAllParamOnlineOrder(IntegralTransferOrderSearch integralTransferOrderSearch)  {
//        JsonResult jr = new JsonResult();
//        try {
//            ArrayList arrayList = new ArrayList();
//            List<IntegralTransferOrder> integralTransferOrders = null;
//            //判断只有是否处理存在
//            if (!integralTransferOrderSearch.getStatus().equals("") && integralTransferOrderSearch.getStatus() != null) {
//                 if (integralTransferOrderSearch.getLogmin().contains("-") != false && integralTransferOrderSearch.getLogmin() != null && integralTransferOrderSearch.getLogmax().contains("-") != false && integralTransferOrderSearch.getLogmax() != null) {
//                    integralTransferOrders = integralTransferOrderService.selectAllParamTimeClass(integralTransferOrderSearch);
//                    //判断起始时间是否存在
//                } else if (integralTransferOrderSearch.getLogmin().contains("-") != false && integralTransferOrderSearch.getLogmin() != null) {
//                    jr.setMessage("结束时间不能为空");
//                    jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//                    //判断结束时间是否存在
//                } else if (integralTransferOrderSearch.getLogmax().contains("-") != false && integralTransferOrderSearch.getLogmax() != null) {
//                    jr.setMessage("起始时间不能为空");
//                    jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//                    return null;
//                } else {
//                    //查询指定状态的
//                    integralTransferOrders = integralTransferOrderService.selectAllParamStatuClass(integralTransferOrderSearch.getStatus());
//                }
//                if (integralTransferOrders.size() > 0) {
//                    for (int i = 0; i < integralTransferOrders.size(); i++) {
//                        IntegralTransferOrder integralTransferOrder = integralTransferOrders.get(i);
//                        arrayList.add(integralTransferOrder);
//                    }
//                }
//            }
//            Map map = new HashMap();
//            map.put("data", arrayList);
//            jr.setItem(map);
//            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//            return jr;
//        } catch (Exception e) {
//            jr.setErrorCode(GlobalConstants.OPERATION_FAILED + e.getLocalizedMessage());
//        }
//
//        return jr;
//    }
//    /**
//     * 删除指定id线上订单数据
//     *
//     * @return
//     */
//    @RequestMapping(value = "/integrals/delete/{id}", method = RequestMethod.GET)
//    @ResponseBody
//    @AuthPassport(permission_code = "5")
//    public JsonResult deleteOnlineOrder(@PathVariable("id") String id) {
//        JsonResult jr = new JsonResult();
//        try {
//            integralTransferOrderService.deleteClass(Integer.parseInt(id));
//            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//        } catch (Exception e) {
//            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//        }
//
//        return jr;
//    }
}
