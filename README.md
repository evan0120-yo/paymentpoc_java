# PaymentPoc

印度市場支付聚合系統 POC。業務說明見 [交接文件.md](交接文件.md)。

本文件只負責：**把專案在本機與 prod 跑起來需要的步驟**。

---

## 前置需求

```text
必要
├─ JDK 21
├─ Maven（或用 mvnw）
├─ PostgreSQL 本機實例（localhost:5432，預設 schema 為 postgres）
└─ Docker（用於本機 RocketMQ）

可選
└─ gcloud CLI（若要接 Firestore emulator）
```

---

## 本機啟動流程

```text
Step 1  啟動 RocketMQ（nameserver + broker + dashboard）
Step 2  啟動 PostgreSQL
Step 3  啟動 Spring Boot app（profile=local）
Step 4  驗證（dashboard 看 topic、打 API 跑一條事件流）
```

### Step 1 啟動 RocketMQ

一行指令起 3 個 container（nameserver、broker、dashboard）：

```
docker compose -f d:\WorkSpace\Payment\Java\PaymentPoc\docker-compose.rocketmq.yml up -d
```

起來後會聽以下 port：

| 服務        | Port  | 用途                             |
| ----------- | ----- | -------------------------------- |
| nameserver  | 9876  | client 連線註冊 / 路由           |
| broker      | 10911 | 訊息收發                         |
| dashboard   | 8180  | Web UI，http://localhost:8180    |

> 注意：本機開發走 broker 的 `autoCreateTopicEnable=true`，topic 會在第一次發送時自動建立，程式啟動不需要 admin bootstrap。設定見 [docker/rocketmq/broker.conf](docker/rocketmq/broker.conf)。

關閉：

```
docker compose -f d:\WorkSpace\Payment\Java\PaymentPoc\docker-compose.rocketmq.yml down
```

清掉資料重來（預設沒有 volume，等同 down 再 up）：

```
docker compose -f d:\WorkSpace\Payment\Java\PaymentPoc\docker-compose.rocketmq.yml down -v
```

### Step 2 啟動 PostgreSQL

本機需要一個 Postgres，連線設定在 [src/main/resources/config/application-local.properties](src/main/resources/config/application-local.properties)：

```text
url      = jdbc:postgresql://localhost:5432/postgres
username = postgres
password = 12345678
```

> 注意：`12345678` 只是範例密碼，請自行改成你本機 Postgres 的實際密碼（或反過來把本機 Postgres 密碼設成這個值）。`application-local.properties` 與 `application-prod.properties` 裡的 `spring.datasource.password` 也請一起改。

建議用 Docker 起一個（密碼自行替換）：

```
docker run -d --name pg-paymentpoc -e POSTGRES_PASSWORD=12345678 -p 5432:5432 postgres:16
```

> 注意：JPA ddl-auto 目前是 `create-drop`，每次啟動會重建 schema。若要保留資料，自行改成 `update`。

### Step 3 啟動 Spring Boot app

用 Maven wrapper，指定 profile=local：

```
d:\WorkSpace\Payment\Java\PaymentPoc\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

或在 IDE 內執行 `PaymentPocApplication`，並在 VM options 加 `-Dspring.profiles.active=local`。

啟動成功會看到：

```text
Tomcat started on port 8082 (http) with context path '/citrus'
[consumer-group] ... CONSUMER_RUNNING
```

### Step 4 驗證

```text
驗證項
├─ http://localhost:8180                 RocketMQ Dashboard 可連
├─ Dashboard → Topic 分頁                首次發送後能看到 topic
├─ Dashboard → Consumer 分頁             4 個 consumer group 在線
└─ 打 API（詳見 交接文件.md 的 API 段落） 觀察 log 與 dashboard 訊息流
```

---

## 訊息基礎設施拓撲

```text
┌──────────────────────┐
│ Publisher 端          │  payCore / payin
│ EventPublisherAbs    │  Outbox polling → syncSend
└──────────┬───────────┘
           │ rocketmq-spring-boot-starter 2.3.4
           ▼
┌──────────────────────┐
│ RocketMQ Broker      │  本機：docker compose
│  nameserver:9876     │  prod：由 infra 管
└──────────┬───────────┘
           │
     ┌─────┼───────────┬──────────────┐
     ▼     ▼           ▼              ▼
┌────────┐┌────────┐┌──────────┐┌─────────┐
│paycore ││paycore ││payledger ││biller   │
│-init-  ││-cb-    ││-recharge-││-recharge-│
│group   ││succ-   ││succ-     ││succ-    │
│        ││group   ││group     ││group    │
└────────┘└────────┘└──────────┘└─────────┘
```

Topic 與 consumer group 對應表由 `TopicEnum` 集中管理（見 [src/main/java/com/citrus/share/enums/](src/main/java/com/citrus/share/enums/)）。

| Topic                               | Consumer Group                         | 說明                    |
| ----------------------------------- | -------------------------------------- | ----------------------- |
| `payin-init-topic`                  | `paycore-init-group`                   | 下單初始化              |
| `payin-callback-success-topic`      | `paycore-callback-success-group`       | 收款 callback 成功      |
| `paycore-recharge-success-topic`    | `payledger-recharge-success-group`     | 帳務投影                |
| `paycore-recharge-success-topic`    | `biller-recharge-success-group`        | biller 投影（fanout）   |
| `paycore-recharge-fail-topic`       | `paycore-recharge-fail-group`          | 失敗補償流              |

> 同 topic + 不同 consumer group = RocketMQ 的 fanout 行為，每個 group 都會拿到完整訊息。

---

## 重試與死信（DLQ）

```text
consumer throw exception
  ↓
RocketMQ 依退避時間自動重投
  ├─ 預設最多 16 次
  └─ 超過 → 自動進 %DLQ%<consumer-group>
       └─ 需要靠 dashboard / 告警監控
```

維運時透過 Dashboard → Topic 分頁查 `%DLQ%paycore-init-group` 等死信 topic。

---

## Profile 差異

```text
application-local.properties
├─ spring.profiles.active=local 時載入
├─ datasource 指 localhost:5432
└─ rocketmq.name-server=localhost:9876

application-prod.properties
├─ spring.profiles.active=prod 時載入
├─ datasource 指 prod DB
├─ rocketmq.name-server=<prod nameserver 位址>
└─ 啟動時執行 RocketMQAdminBootstrap 建 topic（非 autoCreate）
```

> Prod 不依賴 broker autoCreate，所有 topic 由 `RocketMQAdminBootstrap` 在 app 啟動時依 `TopicEnum` 建立。新增 topic 只改 enum，不需要手動到 broker 建。

---

## 常見問題

```text
連不到 nameserver
├─ docker ps 確認 rmqnamesrv 在跑
└─ telnet localhost 9876 確認 port 通

consumer 啟動了但收不到訊息
├─ Dashboard → Consumer 看 group 是否在線
├─ 檢查 subscribe 的 topic 名稱 / tag 拼字
└─ 本機首次發送才會建 topic，發過一次之後 dashboard 才看得到

訊息重複收到
├─ RocketMQ 保證 at-least-once，業務端需要冪等
└─ 用 outbox id / aggregateId 做去重鍵

本機重跑發現訊息還在堆積
└─ docker compose down -v 清掉 broker 資料再起
```
