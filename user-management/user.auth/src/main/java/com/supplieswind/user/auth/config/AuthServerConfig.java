package com.supplieswind.user.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Configuration
//@Import(OAuth2AuthorizationServerConfiguration.class) // automatically registers an AuthorizationServerSettings @Bean, if not already provided.
public class AuthServerConfig {
    @Value("${security.oauth2.authorizationserver.client.user-client.registration.client-id}")
    private String clientId;

    @Value("${security.oauth2.authorizationserver.client.user-client.registration.client-secret}")
    private String clientSecret;

    @Value("${security.oauth2.authorizationserver.client.user-client.registration.token-validity-seconds}")
    private int tokenValidityInSecs;

    @Value("${security.oauth2.authorizationserver.client.user-client.registration.refresh-token-validity-seconds}")
    private int refreshTokenValidityInSecs;

    @Value("${security.oauth2.authorizationserver.issuer}")
    private String issuerUri;

    private enum Claims {
        TOKEN_TYPE("token_type"),
        ROLES("roles"),
        USERNAME("username");

        private final String name;

        Claims(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final String ID_TOKEN_TYPE = "id_token";
    private static final String ACCESS_TOKEN_TYPE = "access_token";

    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());

        http
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login")))
                .oauth2ResourceServer(configurer -> configurer.jwt(Customizer.withDefaults()));

        return http.build();
    }

    // this works for a single client or a couple of clients(internal apps)
    // but when we offer a service to a whole list of clients that are paying on demand for our data
    // we need to put clients in our database with a service, repository
    // see https://docs.spring.io/spring-authorization-server/reference/guides/how-to-jpa.html
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        var registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(new BCryptPasswordEncoder(12).encode(clientSecret))

                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)

                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)

                .scope(OidcScopes.OPENID)

                .tokenSettings(tokenSettings())
                .clientSettings(clientSettings())

                // the redirection after success, should be the same requested to /oauth2/token
                // as redirect_uri key
                .redirectUri("https://oauthdebugger.com/debug")

                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public TokenSettings tokenSettings() {
        return TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofSeconds(tokenValidityInSecs))
                .refreshTokenTimeToLive(Duration.ofSeconds(refreshTokenValidityInSecs))
                .build();
    }

    @Bean
    public ClientSettings clientSettings() {
        return ClientSettings.builder()
                .requireProofKey(true)
                .build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .authorizationEndpoint("/oauth2/authorize")
                .deviceAuthorizationEndpoint("/oauth2/device_authorization")
                .deviceVerificationEndpoint("/oauth2/device_verification")
                .tokenEndpoint("/oauth2/token")
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                .tokenRevocationEndpoint("/oauth2/revoke")
                .jwkSetEndpoint("/oauth2/jwks")
                .issuer(issuerUri) // If the issuer identifier is not configured, it is resolved from the current request.
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        JWKSet jwkSet = new JWKSet(generateRsaKey());
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static RSAKey generateRsaKey() {
        KeyPair keyPair = generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private static KeyPair generateKeyPair() {
        KeyPair keyPair;

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);

            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        return keyPair;
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            var principal = context.getPrincipal();

            var tokenType = context.getTokenType();
            if(isNull(tokenType) || isNull(tokenType.getValue())) {
                throw new RuntimeException("Invalid token in context");
            }

            var claims = context.getClaims();
            if(isNull(claims)) {
                throw new RuntimeException("Invalid context claims");
            }

            var typeValue = tokenType.getValue();

            if(typeValue.equals(ID_TOKEN_TYPE)) {
                claims.claim(Claims.TOKEN_TYPE.getName(), ID_TOKEN_TYPE);
            }

            else if(typeValue.equals(ACCESS_TOKEN_TYPE)) {
                processAccessToken(principal, claims);
            }
        };
    }

    private static void processAccessToken(Authentication principal, JwtClaimsSet.Builder claims) {
        claims.claim(Claims.TOKEN_TYPE.getName(), ACCESS_TOKEN_TYPE);

        Set<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        claims
                .claim(Claims.ROLES.getName(), roles)
                .claim(Claims.USERNAME.getName(), principal.getName());
    }
}
