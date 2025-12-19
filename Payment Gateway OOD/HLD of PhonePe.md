Perfect 👌 — yeh “**quick checklist**” ek interview ke liye **short summary** hai jo aapko payment gateway ya high-TPS system design explain karte waqt cover karni chahiye.

<!-- TOC -->
    * [1️⃣ Fast path vs Async path**]
    * [2️⃣ Idempotency**]
    * [3️⃣ Routing Engine**]
    * [4️⃣ Per-provider Adapters**]
    * [5️⃣ Kafka Buffering**]
    * [6️⃣ Partitioning Strategy**]
    * [7️⃣ Circuit Breaker, Bulkhead, Retry/Backoff, DLQ, Reconciliation**](#7-circuit-breaker-bulkhead-retrybackoff-dlq-reconciliation)
    * [8️⃣ Exactly-once Delivery (approximation)**](#8-exactly-once-delivery-approximation)
    * [9️⃣ SLOs (Service Level Objectives)**](#9-slos-service-level-objectives)
    * [✅ **Quick summary of your answer in interview:**]
<!-- TOC -->
---

### **1️⃣ Fast path vs Async path**

**👉 Meaning:**

* **Fast path** = User ke liye *immediate response* dena hota hai. (Jaise: “Payment successful!” within 200–500ms)
* **Async path** = Background me slow kaam (jaise settlement, notifications, reconciliation, retries).

**💡Example:**

* Jab user payment karta hai → aap “transaction received” bol dete ho (fast path).
* Lekin backend me actual bank confirmation, settlement, retries background async path me chalte hain.

---

### **2️⃣ Idempotency**

**👉 Meaning:**

* Agar ek hi payment request *bar-bar* aaye (network retry, user double click, etc.), toh **duplicate transaction nahi create honi chahiye**.
* Har request me ek **unique idempotency key** hoti hai → same key → same result.

**💡Example:**

* User ne accidentally “Pay Now” 2 bar click kiya → dono bar same key aayi → sirf ek hi payment record create hoga.

---

### **3️⃣ Routing Engine**

**👉 Meaning:**

* System jo decide karta hai ki **kis payment provider (bank/gateway)** se request bhejni hai.
* Decision factors ho sakte hain: provider availability, latency, success rate, cost.

**💡Example:**

* Agar Razorpay down hai → routing engine automatically CCAvenue pe route karega.

---

### **4️⃣ Per-provider Adapters**

**👉 Meaning:**

* Har provider ka API format alag hota hai (Paytm vs Stripe vs Razorpay).
* **Adapter pattern** use karte hain → ek common interface bana kar har provider ke liye custom adapter likhte hain.

**💡Example:**

```java
PaymentProviderAdapter paytmAdapter = new PaytmAdapter();
PaymentProviderAdapter stripeAdapter = new StripeAdapter();
```

→ Dono ek hi interface follow karte hain, to routing engine ko farak nahi padta.

---

### **5️⃣ Kafka Buffering**

**👉 Meaning:**

* Kafka ek message queue hai jo *buffer* ka kaam karti hai.
* High load ke time pe request direct DB me nahi jati — pehle Kafka queue me push hoti hai, fir background consumers process karte hain.

**💡Example:**

* Peak load (30k TPS) → instead of overloading DB, messages temporarily Kafka me store ho jate hain.

---

### **6️⃣ Partitioning Strategy**

**👉 Meaning:**
System ko scale karne ke liye data aur messages ko **partition** karte hain.

* **Kafka partitioning:** Merchant ID ke basis pe → ek merchant ke messages ek partition me.
* **DB partitioning:** Merchant ID ya Time ke basis pe → taaki queries fast ho aur contention kam ho.

**💡Example:**
Merchant A ke transactions DB1 me, Merchant B ke DB2 me → dono parallel handle ho sakte hain.

---

### **7️⃣ Circuit Breaker, Bulkhead, Retry/Backoff, DLQ, Reconciliation**

**👉 Meaning (simple):**

* **Circuit Breaker:** Agar koi provider baar-baar fail ho raha hai → temporarily usko calls band kar do.
* **Bulkhead:** System ke parts isolate karo taaki ek component fail hone se pura system na down ho.
* **Retry/Backoff:** Retry karo lekin smartly — har bar thoda delay badhao.
* **DLQ (Dead Letter Queue):** Failed messages ko safe jagah store karo taaki later investigate kar sako.
* **Reconciliation:** Background job jo verify karti hai ki transactions aur settlements sahi match ho rahe hain ya nahi.

---

### **8️⃣ Exactly-once Delivery (approximation)**

**👉 Meaning:**

* Practically, exactly-once processing karna mushkil hai.
* Hum **idempotency**, **atomic DB updates**, aur **Kafka transactional consumers** ka combination use karte hain.

**💡Example:**
Kafka se ek transaction consume hua → atomic DB insert hua → offset commit tabhi hota hai jab DB success hoti hai → no duplication, no loss.

---

### **9️⃣ SLOs (Service Level Objectives)**

**👉 Meaning:**

* System ke performance goals:

    * Sync path latency ≤ **200–500ms**
    * 99.9% success rate
* **Graceful degradation:** Agar provider slow hai → retry ya alternate provider se process karo.

**💡Example:**
Stripe slow hai → automatically Razorpay pe fallback.

---

### ✅ **Quick summary of your answer in interview:**

> “Our system uses a **fast vs async path** split to ensure low latency.
> Each request has an **idempotency key** to avoid duplicates.
> A **routing engine** decides the best provider via **per-provider adapters**.
> We use **Kafka buffering** for spikes and **partitioning** by merchant for scalability.
> Reliability ensured via **circuit breakers**, **bulkheads**, **retries**, and **DLQs**.
> We approximate **exactly-once delivery** using idempotency + transactional DB + Kafka.
> Finally, we meet **SLOs (200–500ms)** by degrading gracefully under load.”

---

Would you like me to draw a **colorful architecture PlantUML diagram** showing how all these (fast/async path, Kafka, routing engine, adapters) connect together?
# Database Schema Design for Payment Gateway

## 🎯 **Understanding the Problem First**

Your Payment Gateway system needs to store:
- **Who** is paying **whom**
- **How much** and in **what currency**
- **Which gateway** was used (Paytm/Razorpay)
- **What happened** (success/failure/retries)

---

## 📊 **Step-by-Step Schema Design**

### **Step 1: Identify Entities (Tables)**

From your code, we need these tables:

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│    USERS    │     │  GATEWAYS   │     │ CURRENCIES  │
└─────────────┘     └─────────────┘     └─────────────┘
       │                   │                   │
       └───────────┬───────┴───────────────────┘
                   ▼
           ┌─────────────┐
           │ TRANSACTIONS│
           └─────────────┘
                   │
                   ▼
           ┌─────────────┐
           │   RETRIES   │
           └─────────────┘
```

---

## 📋 **Table 1: USERS**

Stores all users (senders and receivers).

| Column | Type | Description |
|--------|------|-------------|
| `user_id` | INT (PK) | Unique identifier |
| `name` | VARCHAR(100) | User's name |
| `email` | VARCHAR(255) | User's email |
| `created_at` | TIMESTAMP | When user registered |

**Sample Data:**
```
┌─────────┬──────────┬─────────────────────┬─────────────────────┐
│ user_id │   name   │        email        │     created_at      │
├─────────┼──────────┼─────────────────────┼─────────────────────┤
│    1    │ Aditya   │ aditya@email.com    │ 2024-01-15 10:30:00 │
│    2    │ Shubham  │ shubham@email.com   │ 2024-01-16 11:00:00 │
│    3    │ Rahul    │ rahul@email.com     │ 2024-01-17 09:15:00 │
└─────────┴──────────┴─────────────────────┴─────────────────────┘
```

---

## 📋 **Table 2: GATEWAYS**

Stores payment gateway information.

| Column | Type | Description |
|--------|------|-------------|
| `gateway_id` | INT (PK) | Unique identifier |
| `gateway_name` | VARCHAR(50) | Name (PAYTM, RAZORPAY) |
| `max_retries` | INT | Maximum retry attempts |
| `is_active` | BOOLEAN | Is gateway currently active? |

**Sample Data:**
```
┌────────────┬──────────────┬─────────────┬───────────┐
│ gateway_id │ gateway_name │ max_retries │ is_active │
├────────────┼──────────────┼─────────────┼───────────┤
│     1      │ PAYTM        │      3      │   TRUE    │
│     2      │ RAZORPAY     │      1      │   TRUE    │
└────────────┴──────────────┴─────────────┴───────────┘
```

---

## 📋 **Table 3: CURRENCIES**

Stores supported currencies.

| Column | Type | Description |
|--------|------|-------------|
| `currency_id` | INT (PK) | Unique identifier |
| `currency_code` | VARCHAR(3) | INR, USD, EUR |
| `currency_name` | VARCHAR(50) | Indian Rupee, US Dollar |

**Sample Data:**
```
┌─────────────┬───────────────┬────────────────┐
│ currency_id │ currency_code │ currency_name  │
├─────────────┼───────────────┼────────────────┤
│      1      │ INR           │ Indian Rupee   │
│      2      │ USD           │ US Dollar      │
│      3      │ EUR           │ Euro           │
└─────────────┴───────────────┴────────────────┘
```

---

## 📋 **Table 4: TRANSACTIONS** (Main Table)

This is the **heart** of your system - stores every payment attempt.

| Column | Type | Description |
|--------|------|-------------|
| `txn_id` | INT (PK) | Unique transaction ID |
| `sender_id` | INT (FK → USERS) | Who is paying |
| `receiver_id` | INT (FK → USERS) | Who receives money |
| `amount` | DECIMAL(15,2) | Payment amount |
| `currency_id` | INT (FK → CURRENCIES) | Currency used |
| `gateway_id` | INT (FK → GATEWAYS) | Gateway used |
| `status` | ENUM | PENDING, SUCCESS, FAILED |
| `created_at` | TIMESTAMP | When transaction started |
| `updated_at` | TIMESTAMP | Last status update |

**Sample Data:**
```
┌────────┬───────────┬─────────────┬──────────┬─────────────┬────────────┬─────────┬─────────────────────┐
│ txn_id │ sender_id │ receiver_id │  amount  │ currency_id │ gateway_id │ status  │     created_at      │
├────────┼───────────┼─────────────┼──────────┼─────────────┼────────────┼─────────┼─────────────────────┤
│   101  │     1     │      2      │ 1000.00  │      1      │     1      │ SUCCESS │ 2024-01-20 14:30:00 │
│   102  │     2     │      1      │  500.00  │      2      │     2      │ FAILED  │ 2024-01-20 15:00:00 │
│   103  │     3     │      1      │ 2500.00  │      1      │     1      │ PENDING │ 2024-01-20 15:30:00 │
└────────┴───────────┴─────────────┴──────────┴─────────────┴────────────┴─────────┴─────────────────────┘
```

### 🤔 **How to Read This?**
- **txn_id=101**: Aditya (1) paid Shubham (2) ₹1000 via Paytm → SUCCESS
- **txn_id=102**: Shubham (2) paid Aditya (1) $500 via Razorpay → FAILED

---

## 📋 **Table 5: TRANSACTION_RETRIES**

Tracks each retry attempt (matches your Proxy pattern).

| Column | Type | Description |
|--------|------|-------------|
| `retry_id` | INT (PK) | Unique retry ID |
| `txn_id` | INT (FK → TRANSACTIONS) | Which transaction |
| `attempt_number` | INT | 1, 2, 3... |
| `status` | ENUM | SUCCESS, FAILED |
| `error_message` | VARCHAR(500) | Why it failed |
| `attempted_at` | TIMESTAMP | When this attempt happened |

**Sample Data:**
```
┌──────────┬────────┬────────────────┬─────────┬──────────────────────┬─────────────────────┐
│ retry_id │ txn_id │ attempt_number │ status  │    error_message     │    attempted_at     │
├──────────┼────────┼────────────────┼─────────┼──────────────────────┼─────────────────────┤
│    1     │  101   │       1        │ FAILED  │ Banking system error │ 2024-01-20 14:30:00 │
│    2     │  101   │       2        │ FAILED  │ Timeout              │ 2024-01-20 14:30:05 │
│    3     │  101   │       3        │ SUCCESS │ NULL                 │ 2024-01-20 14:30:10 │
│    4     │  102   │       1        │ FAILED  │ Insufficient funds   │ 2024-01-20 15:00:00 │
└──────────┴────────┴────────────────┴─────────┴──────────────────────┴─────────────────────┘
```

### 🤔 **How to Read This?**
- **txn_id=101**: Failed twice, succeeded on 3rd attempt (Paytm has 3 retries)
- **txn_id=102**: Failed once, no more retries (Razorpay has 1 retry)

---

## 🔗 **Entity-Relationship Diagram**

```
┌─────────────────┐
│     USERS       │
├─────────────────┤
│ PK: user_id     │◄─────────────────────────────┐
│    name         │                              │
│    email        │                              │
└─────────────────┘                              │
                                                 │
┌─────────────────┐      ┌─────────────────┐     │
│   CURRENCIES    │      │    GATEWAYS     │     │
├─────────────────┤      ├─────────────────┤     │
│ PK: currency_id │◄──┐  │ PK: gateway_id  │◄─┐  │
│    code         │   │  │    name         │  │  │
└─────────────────┘   │  │    max_retries  │  │  │
                      │  └─────────────────┘  │  │
                      │                       │  │
                      │  ┌─────────────────┐  │  │
                      │  │  TRANSACTIONS   │  │  │
                      │  ├─────────────────┤  │  │
                      │  │ PK: txn_id      │  │  │
                      └──│ FK: currency_id │  │  │
                         │ FK: gateway_id  │──┘  │
                         │ FK: sender_id   │─────┤
                         │ FK: receiver_id │─────┘
                         │    amount       │
                         │    status       │
                         └────────┬────────┘
                                  │
                                  │ 1:N
                                  ▼
                         ┌─────────────────┐
                         │ TXN_RETRIES     │
                         ├─────────────────┤
                         │ PK: retry_id    │
                         │ FK: txn_id      │
                         │    attempt_no   │
                         │    status       │
                         └─────────────────┘
```

---
```

---

## 📝 **SQL to Create Tables**

```sql
-- 1. Users Table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Currencies Table
CREATE TABLE currencies (
    currency_id INT PRIMARY KEY AUTO_INCREMENT,
    currency_code VARCHAR(3) UNIQUE NOT NULL,
    currency_name VARCHAR(50) NOT NULL
);

-- 3. Gateways Table
CREATE TABLE gateways (
    gateway_id INT PRIMARY KEY AUTO_INCREMENT,
    gateway_name VARCHAR(50) UNIQUE NOT NULL,
    max_retries INT DEFAULT 1,
    is_active BOOLEAN DEFAULT TRUE
);

-- 4. Transactions Table
CREATE TABLE transactions (
    txn_id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    currency_id INT NOT NULL,
    gateway_id INT NOT NULL,
    status ENUM('PENDING', 'SUCCESS', 'FAILED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (sender_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES users(user_id),
    FOREIGN KEY (currency_id) REFERENCES currencies(currency_id),
    FOREIGN KEY (gateway_id) REFERENCES gateways(gateway_id)
);

-- 5. Transaction Retries Table
CREATE TABLE transaction_retries (
    retry_id INT PRIMARY KEY AUTO_INCREMENT,
    txn_id INT NOT NULL,
    attempt_number INT NOT NULL,
    status ENUM('SUCCESS', 'FAILED') NOT NULL,
    error_message VARCHAR(500),
    attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (txn_id) REFERENCES transactions(txn_id)
);
```

---

## 🔍 **Sample Queries**

**1. Get all transactions by a user:**
```sql
SELECT t.txn_id, u2.name as receiver, t.amount, c.currency_code, t.status
FROM transactions t
JOIN users u1 ON t.sender_id = u1.user_id
JOIN users u2 ON t.receiver_id = u2.user_id
JOIN currencies c ON t.currency_id = c.currency_id
WHERE u1.name = 'Aditya';
```

**2. Get retry history for a failed transaction:**
```sql
SELECT attempt_number, status, error_message, attempted_at
FROM transaction_retries
WHERE txn_id = 101
ORDER BY attempt_number;
```

---

## 🎯 **Key Takeaways**

| Concept | Explanation |
|---------|-------------|
| **Primary Key (PK)** | Unique identifier for each row |
| **Foreign Key (FK)** | Links to another table's PK |
| **1:N Relationship** | One user can have many transactions |
| **Normalization** | Separate tables for users, currencies, gateways (no data duplication) |