package com.gljr.jifen.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.OnlineOrderService;
import com.gljr.jifen.service.ProductService;
import com.gljr.jifen.service.UserCreditsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/online-orders")
public class OnlineOrderController {

    @Autowired
    private OnlineOrderService onlineOrderService;

    @Autowired
    private ProductService productService;

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
    public JsonResult selectAllOnlineOrder(@RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort,
                                           @RequestParam(value = "start_time", required = false) String start_time, @RequestParam(value = "end_time", required = false) String end_time,
                                           HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {

        JsonResult jsonResult = new JsonResult();

        try {

//            onlineOrderService.deleteOnlineOrder();
            int uid;
            //获取头部商城用户id，如果不存在查询所有订单
            String uId = httpServletRequest.getHeader("uid");
            if(uId == null || uId.equals("")){
                uid = 0;
            }else{
                uid = Integer.parseInt(uId);
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

            PageHelper.startPage(page,per_page);
            List<OnlineOrder> onlineOrders = onlineOrderService.selectOnlineOrdersByUid(uid, sort, start_time, end_time);

            PageInfo pageInfo = new PageInfo(onlineOrders);

            for (OnlineOrder onlineOrder : onlineOrders){
                Product product = productService.selectProductById(onlineOrder.getPid());
                onlineOrder.setName(product.getName());
                onlineOrder.setDescription("数量：" + onlineOrder.getQuantity() + product.getUnit());
            }

            Map  map = new HashMap();
            map.put("data", onlineOrders);

            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        } catch (Exception e) {
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.DATABASE_FAILED);
        }

        return jsonResult;
    }


    /**
     * 查询未付款订单
     * @param page
     * @param per_page
     * @param sort
     * @param start_time
     * @param end_time
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/unpaid")
    @ResponseBody
    public JsonResult selectAllOnlineOrderNoPay(@RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort,
                                           @RequestParam(value = "start_time", required = false) String start_time, @RequestParam(value = "end_time", required = false) String end_time,
                                           HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {

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

            PageHelper.startPage(page,per_page);
            List<OnlineOrder> onlineOrders = onlineOrderService.selectOnlineOrdersByUidNotPay(uid, sort, start_time, end_time);

            PageInfo pageInfo = new PageInfo(onlineOrders);

            for (OnlineOrder onlineOrder : onlineOrders){
                Product product = productService.selectProductById(onlineOrder.getPid());
                onlineOrder.setName(product.getName());
                onlineOrder.setDescription("数量：" + onlineOrder.getQuantity() + product.getUnit());
            }

            Map  map = new HashMap();
            map.put("data", onlineOrders);

            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

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
                onlineOrder.setStatus(new Byte("0"));
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
                jsonResult.setMessage(onlineOrder.getTrxCode());

            }
        }catch (Exception e){
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.DATABASE_FAILED);
        }

        return jsonResult;
    }


    /**
     * 修改订单状态 1，已付款
     * @param id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @PutMapping(value = "/{id}/status/{status}")
    @ResponseBody
    public JsonResult updateOnlineOrder(@PathVariable(value = "id") String id, @PathVariable(value = "status") int status, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        String uid = httpServletRequest.getHeader("uid");

        try {
            if (uid.equals("NULL") || uid == null || uid.equals("")) {
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED_MESSAGE);
                return jsonResult;
            } else {
                if(status == 1) {
                    OnlineOrder onlineOrder = onlineOrderService.selectOnlineOrderById(id, Integer.parseInt(uid));

                    onlineOrder.setStatus((byte)status);
                    onlineOrderService.updateOnlineOrderById(onlineOrder);
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                    jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
                }else{
                    jsonResult.setMessage(GlobalConstants.OPERATION_FAILED);
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED_MESSAGE);
                    return jsonResult;
                }
            }
        }catch (Exception e){
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }


}
