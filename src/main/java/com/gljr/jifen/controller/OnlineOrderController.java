package com.gljr.jifen.controller;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.OnlineOrderService;
import com.gljr.jifen.service.TransactionService;
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
@RequestMapping(value = "/v1")
public class OnlineOrderController {

    @Autowired
    private OnlineOrderService onlineOrderService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private StrUtil strUtil;


    /**
     * 查询所有订单，uid为0，查询所有订单，uid不为0，查询该用户订单
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/online-orders", method = RequestMethod.GET)
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
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
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
    @RequestMapping(value = "/online-orders", method = RequestMethod.POST)
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
                onlineOrder.setTrxCode(strUtil.randomKey(18));
                onlineOrder.setTrxId(111);
                onlineOrderService.insertOnlineOrder(onlineOrder);

                //添加一条通用交易信息
                Transaction transaction = new Transaction();
                transaction.setType(2);
                transaction.setOwnerType(new Byte("1"));
                transaction.setOwnerId(Integer.parseInt(uid));
                transaction.setIntegral(onlineOrder.getIntegral());
                transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
                transaction.setCode(onlineOrder.getTrxCode());

                transactionService.insertTransaction(transaction);

                onlineOrder.setTrxId(transaction.getId());
                onlineOrderService.updateOnlineOrderById(onlineOrder);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

            }
        }catch (Exception e){
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }

















//    /**
//     * 添加线上订单
//     *
//     * @param onlineOrder
//     * @return
//     */
//    @RequestMapping(value = "/onlines/insert", method = RequestMethod.POST)
//    @ResponseBody
//    @AuthPassport(permission_code = "5")
//    public JsonResult insertOnlineOrder(@RequestBody OnlineOrder onlineOrder) {
//        JsonResult jr = new JsonResult();
//        try {
//            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//            onlineOrder.setCreateTime(getTime());//创建时间
//            onlineOrder.setUpdateTime(getTime());//更新时间
//            onlineOrder.setStatus(new Byte("1"));//订单状态
//            onlineOrder.setIntegral(onlineOrder.getIntegral() * onlineOrder.getQuantity());
//            //根据商品的id查询商品订单
//            Product product = onlineOrderService.selectProductClass(onlineOrder.getPid());//查询商品信息
//            //创建通用交易信息表
//            Transaction transaction = getTransaction(onlineOrder);
//            //添加积分转增订单（用于交易时转积分给需要赠送的商家）
//            insertIntegralTO(onlineOrder, product,transaction);
//            onlineOrderService.insertClass(onlineOrder);
////            Map map = new HashMap();
////            map.put("data", onlineOrder);
////            map.put("product", product);
////            jr.setItem(map);
//        } catch (Exception e) {
//            jr.setErrorCode(GlobalConstants.OPERATION_FAILED+e.getLocalizedMessage());
//        }
//        return jr;
//    }
//
//
//    /**
//     * 查询带条件参数的线上订单数据
//     *
//     * @return
//     */
//    @RequestMapping(value = "/onlines/param", method = RequestMethod.GET)
//    @ResponseBody
//    @AuthPassport(permission_code = "5")
//    public JsonResult selectAllParamOnlineOrder(OnlineOrderSearch onlineSearch)  {
//        JsonResult jr = new JsonResult();
//        try {
//            ArrayList arrayList = new ArrayList();
//            List<OnlineOrder> onlineOrders = null;
//            //判断只有是否处理存在
//            if (!onlineSearch.getStatus().equals("") && onlineSearch.getStatus() != null) {
//                 if (onlineSearch.getLogmin().contains("-") != false && onlineSearch.getLogmin() != null && onlineSearch.getLogmax().contains("-") != false && onlineSearch.getLogmax() != null) {
//                    onlineOrders = onlineOrderService.selectAllParamTimeClass(onlineSearch);
//                    //判断起始时间是否存在
//                } else if (onlineSearch.getLogmin().contains("-") != false && onlineSearch.getLogmin() != null) {
//                    jr.setMessage("结束时间不能为空");
//                    jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//                    //判断结束时间是否存在
//                } else if (onlineSearch.getLogmax().contains("-") != false && onlineSearch.getLogmax() != null) {
//                    jr.setMessage("起始时间不能为空");
//                    jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//                    return null;
//                } else {
//                    //查询指定状态的
//                    onlineOrders = onlineOrderService.selectAllParamStatuClass(onlineSearch.getStatus());
//                }
//                if (onlineOrders.size() > 0) {
//                    for (int i = 0; i < onlineOrders.size(); i++) {
//                        OnlineOrder onlineOrder1 = onlineOrders.get(i);
//                        arrayList.add(onlineOrder1);
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
//
//    /**
//     * 查询指定id线上订单数据
//     *
//     * @return
//     */
//    @RequestMapping(value = "/onlines/update/{id}", method = RequestMethod.GET)
//    @ResponseBody
//    @AuthPassport(permission_code = "5")
//    public JsonResult selectOnlineOrder(@PathVariable("id") String id) {
//        JsonResult jr = new JsonResult();
//        try {
//            OnlineOrder onlineOrders = onlineOrderService.selectClass(Integer.parseInt(id));
//            Map map = new HashMap();
//            map.put("onlines", onlineOrders);
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
//     * 删除指定id线上订单数据
//     *
//     * @return
//     */
//    @RequestMapping(value = "/onlines/delete/{id}", method = RequestMethod.GET)
//    @ResponseBody
//    @AuthPassport(permission_code = "5")
//    public JsonResult deleteOnlineOrder(@PathVariable("id") String id) {
//        JsonResult jr = new JsonResult();
//        try {
//            OnlineOrder onlineOrder = onlineOrderService.selectClass(Integer.parseInt(id));
//            IntegralTransferOrder integralTransferOrder = onlineOrderService.selectIntegralTransferOrderClass(onlineOrder.getTrxId());
//            onlineOrderService.deleteIntegralTransferOrderClass(integralTransferOrder.getId());
//            onlineOrderService.deleteClass(onlineOrder.getId());
//            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//        } catch (Exception e) {
//            System.out.println(e);
//            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//        }
//
//        return jr;
//    }
//
//    /**
//     * 更改指定id线上订单数据
//     *
//     * @return
//     */
//    @RequestMapping(value = "/onlines/update", method = RequestMethod.POST)
//    @ResponseBody
//    @AuthPassport(permission_code = "5")
//    public JsonResult updateOnlineOrder( HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
//        JsonResult jr = new JsonResult();
//        try {
//            OnlineOrder onlineOrders = onlineOrderService.selectClass(Integer.parseInt(httpServletRequest.getParameter("id")));
//            Map map = new HashMap();
//            String statuses = httpServletRequest.getParameter("status");
//            String express = httpServletRequest.getParameter("express");
//            String expressId = httpServletRequest.getParameter("expressId");
//
//
//            onlineOrders.setStatus(new Byte(statuses));
////            onlineOrders.setUid(onlineOrder.getUid() != null ? onlineOrder.getUid() : onlineOrders.getUid());//设置用户id
////            onlineOrders.setPid(onlineOrder.getPid() != null ? onlineOrder.getPid() : onlineOrders.getPid());//设置商品id
////            onlineOrders.setIntegral(onlineOrder.getIntegral() != null ? onlineOrder.getIntegral() * onlineOrder.getQuantity() : onlineOrders.getIntegral());//设置订单积分
////            onlineOrders.setQuantity(onlineOrder.getQuantity() != null ? onlineOrder.getQuantity() : onlineOrders.getQuantity());//设置商品订单积分
////            onlineOrders.setStatus(onlineOrder.getStatus() != null ? onlineOrder.getStatus() : onlineOrders.getStatus());//设置商品订单积分
////            onlineOrders.setUpdateTime(getTime());
////            Product product = onlineOrderService.selectProductClass(onlineOrders.getPid());//获取指定商品的详情信息
////            //设置积分转换单
////            IntegralTransferOrder integralTransferOrder = onlineOrderService.selectIntegralTransferOrderClass(onlineOrders.getTrxId());
////            integralTransferOrder.setgUid(onlineOrder.getPid() != null ? product.getSiId() : integralTransferOrder.getgUid());//设置赠送用户id
////            integralTransferOrder.setIntegral(onlineOrder.getIntegral() != null ? onlineOrder.getIntegral() : integralTransferOrder.getIntegral());//设置积分总额
////            integralTransferOrder.setTitle("更新");//设置积分转赠单标题
////            integralTransferOrder.setStatus(new Byte("1"));//设置积分转赠单状态
//            onlineOrderService.updateClass(onlineOrders);
////            onlineOrderService.updateIntegralTransferOrderClass(integralTransferOrder);
//            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//            jr.setItem(map);
//        } catch (Exception e) {
//            jr.setErrorCode(GlobalConstants.OPERATION_FAILED + e.getLocalizedMessage());
//        }
//
//        return jr;
//    }
//
//
//    /**
//     * 积分转增
//     */
//
//    private void insertIntegralTO(OnlineOrder onlineOrder, Product product, Transaction transaction) {
//        IntegralTransferOrder integralTO = new IntegralTransferOrder();
//        integralTO.setUid(onlineOrder.getUid());//设置用户id
//        int merchantId = product.getSiId();//得到商户id
//        integralTO.setgUid(merchantId);//设置赠送用户的id
//        Transaction transactions = onlineOrderService.insertTransactionClass(transaction);//添加通用交易表
//        integralTO.setTrxId(transactions.getId());//通用交易表id
//        integralTO.setTrxCode(transactions.getCode());//设置交易订单号
//        int sumIntegral = onlineOrder.getIntegral() * onlineOrder.getQuantity();//通过积分和数量获取商品积分单总额
//        integralTO.setIntegral(sumIntegral);//设置积分单总额
//        integralTO.setTitle("111");//积分转增单标题
//        integralTO.setStatus(new Byte("1"));//设置积分转增单状态
//        integralTO.setCreateTime(getTime());//设置积分转增单创建时间
//        onlineOrderService.insertIntegralTransferOrderClass(integralTO);//添加积分转增订单
//        onlineOrder.setTrxId(integralTO.getTrxId());//设置通用交易表ID
//        onlineOrder.setTrxCode(integralTO.getTrxCode());//设置交易单号
//    }
//
//    /**
//     * 通用交易信息表
//     * @param onlineOrder
//     */
//    private Transaction getTransaction(OnlineOrder onlineOrder){
//        Transaction transaction =  new Transaction();
//        long randomStr = System.currentTimeMillis();//生成不重复的id
//        transaction.setCode(randomStr+"");//设置交易单号
//        transaction.setType(2);//设置交易类型
//        transaction.setOwnerType(new Byte("1"));//设置账号拥有者类型
//        transaction.setOwnerId(onlineOrder.getUid());
//        transaction.setIntegral(onlineOrder.getIntegral()*onlineOrder.getQuantity());
//        transaction.setCreateTime(DateUtils.getTime());
//        return transaction;
//    }
//    public Date getTime() {
//        Date date = new Date();
//        return date;
//    }
}
