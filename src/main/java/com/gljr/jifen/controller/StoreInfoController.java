package com.gljr.jifen.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.JedisUtil;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.Md5Util;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.AdminService;
import com.gljr.jifen.service.StoreInfoService;
import com.gljr.jifen.util.YukiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/stores")
public class StoreInfoController {


    @Autowired
    private StoreInfoService storeInfoService;



    /**
     * 获取所有审核通过商户
     * @param page 当前页
     * @param per_page 每页显示数量
     * @param sort 排序方式
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回商户列表
     */
    @GetMapping
    @ResponseBody
    public JsonResult getStores(@RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort,
                                HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {


        JsonResult jsonResult = new JsonResult();


        try {

            //设置各个参数的默认值
            if(page == null){
                page = 1;
            }
            if(per_page == null){
                per_page = 10;
            }
            if(sort == null || sort > 2 || sort < 0){
                sort = 0;
            }

            PageHelper.startPage(page,per_page);
            List<StoreInfo> storeInfos = storeInfoService.selectAllShowStoreInfo(sort);

            PageInfo pageInfo = new PageInfo(storeInfos);
            Map  map = new HashMap();
            map.put("data", storeInfos);
            //设置总页面
            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

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
     * 获取一个商户信息
     * @param id 商户id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 商户详细信息
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult getStore(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        JsonResult jsonResult = new JsonResult();

        try {

            StoreInfo storeInfo = storeInfoService.selectStoreInfoById(id);

            Map map = new HashMap();
            map.put("data", storeInfo);

            List<StorePhoto> storePhotos = storeInfoService.selectStorePhotoById(id);
            map.put("photos", storePhotos);

            jsonResult.setItem(map);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }
}
