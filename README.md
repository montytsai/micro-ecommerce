# E-Commerce Microservices Platform

> **基於 Spring Cloud 與 Java 21 構建的高併發、可擴充電商後台系統。**

本專案採用微服務架構，旨在實作一個具備服務發現、外部化配置、負載均衡與分散式追蹤的現代化電商平台。

---

## Tech Stack

* **核心框架**: Java 21, Spring Boot 3.5, Spring Cloud 2025
* **服務治理**: Netflix Eureka (Discovery), Spring Cloud Config
* **資料儲存**: MongoDB, PostgreSQL, Flyway (Migration)
* **開發工具**: MapStruct, Lombok, Bean Validation, @SuperBuilder
* **通訊協議**: RESTful API (JSON), OpenFeign (Service-to-Service)

---

## System Architecture

系統由多個獨立運作的微服務組成，透過 **Eureka Discovery** 進行服務治理，並由 **Config Server** 統一控管環境變數。

| 服務名稱                                      | 端口     | 說明                      | 關鍵技術                   |
|-------------------------------------------|--------|-------------------------|------------------------|
| [Config Server](./services/config-server) | `8888` | 外部化配置中心，管理所有環境設定檔       | Git-backed config      |
| [Discovery Server](./services/discovery)  | `8761` | Eureka 註冊中心，實現服務發現與負載均衡 | Eureka Server          |
| [Customer Service](./services/customer)   | `8090` | 管理客戶資料、地址簿              | MongoDB, MapStruct     |
| [Product Service](./services/product)     | `8050` | 管理產品目錄與庫存，支援併發安全扣減      | PostgreSQL, JPA, @Lock |
| **Order Service**                         | ``     | TODO                    |                        |
| **Payment Service**                       | ``     | TODO                    |                        |
| **Notification Service**                  | ``     | TODO                    |                        |

---

## Quick Start

### 1. 啟動基礎設施

確保 Docker 已安裝並啟動容器：

```bash
docker-compose up -d
```

### 2. 啟動微服務 (請遵循下列順序)

> **💡 提示：** 請等 Config Server 完全啟動後再啟動其他服務。

1. **配置中心**: `cd services/config-server && mvn spring-boot:run`
2. **註冊中心**: `cd services/discovery && mvn spring-boot:run`
3. **核心業務服務**:
   * **Customer Service**: `cd services/customer && mvn spring-boot:run`
   * **Product Service**: `cd services/product && mvn spring-boot:run`

### 3. 服務入口與監控

* **Eureka 儀表板**: [http://localhost:8761](https://www.google.com/search?q=http://localhost:8761)
* **Customer API**: [http://localhost:8090/api/v1/customer](https://www.google.com/search?q=http://localhost:8090/api/v1/customer)
* **Product API**: [http://localhost:8050/api/v1/product](https://www.google.com/search?q=http://localhost:8050/api/v1/product)

---

## 📝 專案進度追蹤

1. **✅ 已完成**:
* 基礎設施搭建 (Docker Compose)。
* 服務治理 (Config, Discovery)。
* **Customer Service**: MongoDB 整合與客戶資料管理。
* **Product Service**: 實作悲觀鎖防超賣、N+1 優化、Flyway 版控。

2. **🚧 進行中**:
* **Order Service**: 核心交易邏輯開發。
* 微服務間 OpenFeign 調用與異常傳遞處理。

3. **📅 待辦事項**:
* 實作 API Gateway 進行統一路由。
* 導入 Distributed Tracing。