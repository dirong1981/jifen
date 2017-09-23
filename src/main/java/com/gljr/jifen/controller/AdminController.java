package com.gljr.jifen.controller;


import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.service.StoreInfoService;
import com.gljr.jifen.util.YukiUtil;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.Md5Util;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1")
public class AdminController {


    @Autowired
    private AdminService adminService;


    @Autowired
    private StoreInfoService storeInfoService;


    /**
     * 获取所有管理员
     * @param type 管理员类型 1系统管理员 2商户管理员
     * @param httpServletResponse
     * @param httpServletRequest
     * @return 返回管理员列表
     */
    @RequestMapping(value = "/admins/type/{type}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getAdmins(@PathVariable(value = "type") String type, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            List<Admin> admins = adminService.getAdmins(type);
            String adminPermissions;
            for(Admin admin: admins){

                //循环所有管理员，获取该管理员所有权限
                adminPermissions = "";

                //如果是系统管理员，查询他的权限，如果是商户管理员查询商户的名字
                if(type.equals("1")) {
                    List<AdminPermissionAssign> adminPermissionAssigns = adminService.getAdminPermissionAssign(admin.getId());
                    for (AdminPermissionAssign adminPermissionAssign : adminPermissionAssigns) {

                        //循环所有权限，获取该权限的名字
                        SystemPermission systemPermission = adminService.getSystemPermission(adminPermissionAssign.getPermissionCode());
                        adminPermissions += systemPermission.getName() + ",";
                    }
                    //把权限集合放入管理员模型中
                    admin.setPermission(adminPermissions);
                }else{
                    StoreInfo storeInfo = storeInfoService.selectStoreInfoByAid(admin.getId());
                    admin.setPermission(storeInfo.getName());
                }
            }
            Map map = new HashMap();
            map.put("data", admins);

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return  jsonResult;
    }


    /**
     * 添加系统权限
     * @param systemPermission 系统权限模型
     * @param bindingResult 验证类
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 状态码
     */
    @RequestMapping(value = "/permissions", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addPermission(@Valid SystemPermission systemPermission, BindingResult bindingResult, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        if(bindingResult.hasErrors()){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            jsonResult.setMessage(GlobalConstants.NOTNULL);
            return jsonResult;
        }

        try{
            int code = (int)(Math.random()*1000000);
            systemPermission.setCode(code);
            adminService.insertSystemPermission(systemPermission);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return  jsonResult;
    }


    /**
     * 获取所有系统权限
     * @param httpServletResponse
     * @param httpServletRequest
     * @return 系统权限列表
     */
    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getPermissions(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {

            List<SystemPermission> systemPermissions = adminService.selectSystemPermission();
            Map map = new HashMap();
            map.put("data", systemPermissions);
            jsonResult.setItem(map);

            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return  jsonResult;
    }



    @RequestMapping(value = "/permissions/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult getPermissions(@PathVariable(value = "id") Integer id, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {

            SystemPermission systemPermission = adminService.selectSystemPermissionById(id);

            adminService.deleteSystemPermission(id);

            adminService.deleteSonSystemPermission(systemPermission.getCode());

            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return  jsonResult;
    }



}
