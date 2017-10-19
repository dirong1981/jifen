package com.gljr.jifen.controller.manager;


import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.service.RedisService;
import com.gljr.jifen.service.StoreInfoService;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.AdminService;
import org.json.JSONObject;
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
     * @return 返回管理员列表
     */
    @GetMapping(value = "/type/{type}")
    @ResponseBody
    @AuthPassport(permission_code = "#14#")
    public JsonResult getAdmins(@PathVariable(value = "type") int type, @RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "per_page", required = false) Integer per_page){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(page)){
            page = 1;
        }

        if(StringUtils.isEmpty(per_page)){
            per_page = 10;
        }

        jsonResult = adminService.selectAllAdminByType(type, page, per_page, jsonResult);

        return  jsonResult;
    }




    /**
     * 获取所有系统权限
     * @return 系统权限列表
     */
    @GetMapping(value = "/permissions")
    @ResponseBody
    public JsonResult getPermissions(){
        JsonResult jsonResult = new JsonResult();

            jsonResult = adminService.selectSystemPermission(jsonResult);

        return  jsonResult;
    }


    /**
     * 添加一个系统管理员
     * @param admin
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ResponseBody
    @AuthPassport(permission_code = "#141001#")
    public JsonResult insertAdmin(Admin admin, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();


        try {
            String[] permissions = httpServletRequest.getParameterValues("permissions");


            List<Admin> admins = adminService.selectAdminByUsername(admin.getUsername());
            if (!ValidCheck.validList(admins)){
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage(GlobalConstants.STORE_ADMIN_EXIST);
                return jsonResult;
            }else{
                admin.setCreateTime(new Timestamp(System.currentTimeMillis()));
                admin.setAccountType(DBConstants.AdminAccountType.SYS_ADMIN.getCode());
                admin.setStatus(DBConstants.AdminAccountStatus.INACTIVE.getCode());
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
     * 获取一个管理员信息和权限
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    @AuthPassport(permission_code = "#141002#")
    public JsonResult selectAdminById(@PathVariable(value = "id") Integer id){
        JsonResult jsonResult = new JsonResult();

        jsonResult = adminService.selectAdminPermissionAssignByAid(id, jsonResult);

        return jsonResult;
    }


    /**
     * 禁用管理员账号
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}/rejection")
    @ResponseBody
    @AuthPassport(permission_code = "#141003#")
    public JsonResult stopAdmin(@PathVariable(value = "id") Integer id){
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
     * @return
     */
    @GetMapping(value = "/{id}/acceptance")
    @ResponseBody
    @AuthPassport(permission_code = "#141003#")
    public JsonResult startAdmin(@PathVariable(value = "id") Integer id){
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
     * @param httpServletRequest
     * @return 返回状态码
     */
    @PostMapping("/login")
    @ResponseBody
    public JsonResult login(Admin admin, HttpServletRequest httpServletRequest){

        JsonResult jsonResult = new JsonResult();

        String client_type = httpServletRequest.getHeader("device");

        jsonResult = adminService.login(admin, client_type, jsonResult);


        return jsonResult;
    }


    /**
     * 修改密码
     * @param aid
     * @param oldpwd
     * @param npwd
     * @return
     */
    @PostMapping(value = "/pwd")
    @ResponseBody
    public JsonResult changePwdByAid(@RequestParam(value = "aid") Integer aid, @RequestParam(value = "oldpwd") String oldpwd, @RequestParam(value = "npwd") String npwd){
        JsonResult jsonResult = new JsonResult();

        jsonResult = adminService.updateAdminPasswordByAid(aid, oldpwd, npwd, jsonResult);

        return jsonResult;
    }


    /**
     * 修改一个用户密码和权限
     * @param aid
     * @param password
     * @param permissions
     * @return
     */
    @PostMapping("/modification")
    @ResponseBody
    @AuthPassport(permission_code = "#141004#")
    public JsonResult modifyAdminByAid(@RequestParam(value = "aid") Integer aid, @RequestParam(value = "password", required = false) String password,
                                       @RequestParam(value = "permissions", required = false) String permissions){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(aid)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = adminService.modifyAdminByAid(aid, permissions, password, jsonResult);

        return jsonResult;
    }

    @GetMapping
    @ResponseBody
    public JsonResult selectAdminInfo(HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        String aid = httpServletRequest.getHeader("aid");

        jsonResult = adminService.selectAdminInfo(aid);

        return jsonResult;
    }
}
