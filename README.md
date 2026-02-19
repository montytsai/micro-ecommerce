# E-Commerce Microservices Platform

> **基於 Spring Cloud 與 Java 21 構建的高併發、可擴充電商後台系統。**

本專案採用微服務架構，旨在實作一個具備服務發現、外部化配置、負載均衡與分散式追蹤的現代化電商平台。

---

## Tech Stack

* **核心框架**: Java 21, Spring Boot 3.5, Spring Cloud 2025
* **服務治理**: Netflix Eureka (Discovery), Spring Cloud Config
* **資料儲存**: MongoDB, PostgreSQL
* **開發工具**: MapStruct, Lombok, Bean Validation
* **通訊協議**: RESTful API (JSON), OpenFeign (Service-to-Service)

---

## System Architecture

系統由多個獨立運作的微服務組成，透過 **Eureka Discovery** 進行服務治理，並由 **Config Server** 統一控管環境變數。

| 服務名稱                     | 端口     | 說明                      | 關鍵技術               |
|--------------------------|--------|-------------------------|--------------------|
| **Config Server**        | `8888` | 外部化配置中心，管理所有環境設定檔       | Git-backed config  |
| **Discovery Server**     | `8761` | Eureka 註冊中心，實現服務發現與負載均衡 | Eureka Server      |
| **Customer Service**     | `8090` | 管理客戶資料、地址簿              | MongoDB, MapStruct |
| **Product Service**      | `8091` | TODO                    |                    |
| **Order Service**        | `8092` | TODO                    |                    |
| **Payment Service**      | `8093` | TODO                    |                    |
| **Notification Service** | `8094` | TODO                    |                    |

---

## Quick Start

### 1. 啟動基礎設施

確保 Docker 已安裝並啟動容器：

```bash
docker-compose up -d
```

### 2. 啟動微服務 (請遵循下列順序)

1. **Config Server**: `cd config-server && ./mvnw spring-boot:run`
2. **Discovery Server**: `cd discovery-service && ./mvnw spring-boot:run`
3. **Customer Service**: `cd customer-service && ./mvnw spring-boot:run`

### 3. 服務入口

* **Eureka Dashboard**: [http://localhost:8761](https://www.google.com/search?q=http://localhost:8761)
* **Customer API**: [http://localhost:8090/api/v1/customer](https://www.google.com/search?q=http://localhost:8090/api/v1/customer)
