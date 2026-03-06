package com.tripease.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//            filterChain.doFilter(request, response);
//            return;
//        }

        final String authHeader = request.getHeader("Authorization");
        final String path = request.getServletPath();
        
        log.debug("Processing request for path: {}", path);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String email = jwtUtil.extractEmail(token);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.validateToken(token)) {
                        log.info("JWT validated successfully for user: {}", email);
                        
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                email, null, new ArrayList<>());
                        
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        log.warn("Invalid JWT token provided for path: {}", path);
                    }
                }
            } 
            catch (Exception e) {
                // Log the exception but let the filter chain continue 
                // Spring Security will handle the unauthorized access later
                log.error("Authentication failed: {}", e.getMessage());
            }
        	} 
        else {
            // This is normal for public endpoints like /login or /register
            log.trace("No Bearer token found in request header for path: {}", path);
        }
        filterChain.doFilter(request, response);
    }
}