package com.gljr.jifen.common;

import com.gljr.jifen.constants.GlobalConstants;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

@Component
public class StrUtil {

    //生成随机字符串
    public String randomKey(int length){
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    // 在拦截器中的输出
    @ResponseBody
    public void dealErrorReturn(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj){

        //设置返回数据的响应头
        httpServletResponse.setHeader("Access-Control-Allow-Origin", GlobalConstants.DOMAIN);
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            /* 设置响应头为Json */
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");


        PrintWriter writer = null;
        try {
            JSONObject jsonObject = new JSONObject(obj);
            writer = httpServletResponse.getWriter();
            writer.print(jsonObject);
        } catch (IOException ex) {
            // logger.error("response error",ex);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
