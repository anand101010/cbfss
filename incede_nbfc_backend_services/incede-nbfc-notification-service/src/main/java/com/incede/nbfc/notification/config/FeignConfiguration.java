package com.incede.nbfc.notification.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign Configuration for customizing Feign client behavior
 * Includes custom error decoder, HTTP client, and logging configuration
 */
@Configuration
public class FeignConfiguration {

    /**
     * Custom error decoder for handling HTTP errors
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    /**
     * HTTP client configuration using OkHttp
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    /**
     * Logging level configuration for Feign clients
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
} 