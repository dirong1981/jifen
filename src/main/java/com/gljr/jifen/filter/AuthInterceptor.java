package com.gljr.jifen.filter;

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


public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private StrUtil strUtil;

    @Autowired
    private JedisUtil jedisUtil;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {

            //从传入的handler中检查是否有AuthCheck的声明
        if (handler instanceof HandlerMethod) {

//            String userAgent = httpServletRequest.getHeader("user-agent");
            String device = httpServletRequest.getHeader("device");
//            if(userAgent.indexOf("Android") != -1 || userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("iPad") != -1){
            if(device.equals("mobile")){
                return true;
            }else{
                HandlerMethod method = (HandlerMethod) handler;
                AuthPassport authPassport = method.getMethodAnnotation(AuthPassport.class);

                String id = httpServletRequest.getHeader("id");
                String permission_codes = "";


                Jedis jedis = jedisUtil.getJedis();
                permission_codes = jedis.get("permission_codes"+id);

                //没有声明需要权限,或者声明不验证权限
                if (authPassport == null || authPassport.validate() == false) {
                    return true;
                } else {

                    try {
                        //通过id查询用户的权限
                        String permission_code = authPassport.permission_code();

                        //如果不包含权限，则不允许访问
                        if (permission_codes.contains(permission_code)) {
                            return true;
                        } else {
                            JsonResult jsonResult = new JsonResult();
                            jsonResult.setErrorCode(GlobalConstants.FORBIDDEN_CODE);
                            strUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
                            return false;
                        }
                    }catch (Exception e){
                        return false;
                    }
                }
            }
        }else{

            //不能被转化为方法，直接通过
            return true;
        }



    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
