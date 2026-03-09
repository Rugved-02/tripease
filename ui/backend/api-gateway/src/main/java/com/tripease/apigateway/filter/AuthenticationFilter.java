package com.tripease.apigateway.filter;

import com.tripease.apigateway.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtService jwtService;

    @Value("${api.gateway.internal-secret}")
    private String internalSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("Inside filter of Api Gateway");
        ServerHttpRequest request = exchange.getRequest();

        // 1. Skip Auth for Login/Register (Auth Service)
        if (isPublic(request)) return chain.filter(exchange);

        // 2. Extract Token
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("Auth Header Token : "+authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("authHeader is null");
            return unAuthorized(exchange);
        }
        String token = authHeader.substring(7);

        log.info("Token :"+token);

        try {
            // 3. Validate and Extract Claims
            Claims claims = jwtService.extractAllClaims(token);
            log.info("Claims Extracted"+claims);

            String userId = claims.get("userId", String.class);
            String email = claims.get("email", String.class);

            log.info("Extracted userId: " + userId);
            log.info("Extracted email: " + email);

            if (userId == null || email == null || email.isEmpty()) {
                log.error("userId or roles missing in JWT claims");
                return unAuthorized(exchange);
            }

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Email", email)
                    .header("X-Internal-Secret", internalSecret)
                    .build();

            log.info("Headers set: X-User-Id={}, X-User-Email={}, X-Internal-Secret {}", userId, email, internalSecret);

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            log.error(e.getMessage());
            return unAuthorized(exchange);
        }
    }

    private boolean isPublic(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        // Original auth logic
        if (path.contains("/auth/register") || path.contains("/auth/login")) {
            return true;
        }

        // Strict check for GET calls on flight and hotel search
        if (HttpMethod.GET.equals(method)) {
            return path.contains("/flight/search") || path.contains("/flight") || path.contains("/hotel/search") || path.contains("/hotel");
        }

        return false;
    }

    private Mono<Void> unAuthorized(ServerWebExchange exchange) {
        log.info("Unauthorizes through unAuthorized()");
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() { return -1; }
}
