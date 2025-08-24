Perfect! Let’s start step by step like a real design doc.

---

## ✅ **📌 Step 1: Functional & Non-Functional Requirements (FR & NFR)**

### ✏️ **Functional Requirements (FR)**

These are the *features* the system must support:

1. Users can create an account.
2. Users can create groups.
3. Users can add expenses:

    * Equal split
    * Exact amount split
    * Percentage-based split
4. Users can view how much they owe & how much others owe them.
5. Users can settle up/pay pending balances.
6. Users can see expense history in a group or with an individual.
7. Support for adding/removing users in groups.

---

### ⚙️ **Non-Functional Requirements (NFR)**

These are *qualities* the system should have:

* **Scalability:** Should handle millions of users & groups.
* **Low latency:** Quick balance calculation & updates.
* **Consistency:** Balances should always add up correctly.
* **Fault tolerance:** Should not lose data on crash.
* **Maintainability:** Code should follow clean architecture & SOLID.

---

## ✅ **📦 Step 2: Identify main classes & entities**

Here’s a list of potential classes/entities we’ll likely need:

| Entity/Class Name    | Purpose                                                                               |
| -------------------- | ------------------------------------------------------------------------------------- |
| `User`               | Represents a user: id, name, email, phone, etc.                                       |
| `Group`              | Represents a group of users. Contains group id, name, members list, expenses list.    |
| `Expense`            | Represents an expense: who paid, how much, description, splits.                       |
| `Split`              | Represents how an expense is split among users: user + amount.                        |
| `BalanceSheet`       | Keeps track of net balance per user (who owes whom).                                  |
| `SplitWiseService`   | Service layer exposing main operations: add expense, create group, get balances, etc. |
| `ExpenseType` (Enum) | Type of split: EQUAL, EXACT, PERCENT.                                                 |
| `ExpenseFactory`     | (Optional) To create expenses based on type (Factory pattern).                        |

---

## ✅ **Relationships (IS-A / HAS-A):**

* `Group` **has-a** list of `User` and list of `Expense`.
* `Expense` **has-a** list of `Split`.
* `User` **has-a** `BalanceSheet` (or we can centralize it in the service).
* `Split` **has-a** reference to `User` and amount.
* `SplitWiseService` is the main orchestrator (service layer).

---

Bahut badhiya! ❤️
Chaliye **simple text UML** diagram banate hain pehle, fir **balance calculation** ka logic bahut easy Hindi me samjhte hain.

---

## ✅ **📐 Quick Text UML Diagram**

```plaintext
+---------------------+
|       User          |
|---------------------|
| - id                |
| - name              |
| - email             |
|---------------------|
| + getId()           |
+---------------------+

        ^
        | has-a
        |
+---------------------+
|      BalanceSheet   |
|---------------------|
| - Map<User, Double> | ← har user ke liye balance
+---------------------+

+---------------------+
|       Group         |
|---------------------|
| - id                |
| - name              |
| - members: List<User> |
| - expenses: List<Expense> |
+---------------------+

+---------------------+
|      Expense        |
|---------------------|
| - id                |
| - paidBy: User      |
| - amount: Double    |
| - description       |
| - splits: List<Split> |
| - expenseType: Enum |
+---------------------+

+---------------------+
|       Split         |
|---------------------|
| - user: User        |
| - amount: Double    |
+---------------------+

+---------------------+
|   ExpenseType(Enum) |
| EQUAL, EXACT, PERCENT |
+---------------------+

+---------------------+
|  SplitWiseService   |
|---------------------|
| + addExpense()      |
| + showBalances()    |
| + createGroup()     |
| + getGroupExpenses()|
+---------------------+
```

---

## ✅ **🔢 Balance calculation kaise hoti hai? (Hindi, easy)**

Chalo example lete hain:

* Group: “Trip”
* Users: A, B, C

### Scenario:

* A ne ₹300 diya, split equally among A, B, C.

---

### Step by Step:

1️⃣ Total amount = 300
2️⃣ Split type = EQUAL → 3 log hain → har kisi ka share = 300/3 = 100

---

2️⃣ `paidBy` = A → usne 300 diya

---

3️⃣ Ab calculate karte hain:

* A ka share bhi 100 tha → A actually zyada (300 - 100 = 200) de chuka hai
* B ne kuch nahi diya, uska share 100 hai → B owes 100
* C bhi owes 100

