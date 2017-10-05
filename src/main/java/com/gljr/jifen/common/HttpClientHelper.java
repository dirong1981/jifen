package com.gljr.jifen.common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientHelper {

    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 60000;
    private static final String CHARSET_UTF8 = "UTF-8";

    static {
        /*// 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());*/

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }

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

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     *
     * @param url 请求url
     * @return String
     */
    public static String doGet(String url) {
        return doGet(url, new HashMap<String, Object>());
    }

    /**
     * 发送 GET 请求（HTTPS），不带输入数据
     *
     * @param url 请求url
     * @return String
     */
    public static String doGetSSL(String url) {
        return doGetSSL(url, new HashMap<String, Object>());
    }

    /**
     * 发送 GET 请求（HTTPS），K-V形式
     *
     * @param url 请求url
     * @param params 请求参数
     * @return String
     */
    public static String doGetSSL(String url, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setDefaultRequestConfig(requestConfig).build();
        return doGet(url, params, httpClient);
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url 请求url
     * @param params 请求参数
     * @return String
     */
    public static String doGet(String url, Map<String, Object> params) {
        return doGet(url, params, HttpClients.createDefault());
    }

    private static String doGet(String url, Map<String, Object> params, CloseableHttpClient httpclient){
        String apiUrl = url;
        HttpResponse response = null;
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (key.startsWith("x-")) {
                continue;
            }
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        String result = null;
        try {
            HttpGet httpPost = new HttpGet(apiUrl);
            for(String key : params.keySet()) {
                if (key.startsWith("x-")) {
                    httpPost.addHeader(key, params.get(key).toString());
                }
            }
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            result = EntityUtils.toString(entity, CHARSET_UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     *
     * @param url 请求url
     * @return String
     */
    public static String doPost(String url) {
        return doPost(url, new HashMap<String, Object>());
    }

    /**
     * 发送 POST 请求（HTTPS），不带输入数据
     *
     * @param url 请求url
     * @return String
     */
    public static String doPostSSL(String url) {
        return doPostSSL(url, new HashMap<String, Object>());
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     *
     * @param url API接口URL
     * @param params 参数map
     * @return String
     */
    public static String doPost(String url, Map<String, Object> params) {
        return doPost(url, params, HttpClients.createDefault());
    }



    /**
     * 发送 POST 请求（HTTP），JSON形式
     *
     * @param apiUrl
     * @param json   json对象
     * @return
     */
    public static String doPost(String apiUrl, Object json) {
        return doPost(apiUrl, json, HttpClients.createDefault());
    }

    /**
     * 发送 SSL POST 请求（HTTPS），K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPostSSL(String apiUrl, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setDefaultRequestConfig(requestConfig).build();
        return doPost(apiUrl, params, httpClient);
    }

    /**
     * 发送 SSL POST 请求（HTTPS），JSON形式
     *
     * @param apiUrl API接口URL
     * @param json   JSON对象
     * @return
     */
    public static String doPostSSL(String apiUrl, Object json) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setDefaultRequestConfig(requestConfig).build();
        return doPost(apiUrl, json, httpClient);
    }

    private static String doPost(String apiUrl, Object json, CloseableHttpClient httpClient){
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(), CHARSET_UTF8);//解决中文乱码问题
            stringEntity.setContentEncoding(CHARSET_UTF8);
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, CHARSET_UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    private static String doPost(String apiUrl, Map<String, Object> params, CloseableHttpClient httpClient){
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getKey().startsWith("x-")) {
                    httpPost.addHeader(entry.getKey(), entry.getValue().toString());
                    continue;
                }
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(CHARSET_UTF8)));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, CHARSET_UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 判断是否为https请求
     */
    public static boolean isHttpsRequst(String url) {
        return !(url == null || url.trim().equals("")) && url.toLowerCase().startsWith("https");
    }

    /**
     * 创建SSL安全连接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }
}
