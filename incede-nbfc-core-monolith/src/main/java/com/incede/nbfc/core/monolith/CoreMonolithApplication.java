package com.incede.nbfc.core.monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main Spring Boot Application class for Incede NBFC Core Monolith Service.
 * 
 * This is the entry point for the core monolith application that integrates
 * multiple NBFC services into a single deployable unit.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {
    "com.incede.nbfc.core.monolith",
    "com.incede.nbfc.core.monolith.customer",
    "com.incede.nbfc.core.monolith.masterdata",
    "com.incede.nbfc.core.monolith.product",
    "com.incede.nbfc.core.monolith.user",
    "com.incede.nbfc.core.monolith.tenant",
    "com.incede.nbfc.core.monolith.asset",
    "com.incede.nbfc.core.monolith.auction",
    "com.incede.nbfc.core.monolith.liability",
    "com.incede.nbfc.core.monolith.reports",
    "com.incede.nbfc.core.monolith.audit",
    "com.incede.nbfc.core.monolith.common",
    "com.incede.nbfc.core.monolith.security",
    "com.incede.nbfc.core.monolith.controller",
    "com.incede.nbfc.core.monolith.service",
    "com.incede.nbfc.core.monolith.repository",
    "com.incede.nbfc.core.monolith.config",
    "com.incede.nbfc.core.monolith.mapper",
    "com.incede.nbfc.core.monolith.client",
    "com.incede.nbfc.core.monolith.util"
})
@EntityScan(basePackages = {
    "com.incede.nbfc.core.monolith.domain.entity",
    "com.incede.nbfc.core.monolith.customer.domain.entity",
    "com.incede.nbfc.core.monolith.masterdata.domain.entity",
    "com.incede.nbfc.core.monolith.product.domain.entity",
    "com.incede.nbfc.core.monolith.user.domain.entity",
    "com.incede.nbfc.core.monolith.tenant.domain.entity",
    "com.incede.nbfc.core.monolith.asset.domain.entity",
    "com.incede.nbfc.core.monolith.auction.domain.entity",
    "com.incede.nbfc.core.monolith.liability.domain.entity",
    "com.incede.nbfc.core.monolith.reports.domain.entity",
    "com.incede.nbfc.core.monolith.audit.domain.entity"
})
@EnableJpaRepositories(basePackages = {
    "com.incede.nbfc.core.monolith.repository",
    "com.incede.nbfc.core.monolith.customer.repository",
    "com.incede.nbfc.core.monolith.masterdata.repository",
    "com.incede.nbfc.core.monolith.product.repository",
    "com.incede.nbfc.core.monolith.user.repository",
    "com.incede.nbfc.core.monolith.tenant.repository",
    "com.incede.nbfc.core.monolith.asset.repository",
    "com.incede.nbfc.core.monolith.auction.repository",
    "com.incede.nbfc.core.monolith.liability.repository",
    "com.incede.nbfc.core.monolith.reports.repository",
    "com.incede.nbfc.core.monolith.audit.repository"
})
public class CoreMonolithApplication {

    /**
     * Main method to start the Spring Boot application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CoreMonolithApplication.class, args);
        
        // Display application startup information
        System.out.println("==========================================");
        System.out.println(" Incede NBFC Core Monolith Started!");
        System.out.println("==========================================");
        System.out.println(" Application URL: http://localhost:8080");
        System.out.println(" Health Check: http://localhost:8080/health");
        System.out.println(" H2 Console: http://localhost:8080/h2-console");
        System.out.println(" Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println(" Actuator: http://localhost:8080/actuator");
        System.out.println("==========================================");
        System.out.println(" Integrated Services Loaded:");
        System.out.println("   • Customer Management Service");
        System.out.println("   • Master Data Management Service");
        System.out.println("   • Product & Scheme Configuration Service");
        System.out.println("   • User & Role Management Service");
        System.out.println("   • Tenant Onboarding Service");
        System.out.println("   • Asset Management Service");
        System.out.println("   • Auction Management Service");
        System.out.println("   • Liability Management Service");
        System.out.println("   • Reports & Audit Trails Service");
        System.out.println("   • Audit & Compliance Service");
        System.out.println("==========================================");
    }
} 