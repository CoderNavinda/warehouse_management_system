package com.navinda.wms_api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

        @Bean
        public RouteLocator routes(RouteLocatorBuilder builder) {
                return builder.routes()
                                .route("inventory-service", r -> r.path("/inventory/**")
                                                .uri("http://localhost:8081"))
                                .route("order-service", r -> r.path("/orders/**")
                                                .uri("http://localhost:8082"))
                                .route("shipping-service", r -> r.path("/shipments/**")
                                                .uri("http://localhost:8083"))
                                .route("billing-service", r -> r.path("/invoices/**")
                                                .uri("http://localhost:8086"))
                                .build();
        }
}
