package com.gljr.jifen.controller;


import com.gljr.jifen.common.JwtUtil;
import com.gljr.jifen.common.Md5Util;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.Admin;
import com.gljr.jifen.pojo.AdminOnline;
import com.gljr.jifen.pojo.AdminPermissionAssign;
import com.gljr.jifen.service.AdminService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@Controller
@RequestMapping(value = "/jifen")
public class LoginController {


    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Md5Util md5Util;

    @Autowired
    private StrUtil strUtil;

    @Autowired
    private AdminOnline adminOnline;

    /**
     * 用户登录界面的跳转控制
     * localhost:8080/admin/login
     * localhost:8080/admin/
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = {"/login","/"}, method = RequestMethod.GET)
    public ModelAndView loginPage(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/login");

        //如果是登录状态，直接跳转到index
        HttpSession httpSession = httpServletRequest.getSession();
        if(httpSession.getAttribute(GlobalConstants.SESSION_ADMIN) !=  null){
            mv.setViewName("/admin/index");
        }

        return mv;
    }


    /**
     * 用户登录控制
     * @param admin
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult login(@Valid Admin admin, BindingResult bindingResult, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        JsonResult jsonResult = new JsonResult();

        //httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");

        /**
         * 采用valid插件来验证数据的有效性，验证方法放在pojo实体类里面
         */
        if(bindingResult.hasErrors()){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jsonResult;
        }


        Admin selectAdmin = adminService.login(admin);

        if(selectAdmin != null){
            //生成token

//            System.out.println(admin.getPassword()+"=============");
//            System.out.println(selectAdmin.getSalt()+"============");
//            System.out.println(md5Util.md5(admin.getPassword()+selectAdmin.getSalt())+"==============");
//            System.out.println(selectAdmin.getPassword()+"==============");
            try {
                if (md5Util.md5(admin.getPassword()+selectAdmin.getSalt()).equals(selectAdmin.getPassword())) {

                    //生成32位token的key
                    String key = strUtil.randomKey(32);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", selectAdmin.getId());
                    jsonObject.put("username", selectAdmin.getUsername());

                    //如果是系统管理员把权限放入token
                    if(selectAdmin.getAccountType() == '1'){

                        List<AdminPermissionAssign> list= adminService.getPermission(selectAdmin.getId());
                        jsonObject.put("adminPermission", list);

                    }


                    //存入用户在线表
//                    System.out.println(selectAdmin.getId()+"-----"+key+"----"+new Byte("2"));
                    adminOnline.setAid(selectAdmin.getId());
                    adminOnline.setToken(key);
                    adminOnline.setClientType(new Byte("2"));
                    adminOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));


                    Map map = new HashMap();
                    List<AdminOnline> list = adminService.selectAdminOnlines(selectAdmin.getId(), new Byte("2"));
                    if(list.size() == 0) {
                        adminService.insertAdminOnline(adminOnline);
                        //生成token
                        String token = jwtUtil.createJWT("gljr", jsonObject.toString(), key, 60*60*24*360);

                        map.put("token", token);
                        map.put("id", selectAdmin.getId());
                        map.put("username", selectAdmin.getUsername());

                    }else{
                        key = list.get(0).getToken();
                        String token = jwtUtil.createJWT("gljr", jsonObject.toString(), key, 60*60*24*360);
                        map.put("refreshToken", token);
                    }

                    jsonResult.setItem(map);

                    jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                }else{
                    jsonResult.setErrorCode(GlobalConstants.USER_PASSWORD_ERROR);
                }
            }catch (Exception e){
                jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            }
        }


//        if(admins.isEmpty()){
//            jsonResult.setErrorCode(GlobalConstants.USER_DOES_NOT_EXIST);
//            jsonResult.setMessage(GlobalConstants.USER_DOES_NOT_EXIST_STR);
//            return jsonResult;
//        }
//
//        if(admin.getaPassword().equals(admins.get(0).getaPassword())){
//            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
//
//            HttpSession httpSession = httpServletRequest.getSession();
//            httpSession.setAttribute(GlobalConstants.SESSION_ADMIN, admins.get(0).getaName());
//            httpSession.setAttribute(GlobalConstants.SESSION_ADMIN_ID, admins.get(0).getaId());
//
//            Map map = new HashMap();
//            map.put("admin",admin);
//
//            jsonResult.setItem(map);
//
//            return jsonResult;
//        }else{
//            jsonResult.setErrorCode(GlobalConstants.USER_PASSWORD_ERROR);
//            jsonResult.setMessage(GlobalConstants.USER_PASSWORD_ERROR_STR);
//            return jsonResult;
//        }

        return jsonResult;
    }

    //用户登出
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS,DELETE,PUT");

        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.invalidate();

        jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        return  jsonResult;
    }

}