---

### ✅ Balance sheet banegi:

* B owes A → 100
* C owes A → 100

Matlab: A ne 200 extra pay kiya, B aur C ne kuch nahi pay kiya to wo milkar 200 owe karte hain (100+100).

---

## **💡 Jab multiple expenses honge:**

* Har naya expense addExpense() method se add hoga
* BalanceSheet update hogi:

    * Agar B ne kuch expense diya, wo adjust ho jayega
    * Balance sheet me net difference store hota hai: “kaun kis ko kitna owe karta hai”

---

## ✅ **Easiest samajh:**

* Har expense ke liye:

    * dekho kisine kitna pay kiya
    * uska share kitna tha
    * difference = pay kiya - share
* Ye difference balance sheet me update karte rahenge

Finally:

* `showBalances()` method sabka net balance dikhata hai:

    * “A ko B se itna lena hai”
    * “B ko A ko itna dena hai”

---

### ⚙️ **Summary (Hindi):**

> * User, Group, Expense, Split → yahi main classes
> * Har expense ke liye calculate hota hai: pay kiya vs share
> * BalanceSheet me store hota hai ki kaun kis ko kitna owe karta hai
> * `SplitWiseService` sab coordinate karta hai

---
Bilkul! ❤️
Bahut simple & real-life analogy ke through, Hindi me, **flow samjhte hain**:
Imagine tumhare dost ka group trip hai — usi ko software language me translate kar rahe hain.

---

## 🧩 **Flow: SplitWise app me kya ho raha hai?**

### 🎬 **Use case:**

Aditya, Saurabh, Rahul Goa trip pe gaye.
Aditya ne ₹3000 ka hotel book kiya, aur wo sabke beech equally split karna hai.

---

### ✅ Step by step (real life + code analogy):

---

### 🏢 **1️⃣ SplitWiseService (Singleton) → Group manager**

* Ye pura system ka **single manager** hai.
* Tumhare app me ek hi SplitWiseService hota hai — sabhi groups, users, expenses isi ke paas maintain hote hain.
* Think of it as "Central Accountant Office".

```java
SplitWiseService.getInstance().addExpense(...);
```

---

### 👥 **2️⃣ Group**

* Har trip ya party ka apna ek group banta hai.
* Group me members (users) hote hain, aur expenses ki list.
* Group ke paas `addExpense` method hai — jab koi new expense add hota hai to wo members ko notify karta hai.

---

### 💰 **3️⃣ ExpenseFactory (Factory Pattern)**

* Expense create karne ke liye alag-alag type hote hain: Equal, Exact, Percent.
* Tum code me hardcoded `if-else` nahi likhte, instead:

```java
Expense expense = ExpenseFactory.createExpense(type, paidBy, amount, splits, desc);
```

* Factory decide karega kis type ka expense banana hai.

---

### 📊 **4️⃣ Strategy Pattern (SplitStrategy)**

* Expense object ke andar hota hai splitting logic.
* Different strategy classes hain:

    * EqualSplitStrategy
    * ExactSplitStrategy
    * PercentSplitStrategy
* Kal ko tumhe koi new splitting logic banana ho → nayi strategy class likh do.

---

### 🔄 **5️⃣ Balance update**

* Expense ban gaya, validate bhi ho gaya.
* Ab SplitWiseService group ka BalanceSheet update karta hai:

    * Rahul owes Aditya ₹1000
    * Saurabh owes Aditya ₹1000
* BalanceSheet: simple map, har user ka net balance maintain karta hai.

---

### 🔔 **6️⃣ Observer Pattern (Group → Users)**

* Group me members observer bane hue hain.
* Jab bhi group me new expense add hota hai:

```java
group.notifyAllObservers("New expense added: Goa Hotel ₹3000");
```

* Har user ko mobile app pe notification dikhta hai.

---

### ✅ **📦 Summary (Easy words):**

| Step | Real Life                                  | Code                        |
| ---- | ------------------------------------------ | --------------------------- |
| 1    | Aditya app pe expense add karta            | SplitWiseService.addExpense |
| 2    | System decide karta kaunsa type ka expense | ExpenseFactory              |
| 3    | Expense splitting logic                    | Strategy                    |
| 4    | Balance update hota                        | BalanceSheet                |
| 5    | Sab group members ko pata chalta           | Observer pattern            |

---

## 🧠 **Tumhare liye mental model:**

