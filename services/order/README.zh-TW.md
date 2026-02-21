# 訂單服務 (Order Service)

訂單管理微服務：負責核心訂單流程編排，包含透過遠端調用驗證客戶與產品庫存、訂單資料持久化，並將訂單確認事件發佈至 Kafka。

[English](README.md) | 繁體中文

## 技術棧

- **Java 21** · **Spring Boot 3.5** · **Spring Cloud 2025**
- **PostgreSQL** · **Spring Data JPA**
- **OpenFeign** (用於服務間通訊：customer-service, product-service)
- **Apache Kafka** (非同步發送訂單確認通知)
- **MapStruct** · **Lombok** · **Bean Validation** · **ULID**
- **Config Server** · **Eureka**

## API 概覽

| 方法   | 端點                             | 說明                              |
|------|--------------------------------|---------------------------------|
| GET  | `/api/v1/order`                | 列出所有訂單                          |
| GET  | `/api/v1/order/{order-id}`     | 根據 ID 獲取訂單主檔詳情                  |
| POST | `/api/v1/order`                | 建立訂單 (驗證客戶、寫入 DB、扣除庫存、發送 Kafka) |
| GET  | `/api/v1/orderline/{order-id}` | 取得特定訂單的所有明細項目                   |

## 建立訂單流程 (Create Order Flow)

1. **驗證客戶** – 透過 `CustomerClient` (Feign) 確認客戶身份。
2. **持久化訂單 (本地優先)** – 使用 **ULID** 作為識別碼，先將訂單與明細寫入 PostgreSQL。利用 `@Transactional` 確保失敗時可自動回滾。
3. **扣除庫存** – 透過 `ProductClient` 調用 `/purchase` 進行遠端庫存扣除。
4. **支付處理** – 預留實作位置 (TODO; 設定檔中的 `payment-url` 供未來整合)。
5. **非同步通知** – 透過 `OrderProducer` 將 `OrderConfirmation` 發送至 Kafka `order-topic`。

## 設計亮點

- **ID 策略**: 全面採用 **ULID**。兼具 UUID 的唯一性與 Sequence 的可排序性，優化資料庫 B-Tree 索引效能並適合微服務分散式環境。
- **資料一致性**: 採「先寫本地、後調遠端」的交易順序，確保遠端服務異常時能觸發本地資料庫回滾，避免產生無效訂單。
- **異常處理機制**: 實作全域異常攔截器，特別針對 `FeignException` 進行封裝，防止分散式系統中的錯誤雪崩。
- **自動化稽核**: 透過 `BaseEntity` 統一封裝 `createdAt` 與 `updatedAt` 稽核欄位。

## 環境需求

- **Config Server**: `http://localhost:8888`
- **Eureka**: `http://localhost:8761`
- **PostgreSQL**: 資料庫名稱 `ms-order`
- **Kafka**: `localhost:9092` (可使用 docker-compose 啟動)
- **依賴服務**: 客戶服務 (8090)、產品服務 (8050)

## 執行方式

```bash
mvn spring-boot:run

```

預設埠號: **8070**。