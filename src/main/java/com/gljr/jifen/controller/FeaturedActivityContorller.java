package com.gljr.jifen.controller;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.service.FeaturedActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/featuredactivitys")
public class FeaturedActivityContorller {

    @Autowired
    private FeaturedActivityService featuredActivityService;

    /**
     * 获取精选页内容
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectFeaturedActivitys(@RequestParam(value = "page", required = false) Integer page,
                                              @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort){
        JsonResult jsonResult = new JsonResult();

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

        jsonResult = featuredActivityService.selectFeaturedActivitysEnabled(page, per_page, jsonResult);

        return jsonResult;
    }

}
