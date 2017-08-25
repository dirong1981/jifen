package com.gljr.jifen.filter;

import com.gljr.jifen.constants.GlobalConstants;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionIntecepter implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletRequest.setAttribute("startTime", System.currentTimeMillis());

        String path = httpServletRequest.getContextPath();
        String basePath = httpServletRequest.getScheme()+"://"+httpServletRequest.getServerName()+":"+httpServletRequest.getServerPort()+path;


//        //无需登录，允许访问的地址
//        String[] allowUrls =new String[]{"/login","/"};
//
        //获取请求地址
        String url =httpServletRequest.getRequestURL().toString();
//
//
//        for(String strUrl : allowUrls){
//            if(url.contains(strUrl)){
//                return true;
//            }
//        }

        if(url.contains("/login")){
            return true;
        }

        HttpSession httpSession = httpServletRequest.getSession();
        if(httpSession.getAttribute(GlobalConstants.SESSION_ADMIN) == null){
           // System.out.printf("intecepter");
            httpServletResponse.sendRedirect(basePath + "/admin/login");

        }else{
            //System.out.printf("session ok");
           // httpServletResponse.sendRedirect(basePath + "/admin/index");
            return true;
        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
