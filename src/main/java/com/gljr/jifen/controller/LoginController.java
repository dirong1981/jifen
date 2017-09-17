package com.gljr.jifen.controller;


import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.GlobalConstants;
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
@RequestMapping(value = "/v1")
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

    @Autowired
    private JedisUtil jedisUtil;


    /**
     * 管理员登录
     * @param admin 管理员模型
     * @param bindingResult 验证类
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态码
     */
    @RequestMapping(value = "/login/admin", method = RequestMethod.POST)
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


        Admin selectAdmin = adminService.login(admin);

        if(selectAdmin != null){
            try {
                if (md5Util.md5(admin.getPassword()+selectAdmin.getSalt()).equals(selectAdmin.getPassword())) {

                    //生成32位token的key
                    String key = strUtil.randomKey(32);


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
                        map.put("id", selectAdmin.getId());
                        map.put("username", selectAdmin.getUsername());
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
                        permission_codes = "1,2,3,4,5,6,7,8,9";
                    }
                    //把token和用户权限放入session中，方便拦截器调用
//                    httpSession.setAttribute("token", token);
//                    httpSession.setAttribute("permission_codes", permission_codes);

                    Jedis jedis = jedisUtil.getJedis();

                    jedis.set("token"+selectAdmin.getId(), token);
                    jedis.set("permission_codes"+selectAdmin.getId(), permission_codes);

                    jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                    jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
                }else{
                    jsonResult.setErrorCode(GlobalConstants.USER_PASSWORD_ERROR);
                    jsonResult.setMessage(GlobalConstants.USER_PASSWORD_ERROR_STR);
                }
            }catch (Exception e){
                jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            }
        }else{
            jsonResult.setErrorCode(GlobalConstants.USER_DOES_NOT_EXIST);
            jsonResult.setMessage(GlobalConstants.USER_DOES_NOT_EXIST_STR);
        }

        return jsonResult;
    }



    /**
     * 管理员退出
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态码
     */
    @RequestMapping(value = "/logout/admin", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        String id = httpServletRequest.getHeader("id");


        Jedis jedis = jedisUtil.getJedis();

        jedis.set("token"+id, "");
        jedis.set("permission_codes"+id, "");

        jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        return  jsonResult;
    }

}
