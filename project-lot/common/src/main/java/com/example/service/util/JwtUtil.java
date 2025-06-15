package com.example.service.util;

import com.example.model.user.Users;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String secret= "mySuperSecretKey123456789012345678901234 "; // 从配置文件读取密钥

    private SecretKey secretKey;

    private static final long EXPIRATION = 24 * 60 * 60 * 1000L; // 1 天

    @PostConstruct
    public void init() {
        // 初始化密钥，避免每次都创建
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 JWT token
     */
    public String generateToken(Users user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从 token 中解析用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * 获取角色信息（可选）
     */
    public String getRoleFromToken(String token) {
        return (String) getClaims(token).get("role");
    }

    /**
     * 校验 token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token 已过期");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            System.out.println("Token 非法");
        } catch (IllegalArgumentException e) {
            System.out.println("Token 为空");
        }
        return false;
    }

    /**
     * 解析 Claims
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
