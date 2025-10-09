package com.incede.nbfc.notification.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enhanced Security Configuration for Incede NBFC Core Monolith Service.
 * 
 * Provides comprehensive security including CORS and CSRF protection.
 * Will be enhanced with proper authentication later.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    /**
     * Enhanced Security Filter Chain with CORS and CSRF protection.
     * Allows all requests for now but with proper security headers.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/actuator/**", "/api-docs/**", "/swagger-ui/**","/api/v1/otp/**"))
            .headers(headers -> headers
                .frameOptions().sameOrigin() // Allow frames for H2 console
                .contentSecurityPolicy("default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; font-src 'self'; connect-src 'self'; frame-ancestors 'self';"))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api-docs/", "/swagger-ui/").permitAll()
                   .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 ->oauth2
                        .jwt(jwt-> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
            .httpBasic(AbstractHttpConfigurer::disable) // Disable basic auth
            .formLogin(AbstractHttpConfigurer::disable) // Disable form login
            .logout(AbstractHttpConfigurer::disable); // Disable logout
        return http.build();
    }



    /**
     * Creates a custom JwtAuthenticationConverter for mapping JWT roles to Spring Security authorities.
     *
     * <p>This converter is used by Spring Security's OAuth2 resource server to extract roles from the JWT token
     * and convert them into GrantedAuthority objects that can be used for authorization checks.</p>
     *
     * <p>Steps performed:</p>
     * <ol>
     *     <li>Extract the "realm_access" claim from the JWT.</li>
     *     <li>Get the "roles" list from the "realm_access" map.</li>
     *     <li>If roles are missing, default to an empty list.</li>
     *     <li>Convert each role to a Spring Security GrantedAuthority.</li>
     *         <ul>
     *             <li>Add "ROLE_" prefix if not already present (required by Spring Security for role-based checks).</li>
     *             <li>Convert each role to a SimpleGrantedAuthority object.</li>
     *         </ul>
     *     <li>Return the list of authorities to be used by Spring Security for authorization.</li>
     *     <li>Print roles and authorities for debugging purposes.</li>
     * </ol>
     *
     * @return JwtAuthenticationConverter configured to extract roles from the JWT
     */


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            List<?> rolesRaw = (realmAccess != null && realmAccess.get("roles") instanceof List)
                    ? (List<?>) realmAccess.get("roles")
                    : List.of();

            // Map roles to GrantedAuthority safely
            Collection<GrantedAuthority> authorities = rolesRaw.stream()
                    .map(Object::toString)  // <-- convert Object to String
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());


            log.info("JWT Roles: {}", rolesRaw);
            log.info("Mapped Granted Authorities: {}", authorities);
            ;

            return authorities;
        });

        return converter;
    }

    /**
     * Comprehensive CORS configuration for cross-origin requests.
     * Configures allowed origins, methods, headers, and credentials.
     * 
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Configure allowed origins (customize based on your frontend domains)
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",           // Local development
            "https://localhost:*",          // Local HTTPS development
            "http://127.0.0.1:*",          // Local IP development
            "https://127.0.0.1:*"         // Local HTTPS IP development
        ));
        
        // Configure allowed HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));
        
        // Configure allowed headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Origin", "Content-Type", "Accept", "Authorization", 
            "X-Requested-With", "Cache-Control", "X-File-Name",
            "Access-Control-Request-Method", "Access-Control-Request-Headers"
        ));
        
        // Configure exposed headers (headers that browsers can access)
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials",
            "X-Total-Count", "X-Page-Count", "X-Current-Page"
        ));
        
        // Configure credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Configure preflight request caching (in seconds)
        configuration.setMaxAge(3600L);
        
        // Configure CORS for all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
} 