package com.tripease.analytics.config;

import com.tripease.analytics.filter.InternalAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
    @EnableWebSecurity
    public class SecurityConfig {

//        @PostConstruct
//        public void setup() {
//            // This allows child threads (like Feign) to inherit the request attributes
//            RequestContextHolder.setRequestAttributes(null, true);
//        }

//        @PostConstruct
//        public void init() {
//            SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http, InternalAuthFilter internalAuthFilter) throws Exception {
            return http
                    .csrf(csrf -> csrf.disable())
                    .formLogin(form -> form.disable())
                    .httpBasic(basic -> basic.disable())

                    // 1. Force Stateless
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                    // 2. CHANGE THIS: Explicitly permit your internal filter to manage the context
                    // By default, Spring 6 expect the SecurityContext to be saved to a Repository.
                    // In a stateless internal filter, we want to skip that requirement.
                    .securityContext(context -> context.requireExplicitSave(false))

                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/error").permitAll()
                            .anyRequest().authenticated()
                    )

                    // 3. BEST PRACTICE: Place it before the very first filter in the chain
                    // In stateless apps, SecurityContextHolderFilter is usually the first.
                    .addFilterBefore(internalAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }
    }
