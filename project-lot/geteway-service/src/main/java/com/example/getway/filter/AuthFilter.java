package com.example.gateway.filter;

import com.example.service.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final List<String> WHITELIST = List.of(
            "/auth/login", "/auth/register", "/public/"
    );

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 1. 白名单放行
        if (isWhiteListed(path)) {
            return chain.filter(exchange);
        }

        // 2. 获取 Token
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return writeJson(exchange, 886, "未登录，请先登录");
        }

        String token = authHeader.substring(7); // 去掉 "Bearer "

        Claims claims;
        try {
            // 校验并获取 Claims
            if (!jwtUtil.validateToken(token)) {
                return writeJson(exchange, 886, "Token无效");
            }
            claims = jwtUtil.getClaims(token);
        } catch (Exception e) {
            return writeJson(exchange, 886, "Token无效");
        }

        // 3. 角色校验
        String role = claims.get("role", String.class);

        if (path.contains("/admin/") && !"admin".equals(role)) {
            return writeJson(exchange, 403, "权限不足");
        }

        if (path.contains("/maintain/") && !"maintain".equals(role)) {
            return writeJson(exchange, 403, "权限不足");
        }

        // 正常放行
        return chain.filter(exchange);
    }

    private boolean isWhiteListed(String path) {
        return WHITELIST.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> writeJson(ServerWebExchange exchange, int code, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK); // 状态码用200，错误通过业务code返回
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = String.format("{\"code\":%d,\"msg\":\"%s\"}", code, msg);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1; // 优先级最高
    }
}
