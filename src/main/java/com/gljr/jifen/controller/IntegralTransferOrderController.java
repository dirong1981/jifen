package com.gljr.jifen.controller;


import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.IntegralTransferOrderService;
import com.gljr.jifen.service.MessageService;
import com.gljr.jifen.service.TransactionService;
import com.gljr.jifen.service.UserCreditsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

@RequestMapping(value = "/v1/integral-orders")
public class IntegralTransferOrderController {

    @Autowired
    private IntegralTransferOrderService integralTransferOrderService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserCreditsService userCreditsService;




    /**
     * 添加一个积分转增订单
     * @param integralTransferOrder 订单
     * @param bindingResult
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @PostMapping
    @ResponseBody

    public JsonResult insertIntegralOrder(@Valid IntegralTransferOrder integralTransferOrder, BindingResult bindingResult,
                                          HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws RuntimeException{
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
            integralTransferOrder.setTrxCode(StrUtil.randomKey(18));
            String title = "xx用户向您赠送" + integralTransferOrder.getIntegral() + "积分";
            integralTransferOrder.setTitle(title);
            integralTransferOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
            integralTransferOrder.setTrxId(343434);


            //添加一个交易通用信息
            Transaction transaction = new Transaction();
            transaction.setCode(integralTransferOrder.getTrxCode());
            transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
            transaction.setIntegral(integralTransferOrder.getIntegral());
            transaction.setOwnerId(integralTransferOrder.getUid());
            transaction.setOwnerType(new Byte("1"));
            transaction.setType(3);
            transaction.setStatus(new Byte("0"));

            //扣除用户积分
            UserCredits userCredits = userCreditsService.selectUserCreditsByUid(integralTransferOrder.getUid()).get(0);
            int integral = userCredits.getIntegral() - transaction.getIntegral();
            if (integral >= 0) {
                userCredits.setIntegral(integral);
            }else{
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage(GlobalConstants.INTEGRAL_NOT_ENOUGH);
                return jsonResult;
            }



            //创建一个消息
            Message message = new Message();
            message.setReadStatus(new Byte("0"));
            message.setContent(integralTransferOrder.getTitle());
            message.setCreateTime(new Timestamp(System.currentTimeMillis()));
            message.setUid(integralTransferOrder.getgUid());

            integralTransferOrderService.insertIntegralOrder(integralTransferOrder, transaction, userCredits, message);


            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.DATABASE_FAILED);
        }

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
    public JsonResult selectIntegralOrder(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            String uid = httpServletRequest.getHeader("uid");
            if (uid == null || uid.equals("")) {
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            } else {

                List<IntegralTransferOrder> integralTransferOrders = integralTransferOrderService.selectIntegralOrderByuid(Integer.parseInt(uid));
                Map map = new HashMap();
                map.put("data", integralTransferOrders);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
                jsonResult.setItem(map);
            }
        }catch (Exception e){
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.DATABASE_FAILED);
        }

        return jsonResult;
    }



}
