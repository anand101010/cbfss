package com.incede.nbfc.financial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.incede.nbfc.financial.domain.entity")
@EnableJpaRepositories(basePackages = "com.incede.nbfc.financial.repository")
public class FinancialAccountingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialAccountingServiceApplication.class, args);
        System.out.println("🚀 Incede NBFC Financial Accounting Service started successfully!");
        System.out.println("📍 Service Port: 8082");
        System.out.println("🗄️  Database Schema: financial_accounting_schema");
        System.out.println("🔗 Health Check: http://localhost:8082/actuator/health");
    }
} 