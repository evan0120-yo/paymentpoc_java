
# 專案架構說明書 (AI Read Only)

本文件旨在描述 PaymentPoc 專案的核心架構、設計模式與業務流程，供 AI 助理讀取後，能準確地向開發人員解釋系統運作原理。

## 1. 系統概觀 (System Overview)
本專案為一個 **支付聚合系統 (Payment Aggregator POC)**，主要功能是整合多個上游支付渠道 (如 Mpurse, Billdesk)，提供統一的支付接口給下游業務系統使用。

*   **核心目標**: 屏蔽不同支付渠道的實作差異，提供統一的 API 與流程。
*   **市場背景**: 印度支付市場 (India Payment Market)。
    *   **高併發特性**: 預期尖峰 QPS 達 3000+，系統設計需具備極高的吞吐量與低延遲。
    *   **業務模式**: 本公司無電子支付執照 (No License)，扮演 **支付聚合商 (Aggregator)** 角色。
        *   **Payin**: 用戶資金進入第三方支付公司 (甲方) 的信託/收款戶頭。
        *   **Payout**: 資金從甲方戶頭代付給用戶 (如繳費、充值)。
*   **技術堆疊**:
    *   **語言**: Java 21 (核心依賴 **Virtual Threads** 以應對 3000+ QPS 的高併發 I/O)
    *   **框架**: Spring Boot 3.5.5
    *   **資料庫**: PostgreSQL (交易資料), GCP Firestore (NoSQL 資料)
    *   **MQ**: GCP Pub/Sub (事件驅動)

## 2. 分層架構 (Layered Architecture)
系統採用 **4層架構 (Controller > Usecase > Service > Repo)**，並結合 **CQRS (Command Query Responsibility Segregation)** 概念。這不是完整的 DDD，而是實用導向的改良架構。

### 2.1 層級定義
1.  **Controller**: 接入層，無業務邏輯。
2.  **Usecase**: 業務編排層 (Orchestration)。
3.  **Service**: 業務實作層 (Implementation)。
4.  **Repo (Dao)**: 資料存取層。

### 2.2 橫向溝通規則 (Communication Rules)
為了避免依賴混亂，我們定義了嚴格的溝通規則：

*   **Store (Write/Command) 路徑**:
    *   `Usecase (Store)` **只能** 呼叫旗下的 `Service (Store)`。
    *   `Usecase (Store)` **可以** 呼叫其他的 `Usecase (Store)` 進行業務串聯。
    *   **禁止**: `Usecase (Store)` 直接呼叫其他模組的 `Service (Store)` (必須透過 Usecase)。

*   **Query (Read) 路徑**:
    *   `Usecase (Store/Query)` **可以** 直接呼叫任何平行的 `Service (Query)` 或 `Usecase (Query)`。
    *   **理由**: 查詢操作通常無副作用，允許較寬鬆的依賴以提升開發效率與效能。

## 3. 核心設計哲學：蜈蚣架構 (Centipede Architecture)
我們的目標是讓核心支付邏輯極度穩定，像蜈蚣的身體一樣不動，而將變化與副作用像蜈蚣的腳一樣切分出去。

### 3.1 核心穩定 (Stable Core)
*   **核心支付邏輯**: 包含狀態機流轉、核心檢核、路由選擇。這部分程式碼應盡量少改動。
*   **工廠隔離**: 新增甲方渠道時，透過新增 `Factory` 實作來擴充，完全不觸碰核心 Usecase 程式碼。

### 3.2 事件驅動 (Event-Driven Side Effects)
*   **切分副作用**: 任何非核心的業務（如通知、報表、大數據分析、非同步對帳）都應切分為 **事件 (Event)**。
*   **MQ 整合**: 事件層應將訊息發送到 MQ (GCP Pub/Sub)，由外部 Consumer 處理。
*   **好處**: 核心流程不會因為副作用的失敗或變更而受影響，且能輕易擴充新的副作用處理者。

## 4. 可靠性設計 (Reliability Design)
為了確保在高併發與分散式環境下的資料正確性，我們引入了以下機制：

