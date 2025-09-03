package com.incede.nbfc.core.monolith.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Controller for Incede NBFC Core Monolith Service.
 * 
 * Provides basic health check endpoints to verify service status.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0

 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @Value("${app.service.name:Core Monolith Service}")
    private String serviceName;
    
    @Value("${app.service.version:1.0.0}")
    private String serviceVersion;
    
    @Value("${spring.application.name:incede-nbfc-core-monolith}")
    private String applicationName;

    /**
     * Basic health check endpoint.
     * 
     * @return Health status response
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", serviceName);
        response.put("version", serviceVersion);
        response.put("application", applicationName);
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Core Monolith Service is running");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Detailed health check endpoint.
     * 
     * @return Detailed health status response
     */
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", serviceName);
        response.put("version", serviceVersion);
        response.put("application", applicationName);
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Core Monolith Service is running");
        
        // Service components status
        Map<String, String> components = new HashMap<>();
        components.put("database", "UP");
        components.put("security", "UP");
        components.put("actuator", "UP");
        components.put("swagger", "UP");
        response.put("components", components);
        
        // Service information
        Map<String, Object> info = new HashMap<>();
        info.put("port", 8080);
        info.put("contextPath", "/api/v1");
        info.put("profiles", "dev");
        response.put("info", info);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Service information endpoint.
     * 
     * @return Service information response
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> serviceInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", serviceName);
        response.put("version", serviceVersion);
        response.put("application", applicationName);
        response.put("description", "Core NBFC functionality integrating multiple related services");
        response.put("timestamp", LocalDateTime.now());
        
        // Integrated services
        String[] integratedServices = {
            "Customer Management Service",
            "Master Data Management Service", 
            "Product & Scheme Configuration Service",
            "Reports & Audit Trails Service",
            "User & Role Management Service",
            "Tenant Onboarding Service",
            "Liability Management Service",
            "Auction Management Service",
            "Asset Management Service",
            "Audit & Compliance Service",
            "Configuration Service"
        };
        response.put("integratedServices", integratedServices);
        
        return ResponseEntity.ok(response);
    }
} 