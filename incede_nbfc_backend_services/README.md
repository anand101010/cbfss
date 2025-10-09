# Incede NBFC Backend Services

A comprehensive Spring Boot microservices architecture for Non-Banking Financial Company (NBFC) operations, built with Java 21 and Spring Boot 3.2.x.

## Architecture Overview

This project implements a hybrid microservices architecture consisting of:

- 1 Core Monolith Service (integrated core services)
- 4 Individual Microservices (specialized business services)

### Core Monolith Service (`incede-nbfc-core-monolith`)
Port: 8080  
Purpose: Foundation services and core NBFC functionality

Integrated Services:
1. Customer Management Service - Customer lifecycle, KYC, profile management
2. Master Data Management Service - Reference data, configurations, metadata
3. User & Role Management Service - User authentication, authorization, roles
4. Configuration Service - System configuration, feature flags, settings

### Individual Microservices
1. Loan Processing Service (`incede-nbfc-loan-processing-service`) - Port 8081
2. Financial Accounting Service (`incede-nbfc-financial-accounting-service`) - Port 8082
3. Collateral Management Service (`incede-nbfc-collateral-management-service`) - Port 8083
4. Co-lending Management Service (`incede-nbfc-co-lending-management-service`) - Port 8084

## Technology Stack

- Java: 21 (LTS)
- Framework: Spring Boot 3.2.3
- Spring Cloud: 2023.0.0
- Build Tool: Maven 3.9+
- Database: PostgreSQL (single instance, multiple schemas)
- API Documentation: OpenAPI 3.0 (Swagger)
- Security: Spring Security with JWT support

##  Prerequisites

- Java Development Kit (JDK): 21 or higher
- Maven: 3.9 or higher
- PostgreSQL: 14 or higher
- IDE: IntelliJ IDEA (recommended) or Eclipse

##  Quick Start

### 1. Clone the Repository
bash
git clone <repository-url>
cd incede-nbfc-backend-services


### 2. Setup Database
bash
# Create PostgreSQL database
createdb postgres

# Run database setup scripts
psql -U postgres -d postgres -f database-scripts/setup-database.sql
psql -U postgres -d postgres -f database-scripts/setup-masterdata-schema.sql


### 3. Build the Project
bash
mvn clean install


### 4. Start Services

#### Option A: Individual Service Startup
bash
# Start Core Monolith
cd incede-nbfc-core-monolith
mvn spring-boot:run

# Start Individual Microservices (in separate terminals)
cd incede-nbfc-loan-processing-service
mvn spring-boot:run

cd incede-nbfc-financial-accounting-service
mvn spring-boot:run

cd incede-nbfc-collateral-management-service
mvn spring-boot:run

cd incede-nbfc-co-lending-management-service
mvn spring-boot:run


##  Service Endpoints

| Service | Port | Health Check | API Documentation |
|---------|------|--------------|-------------------|
| Core Monolith | 8080 | http://localhost:8080/actuator/health | http://localhost:8080/swagger-ui.html |
| Loan Processing | 8081 | http://localhost:8081/actuator/health | http://localhost:8081/swagger-ui.html |
| Financial Accounting | 8082 | http://localhost:8082/actuator/health | http://localhost:8082/swagger-ui.html |
| Collateral Management | 8083 | http://localhost:8083/actuator/health | http://localhost:8083/swagger-ui.html |
| Co-lending Management | 8084 | http://localhost:8084/actuator/health | http://localhost:8084/swagger-ui.html |

## 🗄️ Database Configuration

### Current Implementation
- Database: PostgreSQL
- Host: localhost
- Port: 5432
- Database Name: postgres
- Username: postgres
- Password: admin

### Schemas
- `customer_schema`: Customer management data
- `masterdata_schema`: Master data (product categories, loan types, document types)

### Database Scripts
- `database-scripts/setup-database.sql` - Customer schema and tables
- `database-scripts/setup-masterdata-schema.sql` - Master data schema and tables

##  Configuration

### Environment Profiles
- dev: Development configuration with PostgreSQL database
- test: Testing configuration (to be configured)
- prod: Production configuration (to be configured)

### Application Properties

# Core Monolith (application-dev.yml)
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


##  Project Structure


incede-nbfc-backend-services/
├── incede-nbfc-core-monolith/          # Core monolith service
│   ├── src/main/java/
│   │   ├── customer/                   # Customer management
│   │   ├── masterdata/                 # Master data management
│   │   ├── config/                     # Configuration classes
│   │   └── security/                   # Security configuration
│   └── src/main/resources/
│       └── application-dev.yml         # Development configuration
├── incede-nbfc-loan-processing-service/ # Loan processing microservice
├── incede-nbfc-financial-accounting-service/ # Financial accounting microservice
├── incede-nbfc-collateral-management-service/ # Collateral management microservice
├── incede-nbfc-co-lending-management-service/ # Co-lending management microservice
├── database-scripts/                   # Database setup scripts
├── pom.xml                             # Root Maven configuration
└── README.md                           # This file


##  Testing

### Unit Tests
bash
mvn test


### Build Verification
bash
# Build from root
mvn clean install

# Build individual services
cd incede-nbfc-core-monolith
mvn clean install


##  Monitoring & Health Checks

All services include:
- Health Checks: `/actuator/health`
- Metrics: `/actuator/metrics`
- Info: `/actuator/info`
- Environment: `/actuator/env`

##  Security

- Authentication: JWT-based authentication (framework in place)
- Authorization: Role-based access control (RBAC)
- API Security: Spring Security with CSRF disabled for API endpoints
- Endpoints: `/customers/`, `/api/v1/masterdata/`, `/actuator/` are publicly accessible


##  Troubleshooting

### Common Issues

1. Port Already in Use
   bash
   # Check port usage
   netstat -ano | findstr :8080
   
   # Kill process using the port
   taskkill /PID <PID> /F
   

2. Database Connection Issues
   - Verify PostgreSQL is running
   - Check database credentials in `application-dev.yml`
   - Ensure database and schemas exist

3. Maven Build Issues
   bash
   # Clean and rebuild
   mvn clean install -U
   

4. Schema Validation Errors
   - Run database scripts in correct order
   - Check entity mappings match database schema
   - Verify `@Table` annotations include correct schema names

##  API Testing

### Customer API
bash
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


### Master Data API
bash
# Get product categories
curl -X GET http://localhost:8080/api/v1/masterdata/product-categories

# Get loan types
curl -X GET http://localhost:8080/api/v1/masterdata/loan-types

# Get document types
curl -X GET http://localhost:8080/api/v1/masterdata/document-types


## 📝 Development Guidelines

- Follow Domain-Driven Design (DDD) principles
- Use Clean Architecture patterns
- Implement RESTful API design
- Write comprehensive unit and integration tests
- Follow Java coding conventions
- Use Spring Boot best practices



## 📄 License

This project is proprietary software owned by Incede NBFC. All rights reserved.

---

Version: 1.0.0  
Maintained By: Incede Development Team 