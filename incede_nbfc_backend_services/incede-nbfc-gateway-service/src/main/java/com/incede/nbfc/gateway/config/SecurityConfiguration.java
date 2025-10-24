package com.incede.nbfc.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Security configuration for the API Gateway using Spring WebFlux.
 * <p>
 * Responsibilities:
 * - Validates incoming JWT tokens.
 * - Ensures "realm_access" object exists in the JWT.
 * - Handles authentication failures such as expired or invalid tokens.
 * - Logs warnings for invalid or expired tokens.
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {


    /**
     * Configures the security filter chain for the gateway.
     *
     * @param http ServerHttpSecurity instance
     * @return configured SecurityWebFilterChain
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disable CSRF for API gateway
                .cors(ServerHttpSecurity.CorsSpec::disable) // CORS handled via YAML
                .authorizeExchange(ex -> ex.anyExchange().authenticated()) // All requests must be authenticated
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(realmAccessChecker()))
                        .authenticationEntryPoint(this::handleAuthenticationFailure) // Handle auth errors
                )
                .build();
    }

    /**
     * Custom JWT converter that checks if the "realm_access" object exists.
     * <p>
     * If the object is missing, returns 401 Unauthorized.
     *
     * @return Converter that validates JWT
     */
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> realmAccessChecker() {
        return jwt -> {
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");

            if (realmAccess == null) {
                log.info("JWT missing realm_access object: token={}", jwt.getTokenValue());
                return Mono.error(new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_token", "Missing realm_access object in token", null)
                ));
            }

            // Delegate actual conversion to default JwtAuthenticationConverter
            JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
            return Mono.just(delegate.convert(jwt));
        };
    }

    /**
     * Handles JWT authentication failures and returns a JSON response with HTTP 401.
     *
     * Sets the response message to "JWT token expired" if expired, or "Invalid JWT token" otherwise.
     * The response is sent as JSON for frontend consistency.
     *
     * @param exchange the current ServerWebExchange
     * @param ex the authentication exception
     * @return Mono<Void> after writing the response
     */

    private Mono<Void> handleAuthenticationFailure(org.springframework.web.server.ServerWebExchange exchange,
                                                   Throwable ex) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().set("Content-Type", "application/json");

        String message = "Invalid JWT token";

        if (ex instanceof OAuth2AuthenticationException oauthEx) {
            Throwable cause = oauthEx.getCause();
            if (cause != null && cause.getMessage() != null &&
                    cause.getMessage().toLowerCase().contains("expired")) {
                message = "JWT token expired";
                log.info("Expired JWT token: {}", cause.getMessage());
            } else {
                log.info("Invalid JWT token: {}", ex.getMessage());
            }
        } else {
            log.info("Authentication failure: {}", ex.getMessage());
        }

        // Build JSON response
        String json = String.format(
                "{\"timestamp\":\"%s\",\"status\":401,\"error\":\"Unauthorized\",\"message\":\"%s\"}",
                java.time.Instant.now().toString(),
                message
        );

        byte[] bytes = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
        );
    }
}
