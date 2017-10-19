package com.gljr.jifen.filter;

import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.AdminOnline;
import com.gljr.jifen.service.AdminService;
import com.gljr.jifen.service.RedisService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class TokenIntecepter implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;


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

        if(device.equals(DBConstants.ClientType.WEB.getDescription())){
            //从pc端进入，只能是商户管理员和系统管理员
            String aid = httpServletRequest.getHeader("aid");
            String token = httpServletRequest.getHeader("token");
            if(StringUtils.isEmpty(aid) || StringUtils.isEmpty(token)){
                StrUtil.dealErrorReturn(httpServletResponse, jsonResult, GlobalConstants.WEBDOMAIN);
                return false;
            }
            //获取服务器端管理员信息
            Map<String, String> tokenMap = this.redisService.getMap("admin_" + aid, String.class);

            if(null == tokenMap || !tokenMap.containsKey("tokenKey") || !tokenMap.containsKey("accountType") || !tokenMap.containsKey("aid")){
                StrUtil.dealErrorReturn(httpServletResponse, jsonResult, GlobalConstants.WEBDOMAIN);
                return false;
            }

            String tokenKey = tokenMap.get("tokenKey");

            //解密token，失败重新登录，时间过期重新登录
            try {
                Claims claims = JwtUtil.parseJWT(token, tokenKey);
                Date exp = claims.getExpiration();
                Date now = new Date(System.currentTimeMillis());
                if (exp.before(now)) {
                    StrUtil.dealErrorReturn(httpServletResponse, jsonResult, GlobalConstants.WEBDOMAIN);
                    return false;
                }
            }catch (Exception e){
                StrUtil.dealErrorReturn(httpServletResponse, jsonResult, GlobalConstants.WEBDOMAIN);
                return false;
            }

        }else if(device.equals("mobile")){
            //从手机端进入，只能是普通用户
            String uid = httpServletRequest.getHeader("uid");
            String token = httpServletRequest.getHeader("token");


            String requestUri = httpServletRequest.getRequestURI();
            String params = httpServletRequest.getParameter("token");
            if(!StringUtils.isEmpty(params)){
                return true;
            }else {

                if (requestUri.contains("order") || requestUri.contains("users")) {

                    jsonResult.setMessage(GlobalConstants.ADMIN_LOGIN_FAILED);
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);

                    if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(token)) {
                        StrUtil.dealErrorReturn(httpServletResponse, jsonResult, GlobalConstants.APPDOMAIN);
                        return false;
                    }

                    //获取服务器端管理员信息
                    Map<String, String> tokenMap = this.redisService.getMap("user_" + uid, String.class);


                    if (null == tokenMap || !tokenMap.containsKey("tokenKey")) {
                        StrUtil.dealErrorReturn(httpServletResponse, jsonResult, GlobalConstants.APPDOMAIN);
                        return false;
                    }

                    String tokenKey = tokenMap.get("tokenKey");

//                    System.out.println(tokenMap.get("token"));
//
                    //解密token，失败重新登录，时间过期重新登录
                    try {
                        Claims claims = JwtUtil.parseJWT(token, tokenKey);
                        Date exp = claims.getExpiration();
                        Date now = new Date(System.currentTimeMillis());
                        if (exp.before(now)) {
                            StrUtil.dealErrorReturn(httpServletResponse, jsonResult, GlobalConstants.APPDOMAIN);
                            return false;
                        }
                    } catch (Exception e) {
                        StrUtil.dealErrorReturn(httpServletResponse, jsonResult, GlobalConstants.APPDOMAIN);
                        return false;
                    }
                }
            }
            return true;
        }else if(device.equals(DBConstants.ClientType.APP.getDescription())) {
            return true;
        }else {
            StrUtil.dealErrorReturn(httpServletResponse, jsonResult, GlobalConstants.APPDOMAIN);
            return false;
        }
        return true;
    }



//        int clientType = ClientType.checkClientType(device);
//
//        //判断客户端类型
//        if (clientType == 1 || clientType == 2) {
//            //web用户，只允许系统管理员和商户管理员，验证他们的token
//            try {
//                if (clientType == 2){
//                    String token = httpServletRequest.getHeader("token");
//                    String aid = httpServletRequest.getHeader("aid");
//                    String accountType = httpServletRequest.getHeader("accountType");
//                    if (StringUtils.isEmpty(token) || StringUtils.isEmpty(aid) || StringUtils.isEmpty(accountType)) {
//                        StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
//                        return false;
//                    } else {
//                        //获取服务器端管理员信息
//                        Map<String, String> tokenMap = this.redisService.getMap("admin_" + aid, String.class);
//                        if (null == tokenMap || !tokenMap.containsKey("permission")
//                                || !tokenMap.containsKey("accountType") || !tokenMap.containsKey("aid")) {
//                            StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
//                            return false;
//                        }
//
//                        //判断是不是系统管理员登录
//                        if (!(DBConstants.AdminAccountType.SYS_ADMIN.getCode()+"").equals(tokenMap.get("accountType"))
//                                && !(DBConstants.AdminAccountType.STORE_ADMIN.getCode()+"").equals(tokenMap.get("accountType")) ) {
//                            StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
//                            return false;
//                        }
//
//                        //查询在线表中管理员信息
//                        List<AdminOnline> adminOnlines = adminService.selectAdminOnlinesByAid(Integer.parseInt(tokenMap.get("aid")), clientType);
//
//                        String tokenKey = "";
//                        //如果管理员存在，获取tokenkey，否则重新登录
//                        if (!ValidCheck.validList(adminOnlines)) {
//                            tokenKey = adminOnlines.get(0).getToken();
//                        } else {
//                            StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
//                            return false;
//                        }
//
//
//                        //解密token，失败重新登录，时间过期重新登录
//                        Claims claims = JwtUtil.parseJWT(token, tokenKey);
//                        Date exp = claims.getExpiration();
//                        Date now = new Date(System.currentTimeMillis());
//                        if (exp.before(now)) {
//                            StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
//                            return false;
//                        }
//                    }
//                }
//                else if(clientType == 1){
//                    String token = httpServletRequest.getHeader("token");
//                    String uid = httpServletRequest.getHeader("uid");
//                    if (StringUtils.isEmpty(token) || StringUtils.isEmpty(uid)) {
//                        StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
//                        return false;
//                    } else {
//                        //查询在线表中管理员信息
//                        List<AdminOnline> adminOnlines = adminService.selectAdminOnlinesByAid(Integer.parseInt(uid), clientType);
//
//                        String tokenKey = "";
//                        //如果管理员存在，获取tokenkey，否则重新登录
//                        if (adminOnlines != null && adminOnlines.size() != 0) {
//                            tokenKey = adminOnlines.get(0).getToken();
//                        } else {
//                            StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
//                            return false;
//                        }
//
//                        //解密token，失败重新登录，时间过期重新登录
//                        Claims claims = JwtUtil.parseJWT(token, tokenKey);
//                        Date exp = claims.getExpiration();
//                        Date now = new Date(System.currentTimeMillis());
//                        if (exp.before(now)) {
//                            StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
//                            return false;
//                        }
//                    }
//                }
//            }catch (Exception e){
//                StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
//                return false;
//            }
//
//            return true;
//
//        } else {
//
//            //用户没有登录或登录失效
//            StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
//
//
////            httpServletResponse.sendRedirect("http://localhost/admin/login.html");
//            return false;
//        }




    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }



}
