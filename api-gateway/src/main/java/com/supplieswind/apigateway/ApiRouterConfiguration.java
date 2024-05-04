package com.supplieswind.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class ApiRouterConfiguration {

    @Value("${uri.user.command-api}")
    private String userCmdApiUri;

    @Value("${uri.user.query-api}")
    private String userQryApiUri;

    @Value("${uri.bank-account.command-api}")
    private String bankAccountCmdApiUri;

    @Value("${uri.bank-account.query-api}")
    private String bankAccountQryApiUri;

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
                                .uri(userCmdApiUri))

                .route("user-qry-api",
                        predSpec -> predSpec
                                .method(HttpMethod.GET)
                                .and()
                                .path("/api/v1/users", "/api/v1/users/**")
                                .uri(userQryApiUri))

                .route("bank-account-cmd-api",
                        predSpec -> predSpec
                                .method(HttpMethod.POST,
                                        HttpMethod.PATCH,
                                        HttpMethod.DELETE)
                                .and()
                                .path("/api/v1/bank-account/**", "/api/v1/bank-account")
                                .uri(bankAccountCmdApiUri))

                .route("bank-account-qry-api",
                        predSpec -> predSpec
                                .method(HttpMethod.GET)
                                .and()
                                .path("/api/v1/bank-account/**", "/api/v1/bank-account")
                                .uri(bankAccountQryApiUri))

                .build();
    }
}
