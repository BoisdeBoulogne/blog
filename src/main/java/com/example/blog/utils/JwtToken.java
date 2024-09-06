package com.example.blog.utils;

import com.example.blog.constants.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtToken {


    private String secretKey = JwtConstants.JWT_SECRET;
    private long validityInMilliseconds = 3600000; // 1小时

    // 生成JWT Token
    public String createToken(Map<String, Object> claims) {
        // 设置声明
        Claims jwtClaims = Jwts.claims();
        jwtClaims.putAll(claims); // 将额外的声明信息放入claims

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        // 生成JWT令牌
        return Jwts.builder()
                .setClaims(jwtClaims) // 设置声明
                .setIssuedAt(now)
                .setExpiration(validity) // 过期时间
                .signWith(SignatureAlgorithm.HS256, secretKey) // 使用HS256算法和秘钥签名
                .compact();
    }

    // 解析Token，获取声明
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT Token");
        }
    }


}
