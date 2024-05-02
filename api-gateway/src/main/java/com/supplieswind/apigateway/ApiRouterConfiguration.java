package com.supplieswind.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class ApiRouterConfiguration {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // auth.user endpoints are not here because it will make the auth token getting process
                // to fail because of the token URI(PKCE)
                .route("user-cmd-api",
                        predSpec -> predSpec
                                .method(
                                        HttpMethod.POST,
                                        HttpMethod.PUT,
                                        HttpMethod.DELETE)
                                .and()
                                .path("/api/v1/users", "/api/v1/users/**")
                                .uri("http://localhost:8081/**"))

                .route("user-qry-api",
                        predSpec -> predSpec
                                .method(HttpMethod.GET)
                                .and()
                                .path("/api/v1/users", "/api/v1/users/**")
                                .uri("http://localhost:8082/**"))

                .route("bank-account-cmd-api",
                        predSpec -> predSpec
                                .method(HttpMethod.POST)
                                .and()
                                .path("/api/v1/bank-account/**", "/api/v1/bank-account")
                                .uri("http://localhost:8084/**"))

                .build();
    }
}
