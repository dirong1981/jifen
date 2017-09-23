package com.gljr.jifen.controller;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.OnlineOrderService;
import com.gljr.jifen.service.TransactionService;
import com.gljr.jifen.service.UserCreditsService;
import com.gljr.jifen.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.security.spec.ECField;
import java.sql.Timestamp;
import java.util.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/online-orders")
public class OnlineOrderController {

    @Autowired
    private OnlineOrderService onlineOrderService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserCreditsService userCreditsService;


    /**
     * 查询所有订单，uid为0，查询所有订单，uid不为0，查询该用户订单
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectAllOnlineOrder(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        JsonResult jsonResult = new JsonResult();

        try {
            int uid;
            //获取头部商城用户id，如果不存在查询所有订单
            String uId = httpServletRequest.getHeader("uid");
            if(uId == null || uId.equals("")){
                uid = 0;
            }else{
                uid = Integer.parseInt(uId);
            }
            List<OnlineOrder> onlineOrders = onlineOrderService.selectOnlineOrdersByUid(uid);
            Map  map = new HashMap();
            map.put("data", onlineOrders);

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.DATABASE_FAILED);
        }

        return jsonResult;
    }


    /**
     * 添加一条在线订单
     * @param onlineOrder
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult insetOnlineOrder(OnlineOrder onlineOrder, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            String uid = httpServletRequest.getHeader("uid");
            if(uid == null || uid.equals("")){
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }else{
                onlineOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
                onlineOrder.setUid(Integer.parseInt(uid));
                onlineOrder.setStatus(new Byte("1"));
                onlineOrder.setTrxCode(StrUtil.randomKey(18));
                onlineOrder.setTrxId(111);


                //添加一条通用交易信息
                Transaction transaction = new Transaction();
                transaction.setType(2);
                transaction.setOwnerType(new Byte("1"));
                transaction.setOwnerId(Integer.parseInt(uid));
                transaction.setIntegral(onlineOrder.getIntegral());
                transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
                transaction.setCode(onlineOrder.getTrxCode());
                transaction.setStatus(new Byte("0"));


                //扣除用户积分
                UserCredits userCredits = userCreditsService.selectUserCreditsByUid(onlineOrder.getUid()).get(0);
                int integral = userCredits.getIntegral() - transaction.getIntegral();
                if (integral >= 0) {
                    userCredits.setIntegral(integral);
                }else{
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                    jsonResult.setMessage(GlobalConstants.INTEGRAL_NOT_ENOUGH);
                    return jsonResult;
                }


                onlineOrderService.insertOnlineOrder(onlineOrder, transaction, userCredits);


                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

            }
        }catch (Exception e){
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.DATABASE_FAILED);
        }

        return jsonResult;
    }




}
