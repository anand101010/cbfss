package com.incede.nbfc.collateral;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.incede.nbfc.collateral.domain.entity")
@EnableJpaRepositories(basePackages = "com.incede.nbfc.collateral.repository")
public class CollateralManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollateralManagementServiceApplication.class, args);
        System.out.println("🚀 Incede NBFC Collateral Management Service started successfully!");
        System.out.println("📍 Service Port: 8083");
        System.out.println("🗄️  Database Schema: collateral_management_schema");
        System.out.println("🔗 Health Check: http://localhost:8083/actuator/health");
    }
} 