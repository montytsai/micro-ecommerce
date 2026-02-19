# Customer Service (顧客管理服務)

A microservice for managing customer data in the e-commerce platform. 
Part of a Spring Cloud microservices architecture.

## Tech Stack

- **Java 21** · **Spring Boot 3.5** · **Spring Cloud 2025**
- **MongoDB** (Spring Data MongoDB)
- **MapStruct** (DTO mapping) · **Lombok** · **Bean Validation**
- **Config Server** · **Eureka** (service discovery)
- **Microservices Architecture** (RESTful API design, Service Discovery & Centralized Config)

## Design Highlights

- **RESTful API 標準化**: 遵循資源導向設計（Resource-Oriented），明確區分 PUT (全量替換) 與 PATCH (局部更新)。
- **精細化校驗策略**: 使用 Validation Group 策略，針對 Create、Full Update 與 Partial Update (PATCH) 實作不同的驗證群組。
- **集中式異常處理**: 透過 @RestControllerAdvice 統一全域異常處理流程，對外提供結構一致的 ErrorResponse。 
- **轉換層實現**: 使用 MapStruct 簡化流程，利用 `@Condition` 實現僅更新提供的非空值欄位，避免覆蓋未提供的欄位。

## API Overview

| Method | Endpoint                       | Description  | Key Features                        |
|--------|--------------------------------|--------------|-------------------------------------|
| GET    | `/api/v1/customer`             | 查詢客戶清單       |                                     |
| HEAD   | `/api/v1/customer/exists/{id}` | 檢查客戶是否存在     | 採用 HEAD 快速存在性檢查，無回應體                |
| GET    | `/api/v1/customer/{id}`        | 根據客戶ID取得客戶資料 |                                     |
| POST   | `/api/v1/customer`             | 註冊新客戶        | 使用 CreateGroup 驗證規則                 |
| PUT    | `/api/v1/customer/{id}`        | 完整替換客戶資料     | 使用 UpdateGroup 驗證規則，要求所有欄位          |
| PATCH  | `/api/v1/customer/{id}`        | 局部更新客戶資料     | 使用 PatchGroup 驗證規則，自定義 `@Condition` |
| DELETE | `/api/v1/customer/{id}`        | 刪除客戶資料       |                                     |

## Run

1. 啟動基礎設施
> **重要**：必須先啟動 `MongoDB`、`config-server`與 `discovery-service`。
   - MongoDB on `localhost:27017` (or via `docker-compose up mongo`)
   - Config Server running on `http://localhost:8888`
   - Eureka Server (for discovery) running on `http://localhost:8761`

2. 編譯啟動
    ```bash
    mvn clean spring-boot:run
    ```

3. 服務啟動後可訪問 API：
   - Base URL: `http://localhost:8090/api/v1/customer`