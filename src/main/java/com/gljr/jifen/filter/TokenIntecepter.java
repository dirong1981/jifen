package com.gljr.jifen.filter;

import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.AdminOnline;
import com.gljr.jifen.pojo.OnlineOrder;
import com.gljr.jifen.service.AdminService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.Struct;
import java.util.Date;
import java.util.List;


public class TokenIntecepter implements HandlerInterceptor {


    @Autowired
    private AdminService adminService;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String method = httpServletRequest.getMethod();


        JsonResult jsonResult = new JsonResult();
        jsonResult.setMessage(GlobalConstants.ADMIN_LOGIN_FAILED);
        jsonResult.setErrorCode(GlobalConstants.NOT_LOGIN_CODE);


        //由于跨域请求，先要发送一个Option的方法来判断是否允许，所以要排除option这种请求
        if (method.equals("OPTIONS")) {
            return true;
        }

        String device = httpServletRequest.getHeader("device");

        int clientType = ClientType.checkClientType(device);

        //判断客户端类型
        if (clientType == 1 || clientType == 2) {

            //电脑用户，管理员管理后台
            try {
                if (clientType == 2) {
                    String token = httpServletRequest.getHeader("token");
                    String aid = httpServletRequest.getHeader("aid");
                    if (StringUtils.isEmpty(token) || StringUtils.isEmpty(aid)) {
                        StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
                        return false;
                    } else {
                        //获取服务器端管理员信息
                        Jedis jedis = JedisUtil.getJedis();
                        List<String> _admin = jedis.hmget("admin" + aid, "token", "aid", "permission", "type");

                        //判断是不是系统管理员登录
                        if(!_admin.get(3).equals("1")){
                            StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
                            return false;
                        }

                        //查询在线表中管理员信息
                        List<AdminOnline> adminOnlines = adminService.selectAdminOnlinesByAid(Integer.parseInt(_admin.get(1)), clientType);

                        String tokenKey = "";
                        //如果管理员存在，获取tokenkey，否则重新登录
                        if (adminOnlines != null && adminOnlines.size() != 0) {
                            tokenKey = adminOnlines.get(0).getToken();
                        } else {
                            StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
                            return false;
                        }


                        //解密token，失败重新登录，时间过期重新登录
                        Claims claims = JwtUtil.parseJWT(token, tokenKey);
                        Date exp = claims.getExpiration();
                        Date now = new Date(System.currentTimeMillis());
                        if (exp.before(now)) {
                            StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
                            return false;
                        }
                    }
                }
            }catch (Exception e){
                StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
                return false;
            }

            return true;

        } else {

            //用户没有登录或登录失效
            StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);


//            httpServletResponse.sendRedirect("http://localhost/admin/login.html");
            return false;
        }
    }



    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }



}
