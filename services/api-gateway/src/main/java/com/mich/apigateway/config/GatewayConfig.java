package com.mich.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("customer-service", r -> r.path("/api/v1/customers/**")
                        .uri("lb://CUSTOMER-SERVICE"))

                .route("product-service", r -> r.path("/api/v1/products/**")
                        .uri("lb://PRODUCT-SERVICE"))

                .route("order-service", r -> r.path("/api/v1/orders/**", "/api/v1/order-lines/**")
                        .uri("lb://ORDER-SERVICE"))

                .route("payment-service", r -> r.path("/api/v1/payments/**")
                        .uri("lb://PAYMENT-SERVICE"))

                .build();
    }
}