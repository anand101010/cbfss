package com.incede.nbfc.notification.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Custom request interceptor for Feign clients
 * Adds custom headers, authentication tokens, or other request modifications
 */
@Slf4j
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        log.debug("Applying custom headers to Feign request: {}", template.url());
        
        // Add custom headers
        template.header("User-Agent", "Incede-NBFC-Service");
        template.header("X-Request-Source", "NBFC-Core-Monolith");
        template.header("X-Request-ID", generateRequestId());
        
        // Add timestamp for request tracking
        template.header("X-Request-Timestamp", String.valueOf(System.currentTimeMillis()));
        
        // You can add authentication headers here if needed
        // template.header("Authorization", "Bearer " + getAuthToken());
        
        log.debug("Custom headers applied to request: {}", template.headers());
    }
    
    /**
     * Generate a unique request ID for tracking
     */
    private String generateRequestId() {
        return "req-" + System.currentTimeMillis() + "-" + Thread.currentThread().getId();
    }
} 