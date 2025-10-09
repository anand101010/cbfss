package com.incede.nbfc.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.incede.nbfc.notification.domain")
@EnableJpaRepositories(basePackages = "com.incede.nbfc.notification.repository")
@EnableFeignClients(basePackages = "com.incede.nbfc.notification.client")
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
        System.out.println("\uD83D\uDE80 Incede NBFC Notification Service started successfully!");
        System.out.println("\uD83D\uDCCD Service Port: 8085");
        System.out.println("\uD83D\uDDC4\uFE0F  Database Schema: notification_service_schema");
        System.out.println("\uD83D\uDD17 Health Check: http://localhost:8085/actuator/health");
    }
} 