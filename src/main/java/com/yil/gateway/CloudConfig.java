package com.yil.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudConfig {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator gwRoutes(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("addressModule", r -> r.path("/api/address/v1/**").filters(f -> f.filter(filter)).uri("http://localhost:8081/"))
                .route("accountModule", r -> r.path("/api/account/v1/**").filters(f -> f.filter(filter)).uri("http://localhost:8082/"))
                .route("personModule", r -> r.path("/api/prs/v1/**").filters(f -> f.filter(filter)).uri("http://localhost:8083/"))
                .route("companyModule", r -> r.path("/api/cmp/v1/**").filters(f -> f.filter(filter)).uri("http://localhost:8084/"))
                .route("organizationModule", r -> r.path("/api/org/v1/**").filters(f -> f.filter(filter)).uri("http://localhost:8085/"))
                .route("contactModule", r -> r.path("/api/cnt/v1/**").filters(f -> f.filter(filter)).uri("http://localhost:8086/"))
                .route("workflowModule", r -> r.path("/api/wf/v1/**").filters(f -> f.filter(filter)).uri("http://localhost:8087/"))
                .route("educationModule", r -> r.path("/api/edu/v1/**").filters(f -> f.filter(filter)).uri("http://localhost:8088/"))
                .route("jobModule", r -> r.path("/api/job/v1/**").filters(f -> f.filter(filter)).uri("http://localhost:8089/"))
                .build();
    }


}
