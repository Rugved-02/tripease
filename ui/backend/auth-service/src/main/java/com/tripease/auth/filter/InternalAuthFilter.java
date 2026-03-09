package com.tripease.auth.filter;

import com.tripease.auth.config.GlobalSecurityStore;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
@Slf4j
public class InternalAuthFilter extends OncePerRequestFilter {


    @Value("${api.gateway.internal-secret}")
    private String internalSecret;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        // 1. Skip filter for /error
        if ("/error".equals(path)) {
            return true;
        }

        // 2. Skip filter for ALL methods (GET, POST, etc.) for these specific auth paths
        List<String> authPaths = Arrays.asList(
                "/auth/login",
                "/auth/register",
                "/auth/forgotPassword",
                "/auth/reset-password"
        );

        if (authPaths.contains(path)) {
            return true;
        }

        // 3. For all other paths, execute the filter
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("InternalAuthFilter invoked for URI: {}", request.getRequestURI());
        String secret = request.getHeader("X-Internal-Secret");
        String userId = request.getHeader("X-User-Id");
        String email = request.getHeader("X-User-Email");

//        to store these headers globally
        GlobalSecurityStore.store.put("authData",new String[]{userId, email, secret});

        log.info("Received Headers: X-Internal-Secret={}, X-User-Id={}, X-User-Email={}", secret, userId, email);

        // 1. Validate Secret
        if (!internalSecret.equals(secret)) {
            log.error("Forbidden: Invalid X-Internal-Secret header");
            response.sendError(403, "Direct access forbidden");
            return;
        }

        if (userId != null && email != null) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userId, null, new ArrayList<>()
            );
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

//            SecurityContextHolder.getContext().setAuthentication(auth);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
//
//            // Explicitly save context for stateless setup
//            new RequestAttributeSecurityContextRepository().saveContext(context, request, response);

            log.info("SecurityContext set for User: {}, Authentication: {}", userId, SecurityContextHolder.getContext().getAuthentication());
        } else {
            log.error("userId or roles header missing");
        }

        log.info("FILTER THREAD: {} | URI: {}", Thread.currentThread().getName(), request.getRequestURI());

        // 4. Continue the chain
        filterChain.doFilter(request, response);
        log.info("After Filter: Authentication is {}", SecurityContextHolder.getContext().getAuthentication());
    }
}