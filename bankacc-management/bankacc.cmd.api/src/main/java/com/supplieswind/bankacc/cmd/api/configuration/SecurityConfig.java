package com.supplieswind.bankacc.cmd.api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${security.oauth2.authenticationserver.issuer}")
    private String issuerUri;

    private final static String ROLES_CLAIM = "roles";
    private final static String AUTHORITY_PREFIX = "";

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .oauth2ResourceServer(ouath2 -> ouath2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(JwtDecoders.fromIssuerLocation(issuerUri))));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        grantedAuthoritiesConverter.setAuthoritiesClaimName(ROLES_CLAIM);
        grantedAuthoritiesConverter.setAuthorityPrefix(AUTHORITY_PREFIX);

        var authConverter = new JwtAuthenticationConverter();
        authConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return authConverter;
    }
}
