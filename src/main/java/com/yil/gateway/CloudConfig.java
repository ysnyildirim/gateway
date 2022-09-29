package com.yil.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudConfig {

    @Bean
    public RouteLocator gwRoutes(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("personModule", r -> r.path("/api/prs/v1/**").uri("http://localhost:8083/"))
                .build();
    }

}
