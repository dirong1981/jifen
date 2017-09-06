package com.gljr.jifen.controller;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.IntegralTransferOrder;
import com.gljr.jifen.pojo.StoreInfo;
import com.gljr.jifen.pojo.StoreOfflineOrder;
import com.gljr.jifen.pojo.Transaction;
import com.gljr.jifen.service.StoreOfflineOrderService;
import com.gljr.jifen.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "*", maxAge = 3600)

@Controller

@RequestMapping(value = ("/jifen"))
public class StoreOfflineOrderController {

    @Autowired
    private StoreOfflineOrderService storeOfflineOrderService;


    /**
     * 添加线下订单
     *
     * @param storeOfflineOrder
     * @return
     */
    @RequestMapping(value = "/class/insertOfflineOrderAjax", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult insertOnlineOrder(@RequestBody StoreOfflineOrder storeOfflineOrder) {
        JsonResult jr = new JsonResult();
        try {
            storeOfflineOrder.setCreateTime(DateUtils.getTime());//创建时间
            storeOfflineOrder.setUpdateTime(DateUtils.getTime());//更新时间
            storeOfflineOrder.setStatus(new Byte("1"));//订单状态
            //根据商品的id查询商品订单
            StoreInfo storeInfo = storeOfflineOrderService.selectStoreInfoClass(storeOfflineOrder.getSiId());//查询商品信息
            //添加通用交易信息表
            Transaction transaction = getTransaction(storeOfflineOrder);
            //添加积分转增订单（用于交易时转积分给需要赠送的商家）
            insertIntegralTO(storeOfflineOrder, storeInfo,transaction);
            //添加线下订单
            storeOfflineOrderService.insertClass(storeOfflineOrder);
            Map map = new HashMap();
            map.put("data", storeOfflineOrder);
            jr.setItem(map);
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        } catch (Exception e) {
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED + e.getLocalizedMessage());
        }
        return jr;
    }

