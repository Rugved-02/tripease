//package com.tripease.hotels.filter;
//
//import com.tripease.hotels.config.GlobalSecurityStore;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//
//@Component
//@Slf4j
//public class InternalAuthFilter extends OncePerRequestFilter {
//
//    @Value("${api.gateway.internal-secret}")
//    private String internalSecret;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        log.info("InternalAuthFilter invoked for URI: {}", request.getRequestURI());
//        String secret = request.getHeader("X-Internal-Secret");
//        String userId = request.getHeader("X-User-Id");
//        String email = request.getHeader("X-User-Email");
//
// //        to store these headers globally
//        GlobalSecurityStore.store.put("authData",new String[]{userId, email, secret});
//
//        log.info("Received Headers: X-Internal-Secret={}, X-User-Id={}, X-User-Email={}", secret, userId, email);
//
//        // 1. Validate Secret
//        if (!internalSecret.equals(secret)) {
//            log.error("Forbidden: Invalid X-Internal-Secret header");
//            response.sendError(403, "Direct access forbidden");
//            return;
//        }
//
//        if (userId != null && email != null) {
//            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                    userId, null, new ArrayList<>()
//            );
//            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
////            SecurityContextHolder.getContext().setAuthentication(auth);
//
//            SecurityContext context = SecurityContextHolder.createEmptyContext();
//            context.setAuthentication(auth);
//            SecurityContextHolder.setContext(context);
// //
// //            // Explicitly save context for stateless setup
// //            new RequestAttributeSecurityContextRepository().saveContext(context, request, response);
//
//            log.info("SecurityContext set for User: {}, Authentication: {}", userId, SecurityContextHolder.getContext().getAuthentication());
//        } else {
//            log.error("userId or roles header missing");
//        }
//
//        log.info("FILTER THREAD: {} | URI: {}", Thread.currentThread().getName(), request.getRequestURI());
//
//        // 4. Continue the chain
//        filterChain.doFilter(request, response);
//        log.info("After Filter: Authentication is {}", SecurityContextHolder.getContext().getAuthentication());
//    }
//}

package com.tripease.hotels.filter;

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

@Component
@Slf4j
public class InternalAuthFilter extends OncePerRequestFilter {

    @Value("${api.gateway.internal-secret}")
    private String internalSecret;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        String method = request.getMethod();

        // 1. Always skip filter for /error (regardless of GET/POST/etc)
        if (path.equals("/error")) {
            return true;
        }
        // 2. Skip filter ONLY for GET calls on hotel search paths
        boolean isGetCall = "GET".equalsIgnoreCase(method);
        boolean isHotelPath = path.equals("/hotel/search") || path.equals("/hotel");

        return isGetCall && isHotelPath;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        String secret = request.getHeader("X-Internal-Secret");
        String userId = request.getHeader("X-User-Id");
        String email = request.getHeader("X-User-Email");

        // 1. IF NO SECRET: Treat as a public request (from Frontend)
        // We let it pass to the SecurityConfig to see if the URL is 'permitAll()'
        if (secret == null) {
            log.info("Public access request for: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        // 2. IF SECRET EXISTS: Validate it (Internal Service-to-Service call)
        if (!internalSecret.equals(secret)) {
            log.error("Forbidden: Invalid internal secret provided");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Direct access forbidden");
            return;
        }

        // 3. Set Security Context if User headers are provided by Gateway
        if (userId != null && email != null) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userId, null, new ArrayList<>()
            );
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            
            log.info("Internal Authentication successful for user: {}", userId);
        }

        // 4. Continue the chain
        filterChain.doFilter(request, response);
    }
}