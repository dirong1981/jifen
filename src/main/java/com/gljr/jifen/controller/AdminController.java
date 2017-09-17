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
    private Md5Util md5Util;

    @Autowired
    private StrUtil strUtil;

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




//
//
//
//    //POST
//
//    /**
//     * @PathVariable 接受get参数
//     * 修改密码
//     * 据说要避免多级嵌套........读起来也难读
//     */
//    @RequestMapping(value = "/admins/updatapwd", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResult updataPwd(@RequestParam("npwd") String npwd, @RequestParam("oldpwd") String oldpwd, HttpServletRequest httpServletRequest) {
//
//        /*更改密码 貌似没有什么要变的*/
//        JsonResult jr = new JsonResult();
//
//        if ("".equals(npwd) || npwd == null || "".equals(oldpwd) || oldpwd == null) {
//            jr.setErrorCode("113");
//            jr.setMessage("不能留空...");
//            return jr;
//        }
//
//        String id = httpServletRequest.getHeader("id");
//        jr.setErrorCode("1000");
//        jr.setMessage("按理来说你应该看不到这个的...");
//
//        //取到用户的id
//        if (id != null) {
//
//            Integer aId = Integer.valueOf(id);
//
//
//            Admin admin = adminService.getAdmin(aId);//登录的用户信息..话说这些东西不应该放在一个方便取的bend里面嘛?
//
//            if (admin != null) {
//
//                oldpwd = md5Util.md5(oldpwd + admin.getSalt());//
//
//                if (oldpwd.equals(admin.getPassword())) {
//
//                    admin.setPassword(md5Util.md5(npwd + admin.getSalt()));
//                    admin.setId(aId);
//
//                    try {
//                        adminService.updataPwd(admin);
//                        jr.setErrorCode("200");
//                        jr.setMessage("成功修改密码!");
//                    } catch (Exception e) {
//                        jr.setErrorCode("500");
//                        jr.setMessage("修改密码失败!");
//                    }
//                } else {
//                    jr.setErrorCode(GlobalConstants.USER_PASSWORD_ERROR);
//                    jr.setMessage(GlobalConstants.USER_PASSWORD_ERROR_STR);
//                }
//            } else {
//                jr.setErrorCode("110");
//                jr.setMessage("修改密码失败!");
//            }
//        } else {
//            jr.setErrorCode("120");
//            jr.setMessage("修改密码失败!");
//        }
//
//        return jr;
//    }
//
//
//
//
//    /**
//     * 添加用户
//     *
//     * @param admin
//     * @param bindingResult
//     * @param httpServletRequest
//     * @return
//     */
//    @RequestMapping(value = "/admins/add", method = RequestMethod.POST)
//    @ResponseBody
//    @AuthPassport(permission_code = "6")
//    public JsonResult addAdminAjax(@Valid Admin admin, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
//
//        JsonResult jr = new JsonResult();
//
//        String[] roots = httpServletRequest.getParameterValues("aRule");
//
//        if (bindingResult.hasErrors()) {
//            jr.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
//            return jr;
//        }
//
//        try {
//
//            String salt = strUtil.randomKey(32);
//            admin.setPassword(md5Util.md5(admin.getPassword() + salt));
//            admin.setCreateTime(new Date());
//            admin.setSalt(salt);
//            admin.setAccountType(new Byte("1"));
//            admin.setStatus(new Byte("1"));
//
//            adminService.addAdmin(admin);//主表添加完成
//
//            //读id出来
//            admin = adminService.getAdmin(admin.getUsername());
//
//
//            //后面还有两个表
//            for (String root : roots) {
//                AdminPermissionAssign adminPermissionAssign = new AdminPermissionAssign();
//                adminPermissionAssign.setAid(admin.getId());
//                adminPermissionAssign.setPermissionCode(Integer.valueOf(root));
//                adminPermissionAssign.setAssignTime(new Date());
//                adminService.insertAdminPermissionAssign(adminPermissionAssign);
//            }
//
//
//            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//            jr.setMessage("添加成功!");
//        } catch (Exception e) {
//            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
////            jr.setMessage("添加失败!");
//            jr.setMessage(e.toString());
//        }
//
//        return jr;
//    }
//
//
//    /**
//     * 删除一个Admin
//     *
//     * @param id
//     * @param httpServletRequest
//     * @return
//     */
//    @RequestMapping(value = "/admins/delete/{id}")
//    @ResponseBody
//    @AuthPassport(permission_code = "6")
//    public JsonResult deleteAdminAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
//        JsonResult jsonResult = new JsonResult();
//
//
//        try {
//
//            adminService.deleteProduct(Integer.parseInt(id));
//            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//
//        } catch (Exception e) {
//            System.out.println(e);
//            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
//        }
//
//
//        return jsonResult;
//    }
//
//
//    /**
//     * 获取到对应的用户
//     *
//     * @param id
//     * @param httpServletRequest
//     * @return
//     */
//    @RequestMapping(value = "/admins/get/{id}")
//    @ResponseBody
//    @AuthPassport(permission_code = "6")
//    public JsonResult getAdminAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest) {
//        JsonResult jsonResult = new JsonResult();
//
//        try {
//
//            Admin aAdmin = adminService.getAdmin(Integer.valueOf(id));
//            AdminList admin = new AdminList();
//
//            admin.setUserName(aAdmin.getUsername());
//            admin.setId(aAdmin.getId());
//            admin.setCreateTime(YukiUtil.getYMDHMS(aAdmin.getCreateTime()));
//
//
//            //添加权限
//            List<AdminPermissionAssign> adminPermissionAssigns = adminService.getAdminPermissionAssign(admin.getId());
//            String rootss = "";
//            for (AdminPermissionAssign adminPermissionAssign : adminPermissionAssigns) {
//                rootss += adminPermissionAssign.getPermissionCode() + ",";
//            }
//            rootss = rootss.substring(0,rootss.length()-1);
//            admin.setPermissionCode(rootss);
//
//
//            Map map = new HashMap();
//            map.put("admin", admin);
//            jsonResult.setItem(map);
//
//
//            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//
//        } catch (Exception e) {
//            System.out.println(e);
//            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
//        }
//
//
//        return jsonResult;
//    }
//
//
//    /**
//     * 更改一个用户信息
//     *
//     * @param httpServletRequest
//     * @return
//     */
//    @RequestMapping(value = "/admins/updata", method = RequestMethod.POST)
//    @ResponseBody
//    @AuthPassport(permission_code = "6")
//    public JsonResult updateAdminAjax(@Valid Admin admin, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
//        JsonResult jsonResult = new JsonResult();
//
//        /**
//         * 采用valid插件来验证数据的有效性，验证方法放在pojo实体类里面
//         */
//        if (bindingResult.hasErrors()) {
//            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
//            return jsonResult;
//        }
//
//        Admin oladmin = adminService.getAdmin(admin.getUsername());
//
//        if (oladmin != null) {
//
//            oladmin.setPassword(md5Util.md5(admin.getPassword() + admin.getSalt()));
//
//            try {
//                adminService.updataPwd(oladmin);
//                jsonResult.setErrorCode("200");
//                jsonResult.setMessage("修改成功!");
//            } catch (Exception e) {
//                jsonResult.setErrorCode("500");
//                jsonResult.setMessage("修改失败!");
//            }
//        } else {
//            jsonResult.setErrorCode(GlobalConstants.USER_PASSWORD_ERROR);
//            jsonResult.setMessage(GlobalConstants.USER_PASSWORD_ERROR_STR);
//        }
//
//        return jsonResult;
//    }

}
