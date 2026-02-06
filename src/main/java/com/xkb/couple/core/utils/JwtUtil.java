package com.xkb.couple.core.utils;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Date;


/**
 * JWT工具类
 * @author xuwatermelon
 * @date 2023/5/23 14:07
 */
@Slf4j
@Component
public class JwtUtil {
    /**
     * 注入密钥
     */
    @Value("${couple.jwt.secret}")
    private String secret;
    /**
     * 过期时间，单位：秒
     */
    @Value("${couple.jwt.expiration}")
    private Long expiration;


    /**
     * 根据用户ID生成JWT
     *
     * @param uid       用户ID
     * @return JWT
     */
    public String generateToken(Long uid) {
        // 计算过期时间
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setSubject(uid.toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    /**
     * 解析JWT返回用户ID
     *
     * @param token     JWT
     * @return 用户ID
     */
    public Long parseToken(String token) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
                return Long.parseLong(claimsJws.getBody().getSubject());

        } catch (JwtException e) {
            log.warn("JWT解析失败:{}", token);
        }
        return null;
    }

    /**
     * 判断Token是否即将过期（比如剩余时间小于5分钟）
     */
    public boolean isTokenExpiringSoon(String token) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            long expirationTime = claimsJws.getBody().getExpiration().getTime();
            long currentTime = System.currentTimeMillis();
            // 剩余时间小于5分钟（300000毫秒）则认为即将过期
            return (expirationTime - currentTime) < 300000;

        } catch (Exception e) {
            log.warn("判断Token过期状态异常:{}", token);
        }
        return false;
    }
}
