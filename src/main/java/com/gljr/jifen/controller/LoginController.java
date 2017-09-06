package com.gljr.jifen.controller;


import com.gljr.jifen.common.JwtUtil;
import com.gljr.jifen.common.Md5Util;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.Admin;
import com.gljr.jifen.pojo.AdminOnline;
import com.gljr.jifen.pojo.AdminPermissionAssign;
import com.gljr.jifen.service.AdminService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
     * 用户登录控制
     * @param admin
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @AuthPassport(validate = false)

    public JsonResult login(@Valid Admin admin, BindingResult bindingResult, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        JsonResult jsonResult = new JsonResult();

        /**
         * 采用valid插件来验证数据的有效性，验证方法放在pojo实体类里面
         */
        if(bindingResult.hasErrors()){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jsonResult;
        }


        Admin selectAdmin = adminService.login(admin);

        if(selectAdmin != null){
            try {
                if (md5Util.md5(admin.getPassword()+selectAdmin.getSalt()).equals(selectAdmin.getPassword())) {

                    //生成32位token的key
                    String key = strUtil.randomKey(32);

                    //生成session
                    HttpSession httpSession = httpServletRequest.getSession();

                    //生成token中的数据
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", selectAdmin.getId());
                    jsonObject.put("username", selectAdmin.getUsername());


                    String token;

                    //查询online表中用户是否存在
                    Map map = new HashMap();
                    List<AdminOnline> list = adminService.selectAdminOnlines(selectAdmin.getId(), new Byte("2"));
                    if(list.size() == 0) {

                        //如果在线表中用户不存在，将用户存入online表
                        adminOnline.setAid(selectAdmin.getId());
                        adminOnline.setToken(key);
                        adminOnline.setClientType(new Byte("2"));
                        adminOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));

                        adminService.insertAdminOnline(adminOnline);
                        //生成token
                        token = jwtUtil.createJWT("gljr", jsonObject.toString(), key, 60*60*24*360);

                        //输出到客户端
                        map.put("token", token);
                        map.put("id", selectAdmin.getId());
                        map.put("username", selectAdmin.getUsername());

                    }else{

                        //用户存在
                        //更新token的时间
                        key = list.get(0).getToken();
                        token = jwtUtil.createJWT("gljr", jsonObject.toString(), key, 60*60*24*360);
                        map.put("refreshToken", token);
                    }

                    jsonResult.setItem(map);

                    //查询用户权限
                    String permission_codes = "";
                    List<AdminPermissionAssign> adminPermissionAssigns = adminService.getPermission(selectAdmin.getId());
                    for (AdminPermissionAssign adminPermissionAssign : adminPermissionAssigns) {
                        permission_codes += adminPermissionAssign.getPermissionCode() + ",";
                    }

                    //admin这个管理员具有所有权限
                    if(selectAdmin.getUsername().equals("admin")){
                        permission_codes = "100,200,300,400,500,600,700,800,900";
                    }
                    //把token和用户权限放入session中，方便拦截器调用
//                    httpSession.setAttribute("token", token);
//                    httpSession.setAttribute("permission_codes", permission_codes);

                    Jedis jedis = new Jedis("localhost");

                    jedis.set("token"+selectAdmin.getId(), token);
                    jedis.set("permission_codes"+selectAdmin.getId(), permission_codes);

                    jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                }else{
                    jsonResult.setErrorCode(GlobalConstants.USER_PASSWORD_ERROR);
                }
            }catch (Exception e){
                jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            }
        }else{
            jsonResult.setErrorCode(GlobalConstants.USER_DOES_NOT_EXIST);
        }

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
