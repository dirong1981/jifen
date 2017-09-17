package com.gljr.jifen.filter;

import com.gljr.jifen.common.JedisUtil;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.JwtUtil;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.AdminOnline;
import com.gljr.jifen.service.AdminService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


public class TokenIntecepter implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminService adminService;

    @Autowired
    private StrUtil strUtil;

    @Autowired
    private JedisUtil jedisUtil;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        //获取请求地址
        String url =httpServletRequest.getRequestURL().toString();



        String method = httpServletRequest.getMethod();

            //由于跨域请求，先要发送一个Option的方法来判断是否允许，所以要排除option这种请求
        if(!method.equals("OPTIONS")){

//            String userAgent = httpServletRequest.getHeader("user-agent");
            String device = httpServletRequest.getHeader("device");
//            if(userAgent.indexOf("Android") != -1 || userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("iPad") != -1){
            if(device.equals("mobile")){
                    //安卓
                return true;
            }else{
                    //电脑

                String token = httpServletRequest.getHeader("token");
                String id = httpServletRequest.getHeader("id");

                Jedis jedis = jedisUtil.getJedis();
                String jedistoken = jedis.get("token"+id);
                JsonResult jsonResult = new JsonResult();

                if(token == null){
                    jsonResult.setErrorCode(GlobalConstants.NOT_LOGIN);
                    strUtil.dealErrorReturn(httpServletRequest,httpServletResponse,jsonResult);
                    return false;
                }else{

                    try {
                        //if(httpSession.getAttribute("token") != null) {
                        //获取session中的token
                        if(jedistoken.equals(token)) {

                            List<AdminOnline> list = adminService.selectAdminOnlines(Integer.parseInt(id), new Byte("2"));

                            AdminOnline adminOnline = list.get(0);

                            Claims claims = jwtUtil.parseJWT(token, adminOnline.getToken());

                            //String sub = claims.getSubject();
                            String subject = claims.getSubject();
                            Date exp = claims.getExpiration();
                            Date now = new Date(System.currentTimeMillis());
                            if (exp.before(now)) {
                                jsonResult.setErrorCode(GlobalConstants.TOKEN_EXPIRED);
                                strUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
                                return false;
                            }
                            return true;
                        }else{
                            //token验证不相同
                            jsonResult.setErrorCode(GlobalConstants.NOT_LOGIN);
                            strUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
                            return false;
                        }

//                    }else{
//                        //session不存在
//                        jsonResult.setErrorCode(GlobalConstants.NOT_LOGIN);
//                        strUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
//                        System.out.println("nosession");
//                        return false;
//                    }


                    }catch (Exception e){
                        jsonResult.setErrorCode(GlobalConstants.NOT_LOGIN);
                        strUtil.dealErrorReturn(httpServletRequest, httpServletResponse, jsonResult);
                        return false;
                    }

                }
            }
        }else{
            return true;
        }



        //return true;



        //return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }



}
