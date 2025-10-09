# Incede NBFC Core Monolith - Integrated Services

## Overview
This monolith application integrates 11 NBFC services into a single deployable unit using Spring Boot's component scanning and package-based organization.

## Integrated Services Architecture

### Service Organization
All services are organized under the `com.incede.nbfc.core.monolith` package with clear separation:


com.incede.nbfc.core.monolith/
├── customer/           # Customer Management Service
├── masterdata/         # Master Data Management Service
├── product/            # Product & Scheme Configuration Service
├── user/               # User & Role Management Service
├── tenant/             # Tenant Onboarding Service
├── asset/              # Asset Management Service
├── auction/            # Auction Management Service
├── liability/          # Liability Management Service
├── reports/            # Reports & Audit Trails Service
├── audit/              # Audit & Compliance Service
├── common/             # Common utilities and shared components
├── security/           # Security and authentication
├── config/             # Configuration classes
├── controller/         # REST controllers
├── service/            # Business logic services
├── repository/         # Data access layer
├── mapper/             # Object mapping utilities
├── client/             # External service clients
└── util/               # Utility classes


## How Services Are Loaded

### 1. Main Application Class
The `CoreMonolithApplication.java` uses comprehensive component scanning:


@SpringBootApplication
@ComponentScan(basePackages = {
    "com.incede.nbfc.core.monolith",
    "com.incede.nbfc.core.monolith.customer",
    "com.incede.nbfc.core.monolith.masterdata",
    // ... all service packages
})


### 2. Entity Scanning
JPA entities are scanned from all service packages:


@EntityScan(basePackages = {
    "com.incede.nbfc.core.monolith.domain.entity",
    "com.incede.nbfc.core.monolith.customer.domain.entity",
    // ... all entity packages
})


### 3. Repository Scanning
JPA repositories are scanned from all service packages:


@EnableJpaRepositories(basePackages = {
    "com.incede.nbfc.core.monolith.repository",
    "com.incede.nbfc.core.monolith.customer.repository",
    // ... all repository packages
})


### 4. Service Loader Configuration
The `ServiceLoaderConfig.java` provides additional component scanning and startup logging.

## Benefits of This Approach

### ✅ **Immediate Benefits**
- **Single Deployment**: All services deploy as one unit
- **Shared Infrastructure**: Common database, security, and utilities
- **Simplified Testing**: Test all services together
- **Faster Development**: No inter-service communication complexity

### ✅ **Future Migration Benefits**
- **Clear Service Boundaries**: Each package represents a future microservice
- **Independent Development**: Teams can work on different packages
- **Gradual Extraction**: Services can be extracted one by one
- **Shared Code**: Common utilities remain accessible

## Service Communication

### Current State (Monolith)
- **Internal Method Calls**: Services communicate via direct method calls
- **Shared Database**: Single database with schema separation
- **Shared Security**: Common authentication and authorization
- **Shared Configuration**: Centralized configuration management

### Future State (Microservices)
- **REST APIs**: Services communicate via HTTP/REST
- **Event-Driven**: Asynchronous communication via events
- **Independent Databases**: Each service has its own database
- **Service Discovery**: Dynamic service location and health checks

## Development Guidelines

### Package Structure
Each service package should follow this structure:

service-name/
├── controller/         # REST endpoints
├── service/            # Business logic
├── repository/         # Data access
├── domain/            # Entities, DTOs, enums
│   ├── entity/        # JPA entities
│   ├── dto/           # Data transfer objects
│   └── enums/         # Enumerations
├── mapper/             # Object mappers
└── config/             # Service-specific configuration


### Naming Conventions
- **Packages**: lowercase (e.g., `customer`, `masterdata`)
- **Classes**: PascalCase (e.g., `CustomerService`, `UserController`)
- **Methods**: camelCase (e.g., `createCustomer`, `findByEmail`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_COUNT`)

### Database Schema
Each service uses its own schema for data isolation:
- `customer_schema` for customer data
- `masterdata_schema` for reference data
- `product_schema` for product configurations
- etc.

## Testing Strategy

### Unit Testing
- Test each service package independently
- Mock dependencies between services
- Focus on business logic validation

### Integration Testing
- Test service interactions within the monolith
- Validate database operations across schemas
- Test security and authentication flows

### End-to-End Testing
- Test complete business workflows
- Validate API endpoints and responses
- Test error handling and edge cases

## Deployment

### Development
- Run locally with H2 database
- Hot reload for development
- Debug all services together

### Production
- Single JAR deployment
- PostgreSQL with multiple schemas
- Containerized deployment (Docker)
- Kubernetes orchestration ready

## Monitoring and Observability

### Health Checks
- `/health` - Basic application health
- `/health/detailed` - Detailed health information
- `/actuator` - Spring Boot actuator endpoints

### Logging
- Centralized logging for all services
- Structured logging with correlation IDs
- Performance metrics and tracing

### Metrics
- Application metrics via Micrometer
- Database performance metrics
- Custom business metrics per service

