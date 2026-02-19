package com.tripease.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

                .route("flight-service-route", r -> r.path("/flight/**")
                        .uri("lb://flight-service"))
                // Using Load Balancer (Eureka/Consul)


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
}
