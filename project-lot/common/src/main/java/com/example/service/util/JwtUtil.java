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

    // 秘钥（必须长度足够，否则会报错）
    private static final String SECRET = "mySuperSecretKey123456789012345678901234";
    private static final long EXPIRATION = 24 * 60 * 60 * 1000L; // 1 天

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 Token
     */
    public String generateToken(Users user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole()) // admin / user / maintain
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 校验 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token); // 只要能解析就是合法
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 获取用户名（Subject）
     */
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * 获取角色
     */
    public String getRoleFromToken(String token) {
        return (String) getClaims(token).get("role");
    }

    /**
     * 判断是否是某个角色（admin / user / maintain）
     */
    public boolean hasRole(String token, String role) {
        String userRole = getRoleFromToken(token);
        return userRole != null && userRole.equalsIgnoreCase(role);
    }

    /**
     * 是否是管理员
     */
    public boolean isAdmin(String token) {
        return hasRole(token, "admin");
    }

    /**
     * 是否是普通用户
     */
    public boolean isUser(String token) {
        return hasRole(token, "user");
    }

    /**
     * 是否是运维人员
     */
    public boolean isMaintain(String token) {
        return hasRole(token, "maintain");
    }

    /**
     * 获取所有 Claims 信息
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