> **SplitWiseService** = Central accountant
> **ExpenseFactory** = Assistant jo decide karta kaunsa bill banana hai
> **Strategy** = Alag-alag calculator jo amount divide karte hain
> **Group** = WhatsApp group jisme message jaata hai
> **BalanceSheet** = Notebook jisme likha hota kaun kitna paisa owe karta hai

---

✅ Bas!
Tumhara high-level flow clear ho gaya:

* Expense add → Factory → Strategy → Balance update → Notify users

---
Absolutely! ✅
Let’s do this like a **senior system design engineer** explaining to a teammate who’s just starting.
Very **easy Hindi + clear diagrams + practical mindset**.

---

## 🏗️ **Imagine: SplitWise now has 100 million Daily Active Users (DAU)**

मतलब बहुत बड़ा load: करोड़ों खर्चे, balances, groups, notifications.

अब सिर्फ single server या single DB से काम नहीं चलेगा — हमें distributed, scalable, fault-tolerant system बनाना होगा.

---

## ✅ **Step by step: क्या करना होगा?**

### 🔹 1️⃣ Scale the architecture: Monolith → Microservices

अब system को तोड़ देंगे multiple independent services में:

```
+-------------------------+
|  User Service          |  --> handles user profiles, login
+-------------------------+
|  Group Service         |  --> group creation, members
+-------------------------+
|  Expense Service       |  --> add expenses, splits, balance calc
+-------------------------+
|  Notification Service  |  --> sends email/SMS/push
+-------------------------+
|  Settlement Service    |  --> tracks who paid whom
+-------------------------+
```

हर service अलग machine / cluster पर, independently scale कर सकते हैं.

---

### 🔹 2️⃣ Database choices: SQL vs NoSQL

**Expense data**:

* High write, append-only: नए expenses आते रहते हैं.
* थोड़ा denormalized भी acceptable.
  ✅ Use: NoSQL (MongoDB, DynamoDB)
* Horizontal scaling आसान
* JSON जैसी structure store → fast writes.

**User & group data**:

* ज़्यादा relational, joins needed (कौन user किस group में है).
  ✅ Use: Relational DB (PostgreSQL, MySQL)
* Data integrity important.

**Balances / ledgers**:

* Read-heavy, fast lookup चाहिए.
* Option: Redis / Memcached (cache).

---

### 🔹 3️⃣ Scaling writes & reads

* **Sharding**: expenses को multiple DB shards में distribute करो (e.g., by groupId hash).
* **Read replicas**: heavy read load को manage करने के लिए.

---

### 🔹 4️⃣ Reliability: Fault tolerance

* हर service multiple instances → load balancer.
* DB के लिए replication: primary + secondaries.
* Failure होने पर retry mechanism.

---

### 🔹 5️⃣ Caching (performance के लिए)

* Hot data जैसे balances, recent expenses → Redis में.
* Avoids DB hits → super fast.

---

### 🔹 6️⃣ Async processing

* User ने expense add किया → response जल्दी दो.
* Balance calc, notification → async process via **Kafka queue**.

```
User --> Expense Service --> Queue --> Processor updates balances + sends notifications
```

---

### 🔹 7️⃣ Idempotency

* 100 million users → duplicate API calls possible.
* Unique requestId देकर ensure: same expense बार बार न add हो.

---

### 🔹 8️⃣ Security & privacy

* Data encryption, secure auth (OAuth, JWT).
* Logs में sensitive data hide.

---

## 📦 **High-level diagram** (text):

```
Clients (mobile/web)
       |
   API Gateway
       |
-----------------------------------------
|    Expense Service  |   Group Service |
|    Notification     |   User Service  |
-----------------------------------------
|         Databases (sharded NoSQL & SQL)
|         + Caches (Redis)
|
+----> Async queues (Kafka) for processing
|
+----> Search service (ElasticSearch for group search)
```

---

## 🧠 **Summary in Hindi:**

* Monolith → Microservices → Independent scaling
* High write = NoSQL, relational part = SQL
* Read-heavy → replicas + cache
* Load ज़्यादा → sharding
* Fast + scalable → async queues
* Data safe → replication + encryption

---

## ✅ **Easiest mindset:**

> **Writes को queue से smooth करो, reads को cache से तेज करो, data को shard कर के फैलाओ, services को अलग कर के grow करो.**

---

