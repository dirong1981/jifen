package com.gljr.jifen.common;

import com.gljr.jifen.constants.GlobalConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Random;

@Component
public class JwtUtil {



    /**
     * 由字符串生成加密key
     * @return
     */
    public static Key generalKey(String key){
//        byte[] encodedKey = Base64.decodeBase64(GlobalConstants.SECRET);
//        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        return signingKey;
    }

    /**
     * 创建jwt
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     * @throws Exception
     */
    public static String createJWT(String id, String subject, String tokenKey, long ttlMillis) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Key key = generalKey(tokenKey);
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                //.setIssuer()//发送给谁
                //.setAudience()//个人签名
                .setSubject(subject)
                .signWith(signatureAlgorithm, key);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            //系统时间之前的token都是不可以被认可的
            builder.setExpiration(exp).setNotBefore(now);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt, String tokenKey) throws Exception{
        Key key = generalKey(tokenKey);
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
        return claims;
    }



}
