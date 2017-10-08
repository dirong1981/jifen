package com.gljr.jifen.controller.manager;


import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.service.RedisService;
import com.gljr.jifen.service.StoreInfoService;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.AdminService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping(value = "/v1/manager/admins")
public class AdminManagerController extends BaseController {


    @Autowired
    private AdminService adminService;


    @Autowired
    private StoreInfoService storeInfoService;

    @Autowired
    private RedisService redisService;


    /**
     * 获取所有管理员
     * @param type 管理员类型 1系统管理员 2商户管理员
     * @param httpServletResponse
     * @param httpServletRequest
     * @return 返回管理员列表
     */
    @GetMapping(value = "/type/{type}")
    @ResponseBody
    public JsonResult getAdmins(@PathVariable(value = "type") int type, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            List<Admin> admins = adminService.selectAdminsByType(type);

            for(Admin admin: admins){

                String adminPermissions= "没有权限";
                //循环所有管理员，获取该管理员所有权限
                //如果是系统管理员，查询他的权限，如果是商户管理员查询商户的名字
                if(type == 1) {
                    List<AdminPermissionAssign> adminPermissionAssigns = adminService.selectAdminPermissionAssignByAid(admin.getId());
                    if(adminPermissionAssigns.size() != 0 && adminPermissionAssigns != null){
                        adminPermissions = "";
                        for (AdminPermissionAssign adminPermissionAssign : adminPermissionAssigns) {

                            //循环所有权限，获取该权限的名字
                            SystemPermission systemPermission = adminService.selectSystemPermission(adminPermissionAssign.getPermissionCode());
                            adminPermissions += systemPermission.getName() + ",";
                        }
                    }
                    //把权限集合放入管理员模型中
                    admin.setPermission(adminPermissions);
                }else{
//                    StoreInfo storeInfo = storeInfoService.selectStoreInfoByAid(admin.getId());
//                    admin.setPermission(storeInfo.getName());
                }

                //查询管理员最后登录时间
                List<AdminOnline> adminOnlines = adminService.selectAdminOnlinesByAid(admin.getId());
                if(adminOnlines.size() != 0 && adminOnlines != null){
                    admin.setCreateTime(adminOnlines.get(0).getLoginTime());
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
//    @PostMapping(value = "/permissions")
//    @ResponseBody
//    public JsonResult addPermission(@Valid SystemPermission systemPermission, BindingResult bindingResult, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
//        JsonResult jsonResult = new JsonResult();
//
//        if(bindingResult.hasErrors()){
//            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
//            jsonResult.setMessage(GlobalConstants.NOTNULL);
//            return jsonResult;
//        }
//
//        try{
//            int code = (int)(Math.random()*1000000);
//            systemPermission.setCode(code);
//            adminService.insertSystemPermission(systemPermission);
//            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
//            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//        }catch (Exception e){
//            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
//            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
//        }
//
//        return  jsonResult;
//    }


    /**
     * 获取所有系统权限
     * @param httpServletResponse
     * @param httpServletRequest
     * @return 系统权限列表
     */
    @GetMapping(value = "/permissions")
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


    /**
     * 添加一个系统管理员
     * @param admin
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult insertAdmin(Admin admin, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();


        try {
            String[] permissions = httpServletRequest.getParameterValues("permissions");

            List<Admin> admins = adminService.selectAdminByUsername(admin.getUsername());
            if (admins.size() != 0){
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage(GlobalConstants.STORE_ADMIN_EXIST);
                return jsonResult;
            }else{
                admin.setCreateTime(new Timestamp(System.currentTimeMillis()));
                admin.setAccountType(DBConstants.AdminAccountType.SYS_ADMIN.getCode());
                admin.setStatus(DBConstants.AdminAccountStatus.ACTIVED.getCode());
                String salt = StrUtil.randomKey(32);
                admin.setSalt(salt);
                admin.setPassword(Md5Util.md5("admin"+salt));

                adminService.insertAdmin(admin, permissions);
            }

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
        } catch (Exception e) {
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jsonResult;
    }


    /**
     * 获取一个管理选信息和权限
     * @param id
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult selectAdminById(@PathVariable(value = "id") Integer id, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            Admin admin = adminService.selectAdminById(id);

            List<AdminPermissionAssign> adminPermissionAssigns = adminService.selectAdminPermissionAssignByAid(admin.getId());

            Map map = new HashMap();
            map.put("admin", admin);
            map.put("permission", adminPermissionAssigns);

            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setItem(map);
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }


    /**
     * 禁用管理员账号
     * @param id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping(value = "/{id}/rejection")
    @ResponseBody
    public JsonResult stopAdmin(@PathVariable(value = "id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try {
            Admin admin = adminService.selectAdminById(id);

            admin.setStatus(DBConstants.AdminAccountStatus.DISABLED.getCode());
            adminService.updateAdminById(admin);

            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }


    /**
     * 启用管理员账号
     * @param id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping(value = "/{id}/acceptance")
    @ResponseBody
    public JsonResult startAdmin(@PathVariable(value = "id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try {
            Admin admin = adminService.selectAdminById(id);

            admin.setStatus(DBConstants.AdminAccountStatus.ACTIVED.getCode());
            adminService.updateAdminById(admin);

            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }


//
//
//
//    @DeleteMapping("/permissions/{id}")
//    @ResponseBody
//    public JsonResult getPermissions(@PathVariable(value = "id") Integer id, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
//        JsonResult jsonResult = new JsonResult();
//
//        try {
//
//            SystemPermission systemPermission = adminService.selectSystemPermissionById(id);
//
//            adminService.deleteSystemPermission(id);
//
//            adminService.deleteSonSystemPermission(systemPermission.getCode());
//
//            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
//            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//        }catch (Exception e){
//            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
//            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
//        }
//
//        return  jsonResult;
//    }


    /**
     * 管理员退出
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态码
     */
    @GetMapping("/logout")
    @ResponseBody
    public JsonResult logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        String aid = httpServletRequest.getHeader("aid");

        if(aid == null || aid.equals("") || aid.equals("NULL")){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);

        }else{
            this.redisService.evict("admin_" + aid);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
        }
        return  jsonResult;
    }




    /**
     * 管理员登录
     * @param admin 管理员模型
     * @param bindingResult 验证类
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态码
     */
    @PostMapping("/login")
    @ResponseBody
    public JsonResult login(@Valid Admin admin, BindingResult bindingResult, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        JsonResult jsonResult = new JsonResult();

        /**
         * 采用valid插件来验证数据的有效性，验证方法放在pojo实体类里面
         */
        if(bindingResult.hasErrors()){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            jsonResult.setMessage(GlobalConstants.NOTNULL);
            return jsonResult;
        }

        String client_type = httpServletRequest.getHeader("device");


        //int clientType = ClientType.checkClientType(client_type);


//        List<Admin> admins = adminService.login(admin);

        Admin selectAdmin = adminService.login(admin);

        //用户名存在
        if(!ValidCheck.validPojo(selectAdmin)){

            if(selectAdmin.getStatus() == 0){
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage(GlobalConstants.NO_ACTIVATION);
                return jsonResult;
            }
            if(selectAdmin.getStatus() == 2){
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage(GlobalConstants.IS_DISABLE);
                return jsonResult;
            }

            //密码验证通过
            if(Md5Util.md5(admin.getPassword() + selectAdmin.getSalt()).equals(selectAdmin.getPassword())){


                try {

                    //查询该用户的权限
                    String permission = "#";
                    //如果账号为admin，不查询权限
                    if(!selectAdmin.getUsername().equals("admin")) {
                        List<AdminPermissionAssign> adminPermissionAssigns = adminService.selectAdminPermissionAssignByAid(selectAdmin.getId());
                        if (adminPermissionAssigns != null && adminPermissionAssigns.size() != 0) {
                            for (AdminPermissionAssign adminPermissionAssign : adminPermissionAssigns) {
                                permission = permission + adminPermissionAssign.getPermissionCode() + "#";
                            }
                        } else {
                            jsonResult.setMessage(GlobalConstants.AUTH_FAILED);
                            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                            return jsonResult;
                        }
                    }else{
                        List<SystemPermission> systemPermissions = adminService.selectSystemPermission();
                        for (SystemPermission systemPermission : systemPermissions){
                            permission = permission + systemPermission.getCode() + "#";
                        }
                    }

                    //生成32位token的key
                    String key = StrUtil.randomKey(32);


                    //生成token中的数据
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", selectAdmin.getId());
                    jsonObject.put("username", selectAdmin.getUsername());
                    jsonObject.put("permission", permission);

                    //生成token
                    String token = JwtUtil.createJWT("gljr", jsonObject.toString(), key, 60 * 60 * 24 * 360);

                    //查询用户在线状态并更新

                    List<AdminOnline> adminOnlines = adminService.selectAdminOnlinesByAid(selectAdmin.getId(), DBConstants.ClientType.WEB.getCode());
                    if (adminOnlines != null && adminOnlines.size() != 0) {
                        AdminOnline adminOnline = adminOnlines.get(0);

                        adminOnline.setToken(key);
                        adminOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));

                        adminService.updateAdminOnlinesById(adminOnline);

                    } else {
                        AdminOnline adminOnline = new AdminOnline();

                        adminOnline.setAid(selectAdmin.getId());
                        adminOnline.setClientType(DBConstants.ClientType.WEB.getCode());
                        adminOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));
                        adminOnline.setToken(key);

                        adminService.insertAdminOnline(adminOnline);
                    }

                    Map map = new HashMap();
                    map.put("aid", selectAdmin.getId()+"");
                    map.put("username", selectAdmin.getUsername());
                    map.put("token", token);
                    map.put("permission", permission);
                    map.put("accountType", selectAdmin.getAccountType()+"");
                    map.put("tokenKey", key);

                    //把用户信息存入jedis
                    this.redisService.put("admin_" + selectAdmin.getId(), JsonUtil.toJson(map));

//                    System.out.println(jedis.hmget("admin"+selectAdmin.getId(), "aid"));
//                    System.out.println(jedis.hmget("admin"+selectAdmin.getId(), "username"));
//                    System.out.println(jedis.hmget("admin"+selectAdmin.getId(), "token"));
//                    System.out.println(jedis.hmget("admin"+selectAdmin.getId(), "permission"));

                    jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                    jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
                    jsonResult.setItem(map);

                }catch (Exception e){
                    System.out.println(e);
                    jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                }

            }else{
                jsonResult.setMessage(GlobalConstants.ADMIN_PASSWORD_ERROR);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            }
        }else{
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.ADMIN_DOES_NOT_EXIST);
        }

        return jsonResult;
    }


}
