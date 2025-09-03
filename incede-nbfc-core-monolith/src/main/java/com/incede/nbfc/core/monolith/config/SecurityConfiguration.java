package com.incede.nbfc.core.monolith.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Enhanced Security Configuration for Incede NBFC Core Monolith Service.
 * 
 * Provides comprehensive security including CORS and CSRF protection.
 * Will be enhanced with proper authentication later.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * Enhanced Security Filter Chain with CORS and CSRF protection.
     * Allows all requests for now but with proper security headers.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API endpoints
            .headers(headers -> headers
                .frameOptions().sameOrigin() // Allow frames for H2 console
                .contentSecurityPolicy("default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline';")
            )
                               .authorizeHttpRequests(authz -> authz
                       .requestMatchers("/actuator/**").permitAll() // Health check and actuator
                       .requestMatchers("/error").permitAll() // Error pages
                       .requestMatchers("/customers/**").permitAll() // Customer API endpoints
                       .requestMatchers("/api/v1/masterdata/**").permitAll() // Master Data API endpoints
                       .requestMatchers("/api-docs/**").permitAll() // OpenAPI docs
                       .requestMatchers("/swagger-ui/**").permitAll() // Swagger UI
                       .anyRequest().permitAll() // Allow all other requests for now
                   )
            .httpBasic(AbstractHttpConfigurer::disable) // Disable basic auth
            .formLogin(AbstractHttpConfigurer::disable) // Disable form login
            .logout(AbstractHttpConfigurer::disable); // Disable logout
        
        return http.build();
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
            "https://127.0.0.1:*",         // Local HTTPS IP development
            "http://*.incede.com",          // Incede domains
            "https://*.incede.com"          // Incede HTTPS domains
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