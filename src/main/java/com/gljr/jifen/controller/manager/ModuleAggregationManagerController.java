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

    /**
     * 添加一个聚合页
     * @param moduleAggregation
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
     * @param moduleAggregationId
     * @return
     */
    @GetMapping(value = "/{moduleAggregationId}/rejection")
    @ResponseBody
    public JsonResult stopModuleAggregation(@PathVariable("moduleAggregationId") Integer moduleAggregationId){
        JsonResult jsonResult = new JsonResult();

        jsonResult = moduleAggregationService.rejectionModuleAggregationById(moduleAggregationId, jsonResult);


        return jsonResult;
    }


    /**
     * 生效一个聚合页
     * @param moduleAggregationId
     * @return
     */
    @GetMapping(value = "/{moduleAggregationId}/acceptance")
    @ResponseBody
    public JsonResult startModuleAggregation(@PathVariable("moduleAggregationId") Integer moduleAggregationId){
        JsonResult jsonResult = new JsonResult();

        jsonResult = moduleAggregationService.acceptanceModuleAggregationById(moduleAggregationId, jsonResult);


        return jsonResult;
    }


    /**
     * 根据id获取一个聚合页
     * @param moduleAggregationId
     * @return
     */
    @GetMapping(value = "/{moduleAggregationId}")
    @ResponseBody
    public JsonResult selectOneModuleAggregationById(@PathVariable(value = "moduleAggregationId") Integer moduleAggregationId){
        JsonResult jsonResult = new JsonResult();

        jsonResult = moduleAggregationService.selectModuleAggregationById(moduleAggregationId, jsonResult);
        return jsonResult;
    }


    /**
     * 添加商品或者商户到聚合页
     * @param moduleAggregationId 聚合页id
     * @param type 类型 1商品，2商户
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/{moduleAggregationId}/products/{type}")
    @ResponseBody
    public JsonResult insertModuleAggregationProducts(@PathVariable(value = "moduleAggregationId") Integer moduleAggregationId, @PathVariable(value = "type") Integer type,
                                                      HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        String[] productIds = httpServletRequest.getParameterValues("productIds");

        if (productIds == null || productIds.length == 0) {
            CommonResult.noChoice(jsonResult);
            return jsonResult;
        }

        if(type != 1 && type != 2){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = moduleAggregationService.insertModuleAggregationProduct(moduleAggregationId, productIds, type, jsonResult);



        return jsonResult;
    }

}
