/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.gateway;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //kullanıcı oluşturma , login ve refresh işlemlerinde token isteme
        final List<String> apiEndpoints = List.of("/api/acc/v1/users",
                "/api/acc/v1/auth/login",
                "/api/acc/v1/auth/refresh");
        Predicate<ServerHttpRequest> isApiSecured = r -> {
            return !(r.getMethod().compareTo(HttpMethod.POST) == 0
                     &&
                     apiEndpoints
                             .stream()
                             .anyMatch(uri -> r.getURI()
                                     .getPath()
                                     .endsWith(uri)));
        };
        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            final String token = request.getHeaders().getOrEmpty("Authorization").get(0);
            try {
                Claims claims = jwtUtil.getClaims(token);
                exchange.getRequest()
                        .mutate()
                        .header("Authenticated-User-Id", String.valueOf(claims.get("userId")))
                        .build();
            } catch (Exception e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.setComplete();
            }
        }
        return chain.filter(exchange);
    }
}
