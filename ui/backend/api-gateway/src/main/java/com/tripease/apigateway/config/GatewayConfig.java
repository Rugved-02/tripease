package com.tripease.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 1. Simple Path Matching
                .route("auth-user-service-route", r -> r.path("/auth/**")
                        .uri("lb://auth-service"))

                .route("payment-service-route", r -> r.path("/payment/**")
                        .uri("lb://payment-service"))

                .route("booking-service-route", r -> r.path("/booking/**")
                        .uri("lb://booking-service"))

                .route("hotel-service-route", r -> r.path("/hotel/**")
                        .uri("lb://hotel-service"))

                .route("flight-service-route", r -> r.path("/flight/**")
                        .uri("lb://flight-service"))

                .route("itinerary-service-route", r -> r.path("/itinerary/**")
                        .uri("lb://itinerary-service"))



                // 2. Path Rewrite (Removes /api prefix before hitting the service)
//                .route("order-service-route", r -> r.path("/api/orders/**")
//                        .filters(f -> f.rewritePath("/api/(?<segment>.*)", "/${segment}"))
//                        .uri("lb://ORDER-SERVICE"))
//
//                // 3. Header-based Routing
//                .route("beta-test-route", r -> r.header("X-Beta-Enabled", "true")
//                        .uri("http://beta-service.example.com"))

                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 1. Allow your Angular Origin
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:4000", "http://localhost:4200"));

        // 2. Allow common HTTP methods
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. Allow all headers (Content-Type, Authorization, etc.)
        corsConfig.setAllowedHeaders(Arrays.asList("*"));

        // 4. Allow credentials (cookies, authorization headers)
        corsConfig.setAllowCredentials(true);

        // 5. How long the browser should cache this "preflight" request
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this to all paths (/**)
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