### 6️⃣ Async Processing – क्यों ज़रूरी है और कैसे काम करता है

---

## 1. समस्या का अवलोकन (Problem Statement)

जब 100 मिलियन users रोज़ expense add करें, तो अगर आपका API हर एक काम (balance calculation, notification भेजना, database updates) **समान समय** में करे, तो response बहुत slow होगा, और users को “Please wait…” दिखाकर रख देगा।

**उदाहरण:**

* User ने ₹3000 का expense add किया
* System balance calculate करेगा, database में writes करेगा, सभी members को SMS/Email भेजेगा
* अगर ये सब sequentially करे, तो response 500ms से ऊपर जाएगा, और high load में तो seconds भी लग सकते हैं।

यहाँ हम चाहते हैं:

> **“User को तुरंत एक confirmation response दो, बाकि heavy काम पीछे से कर लो।”**

---

## 2. Async Processing का समाधान (The Async Approach)

हम **API layer** में सिर्फ सबसे ज़रूरी काम करते हैं (expense को accept करना + एक “OK” response देना) और बाकी tasks को एक **message queue** में डाल देते हैं।
बाद में separate workers (background processes) वो messages पढ़कर asynchronously अपना काम करते हैं:

* Balance calculation
* Notifications भेजना

```
Client → API Gateway → ExpenseService → “ExpenseReceived” message → [Kafka Topic]
                                                   ↓
                                             Immediate “200 OK” to Client

Background Consumers:
  • BalanceWorker ⟵ “ExpenseReceived” ⟵ Kafka    → update balances in DB
  • NotificationWorker ⟵ “ExpenseReceived” ⟵ Kafka → send SMS/Email
```

---

## 3. क्यूँ Kafka (या कोई Message Queue)?

| Requirement              | Kafka (or MQ) क्यों फिट है?                                         |
| ------------------------ | ------------------------------------------------------------------- |
| **High Throughput**      | Millions of messages per second, horizontally scale-able            |
| **Durability**           | Messages disk पर store रहते हैं until consumed                      |
| **Consumer Scalability** | अलग-अलग workers जोड़ो, load balance                                 |
| **Decoupling**           | Producers (API) and Consumers (workers) independent run कर सकते हैं |

**Real‑world analogy:**

* तुम restaurant में order place कर रहे हो (expense add)
* Order instantly accept हो जाता है और एक ticket number मिल जाता है
* Kitchen में chefs (workers) वो orders queue से लेके खाना बना रहे होते हैं और servers (notifications) customers को serve कर रहे होते हैं

---

## 4. उदाहरण से समझें (Step-by-Step Flow)

### **Step A: Expense Add करना (API Layer)**

1. User: “मैंने ₹3000 pay किया, इसे equally split कर देना”
2. API receives request, validates basic data (group exists, users valid)
3. Creates an **“ExpenseCreated”** event object:

   ```json
   {
     "eventType": "ExpenseCreated",
     "expenseId": "EXP123",
     "groupId": "G1",
     "paidBy": "Alice",
     "amount": 3000,
     "splitType": "EQUAL"
   }
   ```
4. **Publish** this event to Kafka topic `expense-events`
5. **Respond** immediately to client:

   ```
   HTTP 200 OK
   {
     "message": "Expense received!",
     "expenseId": "EXP123"
   }
   ```

### **Step B: Background Workers (Consumers)**

#### 1. BalanceWorker

* Subscribes to `expense-events`
* जब **“ExpenseCreated”** message आता है:

    1. Fetch event data
    2. Calculate splits (EqualSplitStrategy)
    3. Update `balances` table in DB:

       ```sql
       UPDATE balances
       SET amount = amount + 1000  -- for each user who owes
       WHERE userId = 'Bob';
       ```
    4. Mark event as processed (offset commit in Kafka)

#### 2. NotificationWorker

* भी subscribe करता है उसी topic को
* जब message मिलता है:

    1. Read event
    2. Generate notification texts:

        * “Alice added ₹3000 to TripToGoa. You owe ₹1000.”
    3. Send via email/SMS gateway
    4. Log notification status

> **नोट:** दोनों workers एक ही event को parallel process कर सकते हैं (consumer groups से), पर आप **idempotency** रखें कि कोई event दो बार process न हो।

---

## 5. Benefits & Trade‑offs

