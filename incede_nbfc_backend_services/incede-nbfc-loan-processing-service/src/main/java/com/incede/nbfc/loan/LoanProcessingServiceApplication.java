package com.incede.nbfc.loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.incede.nbfc.loan.domain.entity")
@EnableJpaRepositories(basePackages = "com.incede.nbfc.loan.repository")
public class LoanProcessingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanProcessingServiceApplication.class, args);
        System.out.println("🚀 Incede NBFC Loan Processing Service started successfully!");
        System.out.println("📍 Service Port: 8081");
        System.out.println("🗄️  Database Schema: loan_processing_schema");
        System.out.println("🔗 Health Check: http://localhost:8081/actuator/health");
    }
} 