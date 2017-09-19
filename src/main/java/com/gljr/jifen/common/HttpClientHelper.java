package com.gljr.jifen.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Map;

public class HttpClientHelper {

    public static String httpClientGet(String urlParam) {
        StringBuffer resultBuffer = null;
        HttpClient client = new DefaultHttpClient();
        BufferedReader br = null;
        // 构建请求参数
//        StringBuffer sbParams = new StringBuffer();
//        if (params != null && params.size() > 0) {
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//                sbParams.append(entry.getKey());
//                sbParams.append("=");
//                try {
//                    sbParams.append(URLEncoder.encode(String.valueOf(entry.getValue()), charset));
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                sbParams.append("&");
//            }
//        }
//        if (sbParams != null && sbParams.length() > 0) {
//            urlParam = urlParam + "?" + sbParams.substring(0, sbParams.length() - 1);
//        }
//        System.out.println(urlParam);
        HttpGet httpGet = new HttpGet(urlParam);
        try {
            HttpResponse response = client.execute(httpGet);
            // 读取服务器响应数据
            br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String temp;
            resultBuffer = new StringBuffer();
            while ((temp = br.readLine()) != null) {
                resultBuffer.append(temp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                    throw new RuntimeException(e);
                }
            }
        }
        return resultBuffer.toString();
    }
}
