# Order Service

Order management microservice responsible for order orchestration: validating customers and product stock via remote calls, 
persisting transactional data, and publishing order confirmation events to Kafka.

English | [繁體中文](README.zh-TW.md)

## Tech Stack

- **Java 21** · **Spring Boot 3.5** · **Spring Cloud 2025**
- **PostgreSQL** · **Spring Data JPA**
- **OpenFeign** (Inter-service communication: customer, product)
- **Apache Kafka** (Event-driven order confirmation)
- **MapStruct** · **Lombok** · **Bean Validation** · **ULID**
- **Config Server** · **Eureka Discovery**

## API Overview

| Method | Endpoint                       | Description                                                              |
|--------|--------------------------------|--------------------------------------------------------------------------|
| GET    | `/api/v1/order`                | List all orders                                                          |
| GET    | `/api/v1/order/{order-id}`     | Get order details by ID                                                  |
| POST   | `/api/v1/order`                | Create order (validate customer, save DB, reserve stock, publish Kafka)  |
| GET    | `/api/v1/orderline/{order-id}` | List all order lines for a specific order                                |

## Flow: Create Order

1. **Validate Customer** – Verify existence via `CustomerClient` (Feign).
2. **Persistence (DB First)** – Generate **ULID**, save `Order` and `OrderLine` to PostgreSQL. Wrapped in `@Transactional` for atomicity.
3. **Reserve Stock** – Call `ProductClient.purchaseProducts()` (Feign) to deduct inventory.
4. **Payment** – Placeholder (Integration pending; `payment-url` reserved in config).
5. **Notification** – Publish `OrderConfirmation` to Kafka topic `order-topic` for downstream consumers.

## Design Highlights

- **ID Strategy**: Implementation of **ULID (Universally Unique Lexicographically Sortable Identifier)**. Provides distributed uniqueness while maintaining B-Tree index performance in PostgreSQL.
- **Data Consistency**: Optimized "Write-ahead" transaction flow. Local persistence precedes remote state mutations to ensure rollback capability via Spring `@Transactional` if remote calls fail.
- **Fault Tolerance**: Centralized `GlobalExceptionHandler` with dedicated handling for `FeignException` to prevent cascading failures.
- **Auditing**: Unified audit fields (`createdAt`, `updatedAt`) via `BaseEntity` and JPA Auditing.

## Prerequisites

- **Config Server**: `http://localhost:8888`
- **Eureka**: `http://localhost:8761`
- **PostgreSQL**: DB `ms-order` (Refer to `order-service.yml`)
- **Kafka**: `localhost:9092` (e.g., `docker-compose up broker`)
- **Dependent Services**: `customer-service` (8090), `product-service` (8050)

## Run

```bash
mvn spring-boot:run

```

Default port: **8070**.