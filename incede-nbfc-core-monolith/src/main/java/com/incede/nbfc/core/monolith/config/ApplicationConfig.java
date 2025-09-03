package com.incede.nbfc.core.monolith.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Application Configuration for Incede NBFC Core Monolith Service.
 * 
 * Provides basic configuration for application settings, excluding security
 * which is handled by SecurityConfiguration.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
@Configuration
public class ApplicationConfig {

    /**
     * Development profile configuration.
     * This configuration is only active when the 'dev' profile is active.
     */
    @Configuration
    @Profile("dev")
    static class DevConfig {
        // Development-specific configurations can be added here
        // Security is handled by SecurityConfiguration
    }

    /**
     * Production profile configuration.
     * This configuration is only active when the 'prod' profile is active.
     */
    @Configuration
    @Profile("prod")
    static class ProdConfig {
        // Production-specific configurations can be added here
        // Security is handled by SecurityConfiguration
    }
} 