package com.incede.nbfc.core.monolith.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer for future data population needs.
 * Currently empty - will be used when data initialization is required.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Data initialization logic will be added here when needed
        System.out.println("DataInitializer started - no data to initialize at this time.");
    }
} 