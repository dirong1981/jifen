package com.gljr.jifen.controller.manager;


import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/manager/moduleaggregations")
public class ModuleAggregationManagerController {


    @Autowired
    private ModuleAggregationService moduleAggregationService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreInfoService storeInfoService;

    @Autowired
    private RedisService redisService;

    /**
     * 添加一个聚合页
     * @param moduleAggregation
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult insertModuleAggregations(ModuleAggregation moduleAggregation, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        String aid = httpServletRequest.getHeader("aid");
        if(StringUtils.isEmpty(aid)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        moduleAggregation.setCreateTime(new Timestamp(System.currentTimeMillis()));
        moduleAggregation.setManagerId(Integer.parseInt(aid));
        moduleAggregation.setLinkCode(StrUtil.randomKey(16));

        jsonResult = moduleAggregationService.insertModuleAggregation(moduleAggregation, jsonResult);

        return jsonResult;
    }


    /**
     * 获取所有聚合页
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectModuleAggregations(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = moduleAggregationService.selectModuleAggregations(jsonResult);

        return jsonResult;
    }


    /**
     * 查询启用的聚合页
     * @return
     */
    @GetMapping("/enabled")
    @ResponseBody
    public JsonResult selectModuleAggregationsByStatus(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = moduleAggregationService.selectModuleAggregationByEnabled(jsonResult);

        return jsonResult;
    }


