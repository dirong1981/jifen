package com.gljr.jifen.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.AdminService;
import com.gljr.jifen.service.StoreInfoService;
import com.gljr.jifen.service.UserCreditsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/stores")
public class StoreInfoController extends BaseController{


    @Autowired
    private StoreInfoService storeInfoService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserCreditsService userCreditsService;

    /**
     * 获取一个商户信息
     * @param storeId 商户id
     * @return 商户详细信息
     */
    @GetMapping(value = "/{storeId}")
    @ResponseBody
    public JsonResult getStore(@PathVariable("storeId") Integer storeId) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(storeId)){
            CommonResult.noObject(jsonResult);
            return  jsonResult;
        }

        jsonResult = storeInfoService.selectStoreInfoById(storeId, jsonResult);

        return jsonResult;
    }


}
