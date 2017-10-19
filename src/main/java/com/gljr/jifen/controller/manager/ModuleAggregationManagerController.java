package com.gljr.jifen.controller.manager;


import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.*;
import com.google.gson.Gson;
import com.qiniu.util.Json;
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
    @AuthPassport(permission_code = "#131202#")
    public JsonResult insertModuleAggregations(ModuleAggregation moduleAggregation, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        String aid = httpServletRequest.getHeader("aid");
        if(StringUtils.isEmpty(aid)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        moduleAggregation.setCreateTime(new Timestamp(System.currentTimeMillis()));
        moduleAggregation.setManagerId(Integer.parseInt(aid));
        moduleAggregation.setLinkCode(StrUtil.randomKey(5));

        jsonResult = moduleAggregationService.insertModuleAggregation(moduleAggregation, jsonResult);

        return jsonResult;
    }


    /**
     * 获取所有聚合页
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectModuleAggregations(@RequestParam(value = "page", required = false) Integer page,
                                               @RequestParam(value = "per_page", required = false) Integer per_page){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(page)){
            page = 1;
        }

        if(StringUtils.isEmpty(per_page)){
            per_page = 10;
        }

        jsonResult = moduleAggregationService.selectModuleAggregations(page, per_page, jsonResult);

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
    @AuthPassport(permission_code = "#131202#")
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
    @AuthPassport(permission_code = "#131202#")
    public JsonResult startModuleAggregation(@PathVariable("moduleAggregationId") Integer moduleAggregationId){
        JsonResult jsonResult = new JsonResult();

        jsonResult = moduleAggregationService.acceptanceModuleAggregationById(moduleAggregationId, jsonResult);


        return jsonResult;
    }

    /**
     * 删除一个聚合页
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult deleteModuleAggregationById(@PathVariable("id") Integer id){

        JsonResult jsonResult = moduleAggregationService.deleteModuleAggregationById(id);

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
    @AuthPassport(permission_code = "#131202#")
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





    /**
     * 通过路径获取一个聚合页
     * @return
     */
    @GetMapping(value = "/show/{id}")
    @ResponseBody
    public JsonResult selectModuleAggregations(@PathVariable(value = "id") Integer id){
        JsonResult jsonResult = new JsonResult();

        jsonResult = moduleAggregationService.showModuleAggregationByLink(id, jsonResult);

        return jsonResult;
    }

    /**
     * 添加条件聚合页
     * @param id
     * @param from
     * @param to
     * @return
     */
    @PostMapping(value = "/{moduleAggregationId}/condition")
    @ResponseBody
    public JsonResult insertModuleAggregationCondition(@PathVariable(value = "moduleAggregationId") Integer id, @RequestParam("from") Integer from, @RequestParam(value = "to") Integer to){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(from) || StringUtils.isEmpty(to)){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage("请填写搜索条件！");
            return jsonResult;
        }

        jsonResult = moduleAggregationService.insertModuleAggregationCondition(id, from, to);

        return jsonResult;
    }

}