### 4.1 Transactional Outbox Pattern
*   **目的**: 確保「資料庫寫入」與「MQ 訊息發送」的原子性 (Atomicity)。
*   **實作**:
    1.  在同一個 DB Transaction 中，將業務資料寫入 Table，並將要發送的 Event 寫入 `Outbox` Table。
    2.  DB Commit 成功後，由獨立的 Relay Process (或 CDC 工具) 讀取 `Outbox` Table 並發送到 GCP Pub/Sub。
    3.  **解決問題**: 避免了「DB 寫入成功但 MQ 發送失敗」導致的資料不一致。

### 4.2 樂觀鎖 (Optimistic Locking)
*   **目的**: 解決高併發下的資料競爭 (Race Condition) 問題。
*   **實作**: 在資料表中增加 `version` 欄位。更新時檢查 `version` 是否變更 (CAS - Compare And Swap)。
*   **應用**: 所有涉及狀態機流轉的 Table (如 PayinRecord, PayoutRecord) 必須實作樂觀鎖。

### 4.3 事件時序性 (Event Ordering)
*   **挑戰**: 分散式系統中，MQ 訊息可能會亂序到達 (Out of Order)。
*   **對策**:
    1.  **Payload 包含 Version/Timestamp**: 接收端需檢查事件的版本號或時間戳。
    2.  **狀態機保護**: Paycore 或各模組的狀態機必須具備防禦能力，若收到舊狀態的事件應直接忽略或報錯，確保狀態不會倒退。

## 5. 模組架構與職責 (Module Architecture)
系統採用微服務/模組化設計，並明確區分 **Write Model (交易核心)** 與 **Read Model (查詢顯示)**。

### 5.1 交易核心模組 (Write Model - RDBMS)
此類模組注重**資料一致性 (Consistency)**，底層使用 **PostgreSQL**。

*   **Payin Module**:
    *   **職責**: 處理所有收款業務。
    *   **狀態**: 維護 Payin 自身的狀態機。
    *   **日誌**: 完整記錄與上游甲方 (3rd Party) 的 API 溝通日誌 (Request/Response)。
*   **Payout Module**:
    *   **職責**: 處理所有出款/代付業務 (如繳費、充值)。
    *   **狀態**: 維護 Payout 自身的狀態機。
    *   **日誌**: 完整記錄與上游甲方的 API 溝通日誌。
*   **Paycore Module (Central Orchestrator)**:
    *   **職責**: 系統的**總狀態機**與大腦。
    *   **Saga 模式**: 扮演 Saga Orchestrator 角色。所有關鍵事件 (Event) 必定流向 Paycore，由 Paycore 依據業務邏輯運算後，決定發送指令給 Payin, Payout 或其他模組。
    *   **目的**: 確保跨模組業務流程的順序性與一致性，避免模組間網狀調用造成混亂。

### 5.2 查詢與顯示模組 (Read Model)
此類模組注重**查詢效能與特定視圖**，資料來源通常由事件同步而來。

*   **Biller Module**:
    *   **職責**: 提供 App 端 (User Facing) 的帳單顯示與查詢。
*   **PayLedger Module**:
    *   **職責**: 提供後台端 (Admin/Finance Facing) 的對帳、報表與交易紀錄顯示。

### 5.3 共用層 (Shared Layer)
*   **Common Module**: 跨模組共用的業務邏輯或定義。
*   **Share Package**: 純工具類 (Utils) 的共用包。

## 6. 關鍵設計模式 (Design Patterns)

### 6.1 工廠模式 (Factory Pattern)
*   **應用**: 用於 `PayinChannelFactory` 等。
*   **目的**: 實現 **Open-Closed Principle (開閉原則)**。新增支付渠道時，只需新增一個實作類別並註冊到 Factory，無需修改 Usecase 層的程式碼。

### 6.2 策略模式 (Strategy Pattern)
*   **應用**: 用於 `PayinStrategyFactory` 與 `PayinPaRouteUsecase`。
*   **目的**: 允許在執行時期動態切換路由邏輯（例如：依據費率、依據成功率、或是輪詢）。

### 6.3 樣板方法 (Template Method) - *隱性使用*
*   **應用**: 在 Usecase 中定義了標準的支付流程（驗證 -> 路由 -> 執行 -> 回調），具體細節由各個 Factory 實作決定。

