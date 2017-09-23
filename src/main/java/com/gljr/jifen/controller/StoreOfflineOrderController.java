package com.gljr.jifen.controller;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.StoreOfflineOrderService;
import com.gljr.jifen.service.TransactionService;
import com.gljr.jifen.service.UserCreditsService;
import com.gljr.jifen.util.DateUtils;
import com.qiniu.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "*", maxAge = 3600)

@Controller

@RequestMapping(value = "/v1/offline-orders")
public class StoreOfflineOrderController {

    @Autowired
    private StoreOfflineOrderService storeOfflineOrderService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserCreditsService userCreditsService;


    /**
     * 添加一条线下订单，添加一条通用交易信息
     * @param storeOfflineOrder
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ResponseBody
    @Transactional
    public JsonResult insetOfflineOrder(StoreOfflineOrder storeOfflineOrder, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            String uid = httpServletRequest.getHeader("uid");
            if(uid == null || uid.equals("")){
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }else{
                storeOfflineOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
                storeOfflineOrder.setUid(Integer.parseInt(uid));
                storeOfflineOrder.setStatus(new Byte("1"));
                storeOfflineOrder.setTrxCode(StrUtil.randomKey(18));
                storeOfflineOrder.setTrxId(111);


                //添加一条通用交易信息
                Transaction transaction = new Transaction();
                transaction.setType(1);
                transaction.setOwnerType(new Byte("1"));
                transaction.setOwnerId(Integer.parseInt(uid));
                transaction.setIntegral(storeOfflineOrder.getIntegral());
                transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
                transaction.setCode(storeOfflineOrder.getTrxCode());
                transaction.setStatus(new Byte("0"));


                //扣除用户积分
                UserCredits userCredits = userCreditsService.selectUserCreditsByUid(storeOfflineOrder.getUid()).get(0);
                int integral = userCredits.getIntegral() - transaction.getIntegral();
                if (integral >= 0) {
                    userCredits.setIntegral(integral);
                }else{
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                    jsonResult.setMessage(GlobalConstants.INTEGRAL_NOT_ENOUGH);
                    return jsonResult;
                }


                storeOfflineOrderService.insertOfflineOrder(storeOfflineOrder, transaction, userCredits);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

            }
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.DATABASE_FAILED);
        }

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
    public JsonResult allOfflineOrder(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try{
            String uid = httpServletRequest.getHeader("uid");
            if(uid == null || uid.equals("")){
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }else{

                List<StoreOfflineOrder> storeOfflineOrders = storeOfflineOrderService.selectAllOfflineOrderByUid(Integer.parseInt(uid));
                Map map = new HashMap();
                map.put("data", storeOfflineOrders);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setItem(map);
            }
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return  jsonResult;
    }



}
