package com.gljr.jifen.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.pojo.FeaturedActivity;
import com.gljr.jifen.service.FeaturedActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/featuredactivitys")
public class FeaturedActivityContorller {

    @Autowired
    private FeaturedActivityService featuredActivityService;

    /**
     * 获取精选页内容
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectFeaturedActivitys(@RequestParam(value = "page", required = false) Integer page,
                                              @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort,
                                              HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try {
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
            List<FeaturedActivity> featuredActivities = featuredActivityService.selectFeaturedActivitysByStatus(1);

            if(ValidCheck.validList(featuredActivities)) {
                PageInfo pageInfo = new PageInfo(featuredActivities);

                Map map = new HashMap();
                map.put("data", featuredActivities);

                map.put("pages", pageInfo.getPages());

                map.put("total", pageInfo.getTotal());
                //当前页
                map.put("pageNum", pageInfo.getPageNum());

                jsonResult.setItem(map);
                CommonResult.success(jsonResult);
            }else{
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

}
