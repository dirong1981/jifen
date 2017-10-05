package com.gljr.jifen.common.dtchain.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

public class SignatureUtil {

    private static final String CONTENT_CHARSET = "UTF-8";

    private static final String HMAC_ALG_SHA256 = "HmacSHA256";

    public static final String HEADER_INVOKE_ACCESS_KEY = "x-ak";
    public static final String HEADER_INVOKE_TIMESTAMP = "x-ts";
    public static final String HEADER_INVOKE_NONCE = "x-nonce";
    public static final String HEADER_INVOKE_SIGNATURE = "x-sign";

    public static String sign(String signStr, String secret)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_ALG_SHA256);

        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
        mac.init(secretKey);
        byte[] hash = mac.doFinal(signStr.getBytes(CONTENT_CHARSET));

        return new String(Base64.encode(hash));
    }

    public static String makeSignPlainText(TreeMap<String, Object> requestParams, String requestMethod, String requestPath) {
        String retStr = requestMethod + ":";
        retStr += requestPath;
        retStr += buildParamStr(requestParams);

        return retStr;
    }

    private static String buildParamStr(TreeMap<String, Object> requestParams) {
        String retStr = "";

        for (String key : requestParams.keySet()) {
            retStr += (retStr.length() == 0 ? "?" : "&") + key + '=' + requestParams.get(key).toString();
        }

        return retStr;
    }

}
