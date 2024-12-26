package org.cheems.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     * 生成jwt, 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        //指定签名加密算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //jwt过期时间
        long expiryMillis = System.currentTimeMillis() + ttlMillis;
        Date expiryDate = new Date(expiryMillis);


        // 生成 JWT
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(expiryDate)
                .compact();
    }
}
