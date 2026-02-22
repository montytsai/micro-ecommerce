# Product Service

Product catalog and inventory microservice for the e-commerce platform. 
Manages products, categories, and purchase (stock deduction) with concurrent-safe behaviour.

English | [繁體中文](README.zh-TW.md)

## Tech Stack

- **Java 21** · **Spring Boot 3.5** · **Spring Cloud 2025**
- **PostgreSQL** · **Spring Data JPA** · **Flyway**
- **MapStruct** · **Lombok** · **Bean Validation**
- **Config Server** · **Eureka**

## API Overview

| Method | Endpoint                   | Description                       |
|--------|----------------------------|-----------------------------------|
| GET    | `/api/v1/product`          | List all products (with category) |
| GET    | `/api/v1/product/{id}`     | Get product by ID                 |
| POST   | `/api/v1/product`          | Create product                    |
| POST   | `/api/v1/product/purchase` | Purchase (batch stock deduction)  |
| PUT    | `/api/v1/product/{id}`     | Full update                       |
| PATCH  | `/api/v1/product/{id}`     | Partial update                    |
| DELETE | `/api/v1/product/{id}`     | Delete product                    |

## Design Highlights

- **Concurrency**: Pessimistic lock (`PESSIMISTIC_WRITE`) on purchase + ordered lock acquisition to avoid deadlocks; integration test ensures stock never goes negative under concurrent requests.
- **Transactions**: Purchase is transactional with rollback on any failure (e.g. insufficient stock or product not found).
- **Validation groups**: Create / Update / Patch have separate validation rules (e.g. ID null on create, required on update).
- **Consistency**: Optimistic locking (`@Version`) with 409 Conflict on concurrent modification; centralized error body via `ErrorResponse` and `@RestControllerAdvice`.
- **N+1**: `findAll()` uses `@EntityGraph(attributePaths = {"category"})` so categories are loaded in one go.
- **Schema**: Flyway migrations; sequences aligned with Hibernate `allocationSize=50`.

## Prerequisites

- Config Server at `http://localhost:8888`
- Eureka at `http://localhost:8761`
- PostgreSQL (e.g. `ms-product` DB; see `product-service.yml`). Optional: `docker-compose up postgres`.

## Run

```bash
mvn spring-boot:run
```

Default port: 8050.

## Configuration (Config Server)

- product-service.yml: datasource (URL, user, password via env: MS_PG_USER, MS_PG_PWD), JPA, Flyway.
- application.yml: Eureka, logging.