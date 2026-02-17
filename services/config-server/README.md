# Config Server

## 📝 簡介
本服務為微服務架構中的**中央組態管理中心 (Centralized Configuration Server)**。負責統一管理所有微服務的環境設定檔（`.yml`），實現配置與程式碼的分離。

## 🚀 核心技術
- **Spring Cloud Config Server**
- **Java 21**
- **Spring Boot 3.5.10**

## ⚙️ 配置模式
- **Profile**: `native` (本地檔案系統模式)
- **搜尋路徑**: `src/main/resources/configurations/`
- **服務埠號**: `8888`

## 🛠 啟動說明
1. 確保已安裝 Java 21。
2. 進入目錄並執行：
   ```bash
   ./mvnw spring-boot:run
    ```

## 📂 設定檔存取規則

其他服務連線至此 Server 時，會根據其 `spring.application.name` 讀取對應檔案：

* 例如：服務名為 `discovery-service`，則會讀取 `configurations/discovery-service.yml`。

## 📍 常用端點

* 檢查特定服務配置：`GET http://localhost:8888/{service-name}/{profile}`