| Benefit                   | Trade‑off / Consideration                |
| ------------------------- | ---------------------------------------- |
| **Low Latency for Users** | Eventual consistency → data slight delay |
| **Scalable Workers**      | More operational components to manage    |
| **Fault Tolerance**       | Messages replayable on failure           |
| **Loose Coupling**        | Services independent deployable          |

**Real‑world example:**

* **E‑commerce** में OrderService भी ऐसा ही करती है:

    * OrderConfirmation तुरंत return होती है
    * Inventory update, Payment capture, Shipping label generation सब async चलते हैं

---

## 6. TL;DR (Short Summary)

> **Expense add** → **Publish event to Kafka** → **Immediate OK response** → **Background workers** handle heavy tasks (balance update, notifications)

इससे आपका user experience तेज़ रहता है, system आसानी से scale हो जाता है, और आप अलग-अलग components independently बढ़ा सकते हैं।
Absolutely! Great question — and very realistic, because in a real-world **SplitWise**-like app with millions of users, concurrency issues **definitely** arise.
Let’s break this into **three parts**, step by step:

---

## ✅ **1️⃣ Where concurrency problems can happen (real scenarios)**

Imagine our system is being used by many users simultaneously:

* Multiple users adding expenses to the same group **at the same time**
* Multiple users settling up or paying back simultaneously
* Multiple users viewing balances, while others are adding expenses
* Notifications being sent out at the same time new expenses are created
* Updating shared data structures like:

    * `Group.expenses` list
    * `Group.balances` map

These can cause:

* Race conditions: two threads updating same balance, resulting in wrong final balance
* Lost updates: e.g., one thread’s write overwriting another’s
* Dirty reads: reading data while another thread is writing to it

---

## ✅ **2️⃣ What changes we’d need to handle concurrency safely**

### a) **Synchronize critical sections / use locks**

Wherever we:

* Add expense
* Update balances
* Add/remove users to/from group

Example:

```java
synchronized(group) {
    group.getExpenses().add(newExpense);
    // update balances
}
```

Or better: use `ReentrantLock` to lock per group:

```java
private final Lock lock = new ReentrantLock();

lock.lock();
try {
    // critical updates
} finally {
    lock.unlock();
}
```

---

### b) **Use concurrent collections**

Instead of `HashMap`:

* `ConcurrentHashMap` for balances
* `CopyOnWriteArrayList` for expenses (good for more reads, fewer writes)

Example:

```java
private Map<User, Double> balances = new ConcurrentHashMap<>();
private List<Expense> expenses = new CopyOnWriteArrayList<>();
```

---

### c) **Immutable objects**

Make classes like `Expense`, `Split`, `User` immutable → once created, they won’t change → avoids concurrency issues.

---

### d) **Atomic operations**

If you just increment/decrement balances:

```java
balances.compute(user, (u, oldValue) -> oldValue + delta);
```

This way the update is atomic.

---

## ✅ **3️⃣ Summary of concrete code changes**

**In our code:**
✔️ Change:

```java
private List<Expense> expenses = new ArrayList<>();
```

to:

```java
private List<Expense> expenses = Collections.synchronizedList(new ArrayList<>());
```

or better:

```java
private List<Expense> expenses = new CopyOnWriteArrayList<>();
```

✔️ Change:

```java
private Map<User, Double> balances = new HashMap<>();
```

to:

```java
private Map<User, Double> balances = new ConcurrentHashMap<>();
```

✔️ In `addExpense` method of `Group`:

```java
public void addExpense(Expense expense) {
    synchronized(this) {
        expenses.add(expense);
        updateBalances(expense);
    }
}
```

or:

```java
lock.lock();
try {
    expenses.add(expense);
    updateBalances(expense);
} finally {
    lock.unlock();
}
```

---

## ✅ **💡 Real life analogy:**

Imagine a group of friends with a notebook (group.balances):

* If everyone writes at once → mess
* If only one person writes at a time → consistent

Using locks/concurrent collections ensures only one can “write in the notebook” at a time.

---

## ✅ **Why needed:**

* App might get thousands of simultaneous requests → each call to `addExpense` changes balances and expenses → must be thread-safe
* Avoids inconsistent balances or duplicate expenses

---

## ⚡ **If you'd like**:

* I can **rewrite `Group` and `SplitWiseService`** to be concurrency-safe
* Or add a simulation with multiple threads adding expenses

> Shall we do that? 🚀
