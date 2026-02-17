# Discovery Server (Eureka)

## 📝 簡介
本服務為基於 **Netflix Eureka** 實作的**服務註冊與發現中心 (Service Discovery)**。它就像是微服務體系中的「電話簿」，負責紀錄所有執行中服務實例的位址與狀態。

## 🚀 核心技術
- **Spring Cloud Netflix Eureka Server**
- **Spring Cloud Config Client**
- **Java 21**

## ⚙️ 關鍵配置
- **服務埠號**: `8761`
- **組態來源**: 從 `http://localhost:8888` (Config Server) 獲取進階設定。
- **自我保護模式**: 本地開發環境已關閉自我註冊 (`registerWithEureka: false`)。

## 🛠 啟動說明
> **重要**：必須先啟動 `config-server`。
1. 確保 Config Server (8888) 已在運行。
2. 進入目錄並執行：
   ```bash
   ./mvnw spring-boot:run
    ```

## 📊 管理介面

啟動後可訪問 Eureka Dashboard 監看服務狀態：

* **URL**: [http://localhost:8761]()

## 📍 相依性

* 依賴於 `config-server` 提供 `discovery-service.yml` 配置。