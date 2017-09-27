package com.gljr.jifen.filter;

import com.gljr.jifen.common.ClientType;
import com.gljr.jifen.common.JedisUtil;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


public class AuthInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {

            //从传入的handler中检查是否有AuthCheck的声明
        if (handler instanceof HandlerMethod) {

            JsonResult jsonResult = new JsonResult();
            jsonResult.setMessage(GlobalConstants.AUTH_FAILED);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);

            try {


                String device = httpServletRequest.getHeader("device");

                int clientType = ClientType.checkClientType(device);

                //获取路径
                String requestUri = httpServletRequest.getRequestURI();

                if (requestUri.contains("/manager/")) {
                    if (clientType == 2) {

                        String aid = httpServletRequest.getHeader("aid");
                        String permission = httpServletRequest.getHeader("permission");

                        Jedis jedis = JedisUtil.getJedis();

                        List admin = jedis.hmget("admin" + aid, "username", "permission");


                        if (permission == null || permission.equals("") || permission.equals("NULL")) {
                            StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
                            return false;
                        }

                        //获取服务器端管理员信息

                        String permissions = (String)admin.get(1);


                        if(permissions.contains(permission)){
                            return true;
                        }



                        StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
                        return false;

                    } else {
                        StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
                        return false;
                    }
                }
            }catch (Exception e){
                StrUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
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
