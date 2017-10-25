package com.gljr.jifen.filter;

import com.gljr.jifen.common.ClientType;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.OptLogMapper;
import com.gljr.jifen.pojo.OptLog;
import com.gljr.jifen.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Autowired
    private OptLogMapper optLogMapper;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {

            //从传入的handler中检查是否有AuthCheck的声明
        if (handler instanceof HandlerMethod) {

            JsonResult jsonResult = new JsonResult();
            jsonResult.setMessage(GlobalConstants.AUTH_FAILED);
            jsonResult.setErrorCode(GlobalConstants.NOT_LOGIN_CODE);

            try {


                String device = httpServletRequest.getHeader("device");

                //获取路径
                String requestUri = httpServletRequest.getRequestURI();

                if(requestUri.contains("index/code")){
                    return true;
                }



                if(device.equals(DBConstants.ClientType.WEB.getDescription())){
                    HandlerMethod method = (HandlerMethod) handler;
                    AuthPassport authPassport = method.getMethodAnnotation(AuthPassport.class);

                    String aid = httpServletRequest.getHeader("aid");

                    OptLog optLog = new OptLog();
                    optLog.setAid(Integer.parseInt(aid));
                    optLog.setContent(requestUri);
                    optLog.setOptTime(new Timestamp(System.currentTimeMillis()));
                    if(httpServletRequest.getMethod().equals("GET")){
                        optLog.setOptType(1);
                    }
                    if(httpServletRequest.getMethod().equals("POST")){
                        optLog.setOptType(2);
                    }
                    if(httpServletRequest.getMethod().equals("DELETE")){
                        optLog.setOptType(3);
                    }
                    if(httpServletRequest.getMethod().equals("PUT")){
                        optLog.setOptType(4);
                    }

                    String[] a = requestUri.split("/");
                    if(a.length >= 4) {
                        optLog.setTableName(a[3]);
                    }else {
                        optLog.setTableName(a[2]);
                    }
                    optLogMapper.insert(optLog);


                    Map<String, String> tokenMap = this.redisService.getMap("admin_" + aid, String.class);

                    if (null == tokenMap || !tokenMap.containsKey("permission")) {
                        StrUtil.dealErrorReturn(httpServletResponse, jsonResult, GlobalConstants.WEBDOMAIN);
                        return false;
                    }

                    String permissions = tokenMap.get("permission");

                    //没有声明需要权限,或者声明不验证权限
                    if (authPassport != null && authPassport.validate() != false) {

                        try {
                            //通过id查询用户的权限
                            String permission = authPassport.permission_code();

                            //如果不包含权限，则不允许访问
                            if(!permissions.contains(permission)){
                                StrUtil.dealErrorReturn(httpServletResponse, jsonResult, GlobalConstants.WEBDOMAIN);
                                return false;
                            }
                        }catch (Exception e){
                            return false;
                        }
                    }
                }


            }catch (Exception e){
                System.out.println(e);
                StrUtil.dealErrorReturn(httpServletResponse, jsonResult, GlobalConstants.WEBDOMAIN);
                return false;
            }
        }
        return true;

    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
