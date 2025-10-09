# FeignClient Implementation with Spring Boot 3.3.4

This document describes the implementation of a sample FeignClient in the Incede NBFC Core Monolith service, demonstrating integration with external APIs using Spring Cloud OpenFeign.

## Overview

The implementation includes:
- FeignClient Interface: Defines the contract for external API calls
- Configuration: Custom Feign configuration with error handling and interceptors
- Fallback Mechanism: Circuit breaker pattern with fallback responses
- Request Interceptor: Custom headers and request modifications
- Service Layer: Business logic integration
- REST Controller: API endpoints for testing


## Spring Boot Version Update

The project has been updated from Spring Boot 3.2.3 to **3.3.4** with the following changes:

### Parent POM Updates

<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.4</version>
    <relativePath/>
</parent>


### Version Properties

<properties>
    <spring-boot.version>3.3.4</spring-boot.version>
    <spring-cloud.version>2023.0.4</spring-cloud.version>
    <spring-security.version>6.2.2</spring-security.version>
</properties>


## Dependencies Added

### OpenFeign Dependencies

<!-- OpenFeign Dependencies -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>


## Architecture Components

### 1. FeignConfiguration
Location: `src/main/java/com/incede/nbfc/core/monolith/config/FeignConfiguration.java`

Provides custom configuration for Feign clients:
- Custom error decoder for HTTP error handling
- OkHttp client configuration
- Logging level configuration

### 2. CustomErrorDecoder
Location: `src/main/java/com/incede/nbfc/core/monolith/config/CustomErrorDecoder.java`

Handles different HTTP error responses and converts them to appropriate exceptions:
- Maps HTTP status codes to Spring exceptions
- Provides detailed error logging
- Supports custom error handling logic

### 3. FeignRequestInterceptor
Location: `src/main/java/com/incede/nbfc/core/monolith/config/FeignRequestInterceptor.java`

Adds custom headers and request modifications:
- User-Agent identification
- Request source tracking
- Unique request ID generation
- Timestamp headers

### 4. PostApiClient
Location: `src/main/java/com/incede/nbfc/core/monolith/client/PostApiClient.java`

FeignClient interface for free JSONPlaceholder API:
- Get all posts
- Get post by ID
- Get posts by user ID
- Get user by ID
- Get all users
- Fallback configuration
- Custom Feign configuration

### 5. PostApiFallback
Location: `src/main/java/com/incede/nbfc/core/monolith/fallback/PostApiFallback.java`

Fallback implementation for circuit breaker pattern:
- Provides default responses when API is unavailable
- Demonstrates fallback mechanism support in Spring Boot 3.3.4
- Graceful degradation of service

### 6. DTOs
Location: `src/main/java/com/incede/nbfc/core/monolith/client/dto/`

Response DTOs for API integration:
- `Post.java`: Post data structure
- `User.java`: User data structure

### 7. PostService
Location: `src/main/java/com/incede/nbfc/core/monolith/service/PostService.java`

Business logic service using FeignClient:
- Post and user data retrieval
- Error handling and logging
- Business logic integration

### 8. PostController
Location: `src/main/java/com/incede/nbfc/core/monolith/controller/PostController.java`

REST API endpoints:
- Get all posts: `GET /api/v1/posts`
- Get post by ID: `GET /api/v1/posts/{id}`
- Get posts by user: `GET /api/v1/posts/user/{userId}`
- Get user by ID: `GET /api/v1/posts/users/{id}`
- Get all users: `GET /api/v1/posts/users`
- Get post summary: `GET /api/v1/posts/{id}/summary`
- Health check: `GET /api/v1/posts/health`

## Configuration

### Application Properties
Location: `src/main/resources/application-dev.yml`

yaml
# Post API Configuration (Free JSONPlaceholder API)
post:
  api:
    base-url: https://jsonplaceholder.typicode.com

# Feign Client Configuration
feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 10000
        loggerLevel: FULL
      post-api:
        connectTimeout: 3000
        readTimeout: 8000
        loggerLevel: BASIC

# Circuit Breaker Configuration
resilience4j:
  circuitbreaker:
    instances:
      post-api:
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        failureRateThreshold: 50


## Fallback Mechanism Support in Spring Boot 3.3.4

The fallback mechanism is fully supported in Spring Boot 3.3.4 through:

1. @FeignClient fallback attribute: Direct fallback class reference
2. Resilience4j integration: Circuit breaker pattern support
3. Automatic fallback invocation: When external API fails
4. Graceful degradation: Service continues to function with fallback data

### Fallback Implementation Example
java
@FeignClient(
    name = "post-api",
    url = "${post.api.base-url}",
    configuration = FeignConfiguration.class,
    fallback = PostApiFallback.class  // Fallback support
)
public interface PostApiClient {
    // API methods
}


## Usage Examples

### 1. Get All Posts
bash
GET /api/v1/posts


### 2. Get Post by ID
bash
GET /api/v1/posts/1


### 3. Get Posts by User ID
bash
GET /api/v1/posts/user/1


### 4. Get User by ID
bash
GET /api/v1/posts/users/1


### 5. Get All Users
bash
GET /api/v1/posts/users


### 6. Get Post Summary
bash
GET /api/v1/posts/1/summary


### 7. Health Check
bash
GET /api/v1/posts/health


## Testing the Implementation

### 1. Start the Application
bash
mvn spring-boot:run


### 2. Test with Swagger UI
- Navigate to: `http://localhost:8080/swagger-ui.html`
- Test the weather endpoints

### 3. Test Fallback Mechanism
- Disconnect internet or use invalid API key
- Observe fallback responses

## Key Features

### 1. Circuit Breaker Pattern
- Automatic fallback when external API fails
- Configurable failure thresholds
- Graceful service degradation

### 2. Custom Error Handling
- HTTP status code mapping
- Detailed error logging
- Custom exception handling

### 3. Request Interception
- Custom headers injection
- Request tracking
- Authentication support

### 4. Configuration Management
- Environment-specific configuration
- Timeout configuration
- Logging level control

### 5. Monitoring & Observability
- Request/response logging
- Error tracking
- Performance metrics

## Best Practices Implemented

1. Separation of Concerns: Clear separation between client, service, and controller layers
2. Error Handling: Comprehensive error handling with custom error decoder
3. Fallback Strategy: Graceful degradation with meaningful fallback responses
4. Configuration: Externalized configuration with sensible defaults
5. Logging: Comprehensive logging for debugging and monitoring
6. Documentation: OpenAPI/Swagger documentation for all endpoints

## Troubleshooting

### Common Issues

1. FeignClient not found: Ensure `@EnableFeignClients` is present
2. Fallback not working: Check fallback class is properly annotated with `@Component`
3. Timeout issues: Adjust `connectTimeout` and `readTimeout` in configuration
4. Circuit breaker not triggering: Verify Resilience4j configuration

### Debug Mode
Enable debug logging for Feign:
yaml
logging:
  level:
    com.incede.nbfc.core.monolith.client: DEBUG
    com.incede.nbfc.core.monolith.fallback: DEBUG