    /**
     * 下架一个聚合页
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}/rejection")
    @ResponseBody
    public JsonResult stopModuleAggregation(@PathVariable("id") Integer id){
        JsonResult jsonResult = new JsonResult();

        try {
            ModuleAggregation moduleAggregation = moduleAggregationService.selectModuleAggregationById(id);

            if(ValidCheck.validPojo(moduleAggregation)) {
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            moduleAggregation.setStatus(DBConstants.CategoryStatus.INACTIVE.getCode());

            //删除缓存
            this.redisService.evict(moduleAggregation.getLinkCode(), moduleAggregation.getLinkCode()+"1",
                    moduleAggregation.getLinkCode()+"2", moduleAggregation.getLinkCode()+"3", moduleAggregation.getLinkCode()+"4");

            moduleAggregationService.updateModuleAggregationById(moduleAggregation);

            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }


        return jsonResult;
    }


    /**
     * 生效一个聚合页
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}/acceptance")
    @ResponseBody
    public JsonResult startModuleAggregation(@PathVariable("id") Integer id){
        JsonResult jsonResult = new JsonResult();

        try {
            ModuleAggregation moduleAggregation = moduleAggregationService.selectModuleAggregationById(id);

            //判断是否找到该聚合页
            if(ValidCheck.validPojo(moduleAggregation)) {
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            //查找聚合页下包含哪些商品和商户
            List<ModuleAggregationProduct> moduleAggregationProducts = moduleAggregationService.selectModuleAggregationProductByAggregationId(moduleAggregation.getId());


            //如果聚合页下没有添加商品或者商户，不能启用聚合页
            if(ValidCheck.validList(moduleAggregationProducts)) {

                CommonResult.noSelected(jsonResult);
                return jsonResult;

            }

            //循环获取商户或商品信息
            List<AggregationProduct> aggregationProducts = new ArrayList<>();
            for (ModuleAggregationProduct moduleAggregationProduct : moduleAggregationProducts) {
                AggregationProduct aggregationProduct = new AggregationProduct();
                if (moduleAggregationProduct.getType() == 1) {
                    //线上商品
                    Product product = productService.selectProductById(moduleAggregationProduct.getProductId());
                    if (!ValidCheck.validPojo(product)) {

                        aggregationProduct.setId(product.getId());
                        aggregationProduct.setIntegral(product.getIntegral());
                        aggregationProduct.setName(product.getName());
                        aggregationProduct.setPrice(product.getPrice());
                        aggregationProduct.setType(DBConstants.CategoryType.PRODUCT.getCode());
                        aggregationProduct.setLogo_key(product.getLogoKey());
                        aggregationProduct.setSales(product.getSales());

                        aggregationProducts.add(aggregationProduct);
                    }
                } else {
                    //店铺
                    StoreInfo storeInfo = storeInfoService.selectStoreInfoById(moduleAggregationProduct.getStoreId());
                    if (!ValidCheck.validPojo(storeInfo)) {
                        aggregationProduct.setLogo_key(storeInfo.getLogoKey());
                        aggregationProduct.setType(DBConstants.CategoryType.STORE.getCode());
                        aggregationProduct.setName(storeInfo.getName());
                        aggregationProduct.setId(storeInfo.getId());
                        aggregationProduct.setAddress(storeInfo.getAddress());

                        aggregationProducts.add(aggregationProduct);
                    }
                }
            }


            //生成5种排序规则


            //默认排序
            this.redisService.put(moduleAggregation.getLinkCode(), JsonUtil.toJson(aggregationProducts));



            //积分低到高
            Collections.sort(aggregationProducts, new Comparator<AggregationProduct>() {

                @Override
                public int compare(AggregationProduct o1, AggregationProduct o2) {
                    //按照学生的年龄进行升序排列
                    if (o1.getIntegral() > o2.getIntegral()) {
                        return 1;
                    }
                    if (o1.getIntegral() == o2.getIntegral()) {
                        return 0;
                    }
                    return -1;
                }

            });
            this.redisService.put(moduleAggregation.getLinkCode() + "4",JsonUtil.toJson(aggregationProducts));


            //积分高到低
            Collections.sort(aggregationProducts, new Comparator<AggregationProduct>() {

                @Override
                public int compare(AggregationProduct o1, AggregationProduct o2) {
                    //按照学生的年龄进行升序排列
                    if (o1.getIntegral() > o2.getIntegral()) {
                        return -1;
                    }
                    if (o1.getIntegral() == o2.getIntegral()) {
                        return 0;
                    }
                    return 1;
                }

            });
            this.redisService.put(moduleAggregation.getLinkCode() + "3",JsonUtil.toJson(aggregationProducts));


            //销量低到高
            Collections.sort(aggregationProducts, new Comparator<AggregationProduct>() {

                @Override
                public int compare(AggregationProduct o1, AggregationProduct o2) {
                    //按照学生的年龄进行升序排列
                    if (o1.getSales() > o2.getSales()) {
                        return 1;
                    }
                    if (o1.getSales() == o2.getSales()) {
                        return 0;
                    }
                    return -1;
                }

            });
            this.redisService.put(moduleAggregation.getLinkCode() + "2",JsonUtil.toJson(aggregationProducts));


            //销量高到低
            Collections.sort(aggregationProducts, new Comparator<AggregationProduct>() {

                @Override
                public int compare(AggregationProduct o1, AggregationProduct o2) {
                    //按照学生的年龄进行升序排列
                    if (o1.getSales() > o2.getSales()) {
                        return -1;
                    }
                    if (o1.getSales() == o2.getSales()) {
                        return 0;
                    }
                    return 1;
                }

            });
            this.redisService.put(moduleAggregation.getLinkCode() + "1",JsonUtil.toJson(aggregationProducts));


            //更新聚合页状态
            moduleAggregation.setStatus(DBConstants.CategoryStatus.ACTIVED.getCode());
            moduleAggregationService.updateModuleAggregationById(moduleAggregation);

            CommonResult.success(jsonResult);

        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }


        return jsonResult;
    }


    /**
     * 根据id获取一个聚合页
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult selectOneModuleAggregationById(@PathVariable(value = "id") Integer id){
        JsonResult jsonResult = new JsonResult();

        try {

            ModuleAggregation moduleAggregation = moduleAggregationService.selectModuleAggregationById(id);
            if(ValidCheck.validPojo(moduleAggregation)) {
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            Map map = new HashMap();
            map.put("data", moduleAggregation);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }


    /**
     * 添加商品或者商户到聚合页
     * @param id 聚合页id
     * @param type 类型 1商品，2商户
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/{id}/products/{type}")
    @ResponseBody
    public JsonResult insertModuleAggregationProducts(@PathVariable(value = "id") Integer id, @PathVariable(value = "type") Integer type,
                                                      HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            String[] productIds = httpServletRequest.getParameterValues("productIds");

            if (productIds == null || productIds.length == 0) {
                CommonResult.noChoice(jsonResult);
                return jsonResult;
            }

            moduleAggregationService.insertModuleAggregationProduct(id, productIds, type);

            CommonResult.success(jsonResult);

        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

}
