package com.incede.nbfc.core.monolith.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test configuration class for the Core Monolith service.
 * 
 * Provides test-specific beans and configurations.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
@TestConfiguration
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@EntityScan(basePackages = "com.incede.nbfc.core.monolith.domain")
@EnableJpaRepositories(basePackages = "com.incede.nbfc.core.monolith.repository")
@ActiveProfiles("test")
public class TestConfig {

    // Add test-specific beans here as needed
    
    /**
     * Example test bean - replace with actual test requirements
     */
    @Bean
    @Primary
    public String testBean() {
        return "test-bean";
    }
} 