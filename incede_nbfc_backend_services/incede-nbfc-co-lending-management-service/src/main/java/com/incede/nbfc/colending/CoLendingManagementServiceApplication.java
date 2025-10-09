package com.incede.nbfc.colending;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.incede.nbfc.colending.domain.entity")
@EnableJpaRepositories(basePackages = "com.incede.nbfc.colending.repository")
public class CoLendingManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoLendingManagementServiceApplication.class, args);
        System.out.println("🚀 Incede NBFC Co-lending Management Service started successfully!");
        System.out.println("📍 Service Port: 8084");
        System.out.println("🗄️  Database Schema: co_lending_management_schema");
        System.out.println("🔗 Health Check: http://localhost:8084/actuator/health");
    }
} 