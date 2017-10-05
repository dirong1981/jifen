package com.gljr.jifen.controller;


import com.gljr.jifen.common.CommonResult;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.pojo.ModuleAggregation;
import com.gljr.jifen.service.ModuleAggregationService;
import com.gljr.jifen.service.RedisService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/moduleaggregations")
public class ModuleAggregationController {

    @Autowired
    private ModuleAggregationService moduleAggregationService;

    @Autowired
    private RedisService redisService;

    /**
     * 通过路径获取一个聚合页
     * @param link
     * @param page
     * @param per_page
     * @param sort 排序
     * @return
     */
    @GetMapping(value = "/{link}")
    @ResponseBody
    public JsonResult selectModuleAggregations(@PathVariable(value = "link") String link, @RequestParam(value = "page", required = false) Integer page,
                                               @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort){
        JsonResult jsonResult = new JsonResult();
        try {
            Map map = new HashMap();

            String _sort;
            //如果没有设置排序，按照默认读取数据
            if(sort == null || sort > 4 || sort < 0 || sort == 0){
                _sort = "";
            }else {
                _sort = sort +"";
            }


            //获取缓存中对应的排序结果
            String json = this.redisService.get(link+_sort);

            //为空，提示没有找到数据
            if(StringUtils.isEmpty(json)){
                jsonResult = moduleAggregationService.restartmoduleAggregationByLink(link, jsonResult);
            }

            json = this.redisService.get(link+_sort);

            //还为空
            if(StringUtils.isEmpty(json)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            map.put("data", json);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            System.out.println(e);
            CommonResult.failed(jsonResult);
        }


        return jsonResult;
    }

}
