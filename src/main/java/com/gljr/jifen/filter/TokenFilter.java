package com.gljr.jifen.filter;

import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * token过滤器，token校验不通过的提示未登录
 *
 * Created by Administrator on 2017/9/27 0027.
 */
public class TokenFilter implements Filter{

    private static final String[] NOT_CHECK_TOKEN_PATHS = {"/storeUser/login", "/login/admin"};

    private static AdminService adminService;

    static {
        WebApplicationContext wc = ContextLoader.getCurrentWebApplicationContext();
        adminService = (AdminService) wc.getBean("adminService");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String servletPath = httpServletRequest.getServletPath();
        JSONObject jsonObject = new JSONObject();
        for(String path : NOT_CHECK_TOKEN_PATHS){
            //如果需要检验token
            if(!StringUtils.startsWith(servletPath, path)){
                String uid = httpServletRequest.getParameter("uid");
                String token = httpServletRequest.getParameter("token");
                boolean checkTokenResult = adminService.checkToken(uid, token);
                //校验失败返回
                if(!checkTokenResult){
                    jsonObject.put("errorCode", GlobalConstants.USER_NOT_LOGIN[0]);
                    jsonObject.put("message", GlobalConstants.USER_NOT_LOGIN[1]);
                    printJson(httpServletResponse, jsonObject.toString());
                    return;
                }
                break;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private void printJson(HttpServletResponse response, String json){
        try {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
