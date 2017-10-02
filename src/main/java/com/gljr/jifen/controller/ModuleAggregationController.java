package com.gljr.jifen.controller;


import com.gljr.jifen.common.CommonResult;

import com.gljr.jifen.common.JsonResult;
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
     * @param id
     * @param page
     * @param per_page
     * @param sort 排序
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult selectModuleAggregations(@PathVariable(value = "id") String id, @RequestParam(value = "page", required = false) Integer page,
                                               @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) String sort,
                                               HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();
        try {
            Map map = new HashMap();

            Gson gson = new Gson();

            //如果没有设置排序，按照默认读取数据
            if(sort == null || sort.equals("")){
                sort = "";
            }


            //获取缓存中对应的排序结果
            String json = this.redisService.get(id+sort);

            //为空，提示没有找到数据
            if(StringUtils.isEmpty(json)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }else {
                //json = json.replaceAll("\\\"", "'");
                map.put("data", json);

                jsonResult.setItem(map);
                CommonResult.success(jsonResult);
            }
        }catch (Exception e){
            System.out.println(e);
            CommonResult.failed(jsonResult);
        }


        return jsonResult;
    }

}
