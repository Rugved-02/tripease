package com.cts.authService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cts.authService.security.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private JwtAuthenticationFilter jwtFilter;
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. MUST disable CSRF for REST APIs to allow POST/PUT requests
            .csrf(csrf -> csrf.disable()) 
            
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 2. Permit all requests to your user endpoints
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login","/auth/register","/auth/forgotPassword").permitAll() // Adjust path to match your Controller
                .anyRequest().authenticated()
            )
            
            // 3. Disable the default login form that causes redirects
//            .formLogin(form -> form.disable())
//            .httpBasic(basic -> basic.disable());
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
