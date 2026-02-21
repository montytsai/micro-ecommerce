# Product Service (產品微服務)

電子商務平台的產品目錄與庫存管理微服務。
負責管理產品、分類，並提供具備**併發安全機制**的購買（扣減庫存）功能。

[English](README.md) | 繁體中文

## Tech Stack

* **Java 21** · **Spring Boot 3.5** · **Spring Cloud 2025**
* **PostgreSQL** · **Spring Data JPA** · **Flyway**
* **MapStruct** · **Lombok** · **Bean Validation**
* **Config Server** · **Eureka**

## API Overview

| 方法     | 端點 (Endpoint)              | 說明               |
|--------|----------------------------|------------------|
| GET    | `/api/v1/product`          | 取得所有產品列表（包含分類資訊） |
| GET    | `/api/v1/product/{id}`     | 透過 ID 取得特定產品     |
| POST   | `/api/v1/product`          | 建立新產品            |
| POST   | `/api/v1/product/purchase` | 批量購買（批量扣減庫存）     |
| PUT    | `/api/v1/product/{id}`     | 全量更新產品           |
| PATCH  | `/api/v1/product/{id}`     | 部分更新產品           |
| DELETE | `/api/v1/product/{id}`     | 刪除產品             |

## Design Highlights

* **併發處理 (Concurrency)**: 購買流程採用**悲觀鎖** (`PESSIMISTIC_WRITE`) 並結合**排序鎖定 (Ordered Lock Acquisition)** 機制以防止死結 (Deadlock)。
* **事務管理 (Transactions)**: 購買操作具備原子性，任何錯誤（如庫存不足或產品不存在）皆會觸發完整回滾 (Rollback)。
* **驗證分組 (Validation groups)**: 針對 Create / Update / Patch 定義獨立驗證規則（例如：建立時 ID 須為空，全量更新時 ID 為必填）。
* **一致性控制 (Consistency)**: 整合**樂觀鎖** (`@Version`) 處理同時修改衝突；透過 `@RestControllerAdvice` 與自定義 `ErrorResponse` 提供標準化錯誤訊息。
* **效能優化 (N+1)**: `findAll()` 導入 `@EntityGraph(attributePaths = {"category"})`，確保產品與分類資訊在單次查詢中完成加載。
* **資料庫 Schema**: 使用 Flyway 進行版本管理；資料庫 Sequence 與 Hibernate 的 `allocationSize=50` 配置保持一致以優化效能。

## 前置要求

* Config Server 運行於 `http://localhost:8888`
* Eureka 運行於 `http://localhost:8761`
* PostgreSQL (需建立 `ms-product` 資料庫；詳見 `product-service.yml`)。可使用：`docker-compose up postgres` 快速啟動。

## 執行方式

```bash
mvn spring-boot:run
```

預設埠號 (Port): 8050。

## 配置管理 (Config Server)

* **product-service.yml**: 包含資料庫連線資訊（URL、帳密透過環境變數 `MS_PG_USER`, `MS_PG_PWD` 注入）、JPA 屬性與 Flyway 配置。
* **application.yml**: 定義 Eureka 註冊中心位址與全域日誌 (Logging) 等級。