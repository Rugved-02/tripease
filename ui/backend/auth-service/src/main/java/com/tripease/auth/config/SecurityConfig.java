package com.tripease.auth.config;

import com.tripease.auth.filter.InternalAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.tripease.auth.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final InternalAuthFilter internalAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Initializing Security Filter Chain for AuthService...");

        http
                // 1. Disable CORS here because the Gateway handles it.
                // This prevents the "Multiple Values" error in the browser.
                .cors(cors -> cors.disable())

                // 2. Disable CSRF for REST APIs
                .csrf(csrf -> csrf.disable())

                // 3. Stateless sessions for JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Endpoint Permissions
                .authorizeHttpRequests(auth -> auth
                        // Allow Preflight OPTIONS requests
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        // Public Auth Endpoints
                        .requestMatchers("/auth/login", "/auth/register", "/auth/forgotPassword", "/auth/reset-password").permitAll()
                        // Everything else requires a token
                        .anyRequest().authenticated()
                )

                // 5. Add JWT Filter
                .addFilterBefore(internalAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}