    /**
     * 查询全部线下订单数据
     *
     * @return
     */
    @RequestMapping(value = "/offlines", method = RequestMethod.GET)
    @ResponseBody
    public Map selectAllOnlineOrder() {
        JsonResult jr = new JsonResult();
        try {
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            List<StoreOfflineOrder> storeOfflineOrders = storeOfflineOrderService.selectAllClass();
            Map map = new HashMap();
            map.put("data", storeOfflineOrders);
            jr.setItem(map);
            return map;
        } catch (Exception e) {
            System.out.println(e);
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return null;
    }

    /**
     * 查询指定id线上订单数据
     *
     * @return
     */
    @RequestMapping(value = "/offlines/update/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult selectOnlineOrder(@PathVariable("id") String id) {
        JsonResult jr = new JsonResult();
        try {
            StoreOfflineOrder storeOfflineOrders = storeOfflineOrderService.selectClass(Integer.parseInt(id));
            Map map = new HashMap();
            map.put("offlines", storeOfflineOrders);
            jr.setItem(map);
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        } catch (Exception e) {
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jr;
    }


    /**
     * 更改指定id线上订单数据
     *
     * @return
     */
    @RequestMapping(value = "/class/updateStoreOfflineOrderAjax", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult updateOnlineOrder(@RequestBody StoreOfflineOrder storeOfflineOrder) {
        JsonResult jr = new JsonResult();
        try {
            StoreOfflineOrder storeOfflineOrders = storeOfflineOrderService.selectClass(storeOfflineOrder.getId());
            Map map = new HashMap();
            map.put("data", storeOfflineOrder);
            storeOfflineOrders.setUid(storeOfflineOrder.getUid() != null ? storeOfflineOrder.getUid() : storeOfflineOrder.getUid());//设置用户id
            storeOfflineOrders.setSiId(storeOfflineOrder.getSiId() != null ? storeOfflineOrder.getSiId() : storeOfflineOrder.getSiId());//设置商品id
            storeOfflineOrders.setIntegral(storeOfflineOrder.getIntegral() != null ? storeOfflineOrder.getIntegral(): storeOfflineOrders.getIntegral());//设置订单抵扣积分
            storeOfflineOrders.setTotalMoney(storeOfflineOrder.getTotalMoney() != null ? storeOfflineOrder.getTotalMoney() : storeOfflineOrders.getTotalMoney());//设置订单总金额
            storeOfflineOrders.setExtCash(storeOfflineOrder.getExtCash()!=null?storeOfflineOrder.getExtCash():storeOfflineOrders.getExtCash());//设置积分外支出金额
            storeOfflineOrders.setStatus(storeOfflineOrder.getStatus() != null ?storeOfflineOrder.getStatus() : storeOfflineOrders.getStatus());
            storeOfflineOrders.setUpdateTime(DateUtils.getTime());
            StoreInfo storeInfo = storeOfflineOrderService.selectStoreInfoClass(storeOfflineOrder.getSiId());//获取指定商品的详情信息
            //设置积分转换单
            IntegralTransferOrder integralTransferOrder = storeOfflineOrderService.selectIntegralTransferOrderClass(storeOfflineOrders.getTrxId());
            integralTransferOrder.setgUid(storeOfflineOrder.getSiId() != null ? Integer.parseInt(storeInfo.getSerialCode()) : integralTransferOrder.getgUid());//设置赠送用户id
            integralTransferOrder.setIntegral(storeOfflineOrder.getIntegral() != null ? storeOfflineOrder.getIntegral() : integralTransferOrder.getIntegral());//设置积分总额
            integralTransferOrder.setTitle("更新");//设置积分转赠单标题
            integralTransferOrder.setStatus(new Byte("1"));//设置积分转赠单状态
            storeOfflineOrderService.updateClass(storeOfflineOrders);
            storeOfflineOrderService.updateIntegralTransferOrderClass(integralTransferOrder);
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jr.setMessage(storeOfflineOrders + "");
            jr.setItem(map);
        } catch (Exception e) {
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED + e.getLocalizedMessage());
        }

        return jr;
    }

    /**
     * 查询指定id线上订单数据
     *
     * @return
     */
    @RequestMapping(value = "/offlines/update/status/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult updateStateOnlineOrder(@PathVariable("id") String id) {
        JsonResult jr = new JsonResult();
        try {
            StoreOfflineOrder storeOfflineOrders = storeOfflineOrderService.selectClass(Integer.parseInt(id));
            storeOfflineOrders.setStatus(new Byte("1"));
            storeOfflineOrderService.updateClass(storeOfflineOrders);
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        } catch (Exception e) {
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }
        return jr;
    }

    /**
     * 删除指定id线上订单数据
     *
     * @return
     */
    @RequestMapping(value = "/offlines/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult deleteOnlineOrder(@PathVariable("id") String id) {
        JsonResult jr = new JsonResult();
        try {
            StoreOfflineOrder storeOfflineOrder = storeOfflineOrderService.selectClass(Integer.parseInt(id));
            IntegralTransferOrder integralTransferOrder = storeOfflineOrderService.selectIntegralTransferOrderClass(storeOfflineOrder.getTrxId());
            storeOfflineOrderService.deleteIntegralTransferOrderClass(integralTransferOrder.getId());
            storeOfflineOrderService.deleteClass(storeOfflineOrder.getId());
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
//    @RequestMapping(value = "/offlines/param", method = RequestMethod.GET)
//    @ResponseBody
//    public Map selectAllParamOnlineOrder(OfflineOrderSearch offlineOrderSearch) {
//        JsonResult jr = new JsonResult();
//        try {
//            ArrayList arrayList = new ArrayList();
//            List<StoreOfflineOrder> storeOfflineOrders = null;
//            //判断只有是否处理存在
//            if (!offlineOrderSearch.getStatus().equals("") && offlineOrderSearch.getStatus() != null) {
//                if (offlineOrderSearch.getLogmin().contains("-") != false && offlineOrderSearch.getLogmin() != null && offlineOrderSearch.getLogmax().contains("-") != false && offlineOrderSearch.getLogmax() != null) {
//                    storeOfflineOrders = storeOfflineOrderService.selectAllParamTimeClass(offlineOrderSearch);
//                    //判断起始时间是否存在
//                } else if (offlineOrderSearch.getLogmin().contains("-") != false && offlineOrderSearch.getLogmin() != null) {
//                    jr.setMessage("结束时间不能为空");
//                    jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//                    //判断结束时间是否存在
//                } else if (offlineOrderSearch.getLogmax().contains("-") != false && offlineOrderSearch.getLogmax() != null) {
//                    jr.setMessage("起始时间不能为空");
//                    jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//                    return null;
//                } else {
//                    //查询指定状态的
//                    storeOfflineOrders = storeOfflineOrderService.selectAllParamStatuClass(offlineOrderSearch.getStatus());
//                }
//                if (storeOfflineOrders.size() > 0) {
//                    for (int i = 0; i < storeOfflineOrders.size(); i++) {
//                        StoreOfflineOrder storeOfflineOrder = storeOfflineOrders.get(i);
//                        arrayList.add(storeOfflineOrder);
//                    }
//                }
//            }
//            Map map = new HashMap();
//            map.put("data", arrayList);
//            jr.setItem(map);
//            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//            return map;
//        } catch (Exception e) {
//            jr.setErrorCode(GlobalConstants.OPERATION_FAILED + e.getLocalizedMessage());
//        }
//
//        return null;
//    }

    /**
     * 积分转增
     */

    private void insertIntegralTO(StoreOfflineOrder storeOfflineOrder, StoreInfo storeInfo, Transaction transaction) {
        Transaction transactions = storeOfflineOrderService.insertTransactionClass(transaction);//添加通用交易表
        IntegralTransferOrder integralTO = new IntegralTransferOrder();
        integralTO.setUid(storeOfflineOrder.getUid());//设置用户id
        String merchantId = storeInfo.getSerialCode();//得到商户id
        integralTO.setgUid(Integer.parseInt(merchantId));//设置赠送用户的id
        integralTO.setTrxId(transactions.getId());//通用交易表id
        integralTO.setTrxCode(transactions.getCode());//设置交易订单号
//        int sumIntegral = storeOfflineOrder.getIntegral() * onlineOrder.getQuantity();//通过积分和数量获取商品积分单总额
        integralTO.setIntegral(storeOfflineOrder.getTotalMoney());//设置积分单总额
        integralTO.setTitle("111");//积分转增单标题
        integralTO.setStatus(new Byte("1"));//设置积分转增单状态
        integralTO.setCreateTime(DateUtils.getTime());//设置积分转增单创建时间
        storeOfflineOrderService.insertIntegralTransferOrderClass(integralTO);//添加积分转增订单
        storeOfflineOrder.setTrxId(integralTO.getTrxId());//设置通用交易表ID
        storeOfflineOrder.setTrxCode(integralTO.getTrxCode());//设置交易单号
    }

    /**
     * 通用交易信息表
     *
     * @param
     */
    private Transaction getTransaction(StoreOfflineOrder storeOfflineOrder) {
        Transaction transaction = new Transaction();
        long randomStr = System.currentTimeMillis();//生成不重复的id
        transaction.setCode(randomStr + "");//设置交易单号
        transaction.setType(2);//设置交易类型
        transaction.setOwnerType(new Byte("1"));//设置账号拥有者类型
        transaction.setOwnerId(storeOfflineOrder.getUid());//
        transaction.setIntegral(storeOfflineOrder.getIntegral());//设置积分
        transaction.setCreateTime(DateUtils.getTime());
        return transaction;
    }
}
