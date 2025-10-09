# Incede NBFC Core Monolith Service

## Overview

The Incede NBFC Core Monolith Service is a Spring Boot application that integrates core NBFC services into a single, cohesive application. This service provides the foundation for core NBFC functionality and serves as the central hub for various business operations.

## Service Details

- Service Name: Core Monolith Service
- Port: 8080
- Context Path: `/` (root)
- Java Version: 21
- Spring Boot Version: 3.2.3
- Database: PostgreSQL with multiple schemas

## Integrated Services

This monolith currently integrates the following core services:

### 1. Customer Management Service 
- Customer lifecycle management
- KYC processing and validation
- Customer profile management
- Customer relationship management

### 2. Master Data Management Service 
- Reference data management
- System configurations
- Metadata management
- Code tables and lookups

### 3. User & Role Management Service 
- User authentication and authorization
- Role-based access control
- Permission management
- User lifecycle management

### 4. Configuration Service 
- System configuration
- Feature flags
- Environment settings
- Configuration management

## Technology Stack

- Framework: Spring Boot 3.2.3
- Java: Java 21 (LTS)
- Database: PostgreSQL (single instance, multiple schemas)
- Security: Spring Security 6.2.1
- Documentation: OpenAPI 3.0 with Swagger UI
- Monitoring: Spring Boot Actuator
- Build Tool: Maven 3.9+

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/incede/nbfc/core/monolith/
│   │       ├── config/           # Configuration classes
│   │       ├── controller/       # REST controllers
│   │       │   ├── customer/     # Customer management
│   │       │   └── masterdata/   # Master data management
│   │       ├── service/          # Business logic services
│   │       ├── repository/       # Data access layer
│   │       ├── domain/           # Domain models
│   │       │   ├── entity/       # JPA entities
│   │       │   ├── dto/          # Data transfer objects
│   │       │   └── exception/    # Custom exceptions
│   │       ├── mapper/           # Object mappers
│   │       └── security/         # Security configurations
│   └── resources/
│       ├── application.yml       # Common configuration
│       └── application-dev.yml   # Development configuration
└── test/
    └── java/                     # Test classes
```

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.9 or higher
- PostgreSQL 14 or higher

### Local Development Setup

1. Clone the repository
   
   git clone <repository-url>
   cd incede-nbfc-backend-services
   ```

2. Setup Database
   
   # Create PostgreSQL database
   createdb postgres
   
   # Run database setup scripts
   psql -U postgres -d postgres -f database-scripts/setup-database.sql
   psql -U postgres -d postgres -f database-scripts/setup-masterdata-schema.sql
   ```

3. Navigate to the service
   
   cd incede-nbfc-core-monolith
   ```

4. Build the project
   
   mvn clean install
   ```

5. Run the application
   
   mvn spring-boot:run
   ```

6. Access the application
   - Application: http://localhost:8080
   - Health Check: http://localhost:8080/actuator/health
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Actuator: http://localhost:8080/actuator

### Environment Configuration

The application supports multiple profiles:

- dev: Development environment with PostgreSQL database (default)
- test: Testing environment (to be configured)
- prod: Production environment (to be configured)

Set the active profile using:

export SPRING_PROFILES_ACTIVE=dev
```

## API Endpoints

### Customer Management Endpoints

- `GET /customers` - Get all customers
- `POST /customers` - Create a new customer
- `GET /customers/{id}` - Get customer by ID
- `PUT /customers/{id}` - Update customer
- `DELETE /customers/{id}` - Delete customer

### Master Data Endpoints

- `GET /api/v1/masterdata/product-categories` - Get all product categories
- `GET /api/v1/masterdata/loan-types` - Get all loan types
- `GET /api/v1/masterdata/document-types` - Get all document types

### Health Check Endpoints

- `GET /actuator/health` - Health information
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Application metrics

## Database Configuration

The service uses a single PostgreSQL instance with multiple schemas for data isolation:

### Current Schemas
- `customer_schema` - Customer management data
- `masterdata_schema` - Master data (product categories, loan types, document types)

### Database Connection
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: admin
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        default_schema: customer_schema
```

## Security

The service includes:
- Spring Security configuration
- CSRF protection disabled for API endpoints
- Public access to:
  - `/customers/` - Customer API endpoints
  - `/api/v1/masterdata/` - Master data API endpoints
  - `/actuator/` - Health check and monitoring endpoints
- JWT-based authentication framework (to be implemented)

## Monitoring and Observability

- Health Checks: Built-in health endpoints via Spring Boot Actuator
- Metrics: Application metrics export
- Logging: Structured logging with configurable levels
- Actuator: Spring Boot Actuator for monitoring

## Current Implementation Status

###  Completed
- Core monolith service structure
- Customer management API (GET/POST operations)
- Master data management API (GET operations)
- PostgreSQL database integration
- Multi-schema support
- Basic security configuration
- Maven project structure

###  In Progress
- Advanced security features
- Comprehensive testing
- Additional business services

###  Planned
- User & Role Management Service
- Configuration Service
- Advanced monitoring and logging
- Performance optimization

## Development Guidelines

### Code Style
- Follow Java coding conventions
- Use meaningful names for classes, methods, and variables
- Include comprehensive JavaDoc comments
- Follow Spring Boot best practices

### Testing
- Write unit tests for all business logic
- Include integration tests for critical paths
- Maintain high test coverage

### Documentation
- Keep API documentation up to date
- Document complex business logic
- Maintain clear README files
- Use OpenAPI annotations for API documentation

## Troubleshooting

### Common Issues

1. Port Already in Use
   - Change the port in `application-dev.yml`
   - Kill the process using the port

2. Database Connection Issues
   - Verify PostgreSQL is running
   - Check database credentials in `application-dev.yml`
   - Ensure database and schemas exist

3. Schema Validation Errors
   - Run database scripts in correct order
   - Check entity mappings match database schema
   - Verify `@Table` annotations include correct schema names

4. Build Failures
   - Verify Java version (Java 21 required)
   - Check Maven version (3.9+ required)
   - Clean and rebuild the project

### Logs

Check application logs for detailed error information:

tail -f logs/application.log
```

## API Testing

### Customer API

# Get all customers
curl -X GET http://localhost:8080/customers

# Create customer
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1234567890",
    "monthlyIncome": 5000.00
  }'
```

### Master Data API

# Get product categories
curl -X GET http://localhost:8080/api/v1/masterdata/product-categories

# Get loan types
curl -X GET http://localhost:8080/api/v1/masterdata/loan-types

# Get document types
curl -X GET http://localhost:8080/api/v1/masterdata/document-types
```




## License

This project is proprietary to Incede NBFC. All rights reserved.

---

Version: 1.0.0
Maintained By: Incede NBFC Development Team 