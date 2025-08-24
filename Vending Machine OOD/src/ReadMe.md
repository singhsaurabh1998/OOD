
---

## ✅ Next Beginner-Friendly OOD Problem:

Let's go with:

### 🧾 **Design a Vending Machine**

This is **intermediate in complexity** but still beginner-friendly, and covers many core OOD concepts:

---

## 🧠 Problem Statement

Design a **Vending Machine** that:

1. Accepts coins.
2. Allows selection of items (like Coke, Pepsi, Snacks).
3. Dispenses item if enough money is inserted.
4. Returns change if required.
5. Allows refilling of items.

---

## 💡 Concepts You'll Learn

| Concept       | Why It’s Important                                     |
| ------------- | ------------------------------------------------------ |
| Enum          | For coins & items                                      |
| State Machine | Machine states like IDLE, WAITING, DISPENSING          |
| Encapsulation | Separating coin handling and inventory logic           |
| SOLID         | Apply Single Responsibility and Open/Closed principles |
| Interface     | Strategy for payment logic (later extensions)          |

---

## 🚦 Suggested Breakdown (Step-by-Step)

We’ll build this incrementally:

### ✅ Task 1: Define coins and items

* Enum for `Coin` (with value)
* Class/enum for `Item` (with price)

### ✅ Task 2: Inventory class

* Track item quantities.

### ✅ Task 3: VendingMachine class

* Insert coin
* Select item
* Dispense item
* Return change

### ✅ Task 4: Add state handling (IDLE, PROCESSING, DISPENSING)

---

## ✅ Shall we start with **Task 1: Coin and Item Definitions**?

Let me know and I’ll explain line-by-line just like we did for the Coffee Machine.
Perfect! Let’s start building the **Vending Machine** with **Task 1: Coin and Item Definitions**.

We’ll create two basic components:

1. ✅ `Coin` — Enum with supported denominations: ₹10, ₹20, ₹50, ₹100
2. ✅ `Item` — Enum (or class) to define vending items with names and prices (e.g., Coke ₹25)

---

## ✅ Step 1: `Coin` Enum

```java
public enum Coin {
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    HUNDRED(100);

    private final int value;

    Coin(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
```

### ✅ Explanation:

| Line                       | Explanation                            |
| -------------------------- | -------------------------------------- |
| `public enum Coin`         | Java enum to represent fixed coins     |
| `TEN(10)` etc.             | Enum constants with value              |
| `private final int value;` | Stores the actual ₹ value              |
| `getValue()`               | Returns coin’s value like 10, 50, etc. |

---

## ✅ Step 2: `Item` Enum

```java
public enum Item {
    COKE("Coke", 25),
    PEPSI("Pepsi", 35),
    JUICE("Juice", 45),
    SNACKS("Snacks", 50);

    private final String name;
    private final int price;

    Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
```

### ✅ Explanation:

| Line                         | Explanation                         |
| ---------------------------- | ----------------------------------- |
| `Item("Coke", 25)`           | Represents item with name and price |
| `private final String name;` | Human-readable label                |
| `private final int price;`   | Price in ₹                          |
| `getName()` / `getPrice()`   | Accessors                           |

---

## 🔍 Usage Example:

```java
Coin coin = Coin.FIFTY;
System.out.println("Inserted coin: ₹" + coin.getValue());

Item item = Item.COKE;
System.out.println("Selected item: " + item.getName() + " costing ₹" + item.getPrice());
```

---

Would you like to move on to **Task 2: Inventory System**, where we store and manage the available items and their counts?

Or do you want a quick test/demo class for Coin & Item first?


Awesome! Let's build **Task 2: Inventory System** for the vending machine.

---

## 🎯 Goal of Inventory System

We'll create a class `Inventory` to:

1. ✅ Track **available quantity** of each `Item`.
2. ✅ Allow:

    * Adding items (refilling).
    * Reducing items (when dispensing).
    * Checking item availability.
    * Printing current stock.

---

## ✅ Step-by-Step Design

We'll use:

* `Map<Item, Integer>` → maps each item to its **available count**.
* Synchronized methods → to keep things thread-safe if we extend later.

---

## ✅ `Inventory.java`

```java
import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<Item, Integer> itemStock;

    public Inventory() {
        itemStock = new HashMap<>();
        // Initialize inventory with 0 quantity for all items
        for (Item item : Item.values()) {
            itemStock.put(item, 0);
        }
    }

    // Add stock
    public void addItem(Item item, int quantity) {
        int current = itemStock.getOrDefault(item, 0);
        itemStock.put(item, current + quantity);
        System.out.println("✔️ Refilled " + item.getName() + " by " + quantity + " units");
    }

    // Reduce stock (if available)
    public boolean useItem(Item item) {
        int current = itemStock.getOrDefault(item, 0);
        if (current > 0) {
            itemStock.put(item, current - 1);
            return true;
        }
        return false;
    }

    // Check if item is available
    public boolean isAvailable(Item item) {
        return itemStock.getOrDefault(item, 0) > 0;
    }

    // Print current stock
    public void printStock() {
        System.out.println("📦 Current Inventory:");
        for (Map.Entry<Item, Integer> entry : itemStock.entrySet()) {
            System.out.println("• " + entry.getKey().getName() + ": " + entry.getValue() + " units");
        }
    }
}
```

---

## 🔍 Explanation

| Method          | Purpose                                       |
| --------------- | --------------------------------------------- |
| `addItem()`     | Refill stock for a given item                 |
| `useItem()`     | Dispense item (if available), reduce quantity |
| `isAvailable()` | Checks if the item is in stock                |
| `printStock()`  | Displays all item quantities                  |

---

## 🧪 Example Usage

```java
Inventory inventory = new Inventory();
inventory.addItem(Item.COKE, 5);
inventory.addItem(Item.PEPSI, 2);

inventory.printStock(); // shows current item levels

System.out.println("Is Coke available? " + inventory.isAvailable(Item.COKE)); // true

inventory.useItem(Item.COKE); // dispense
inventory.printStock();
```

---

Ready for **Task 3: VendingMachine class** that uses Coin, Item, and Inventory?

It’ll cover:

* inserting coins,
* selecting an item,
* checking price,
* dispensing item,
* returning change if needed.

Great! Let's move to **Task 3: VendingMachine class**, where we’ll put everything together:

---

## 🧠 Features of `VendingMachine` (Basic Version)

The class will:

1. Accept coins.
2. Track the inserted amount.
3. Allow item selection.
4. Check if enough money is inserted.
5. Dispense item and return remaining change.
6. Interact with `Inventory`.

---

## ✅ Class Responsibilities Breakdown

| Responsibility           | Belongs to       |
| ------------------------ | ---------------- |
| Accept & track coins     | `VendingMachine` |
| Maintain item stock      | `Inventory`      |
| Represent item and price | `Item` enum      |
| Coin value               | `Coin` enum      |

---

## ✅ `VendingMachine.java`

```java
import java.util.ArrayList;
import java.util.List;

public class VendingMachine {
    private final Inventory inventory;
    private final List<Coin> insertedCoins;
    private int currentBalance;

    public VendingMachine(Inventory inventory) {
        this.inventory = inventory;
        this.insertedCoins = new ArrayList<>();
        this.currentBalance = 0;
    }

    // User inserts a coin
    public void insertCoin(Coin coin) {
        insertedCoins.add(coin);
        currentBalance += coin.getValue();
        System.out.println("💰 Inserted: ₹" + coin.getValue() + " | Current Balance: ₹" + currentBalance);
    }

    // User selects an item
    public void selectItem(Item item) {
        System.out.println("➡️ Selected Item: " + item.getName() + " costing ₹" + item.getPrice());

        if (!inventory.isAvailable(item)) {
            System.out.println("❌ " + item.getName() + " is out of stock.");
            return;
        }

        if (currentBalance < item.getPrice()) {
            System.out.println("⚠️ Not enough balance. Please insert ₹" + (item.getPrice() - currentBalance) + " more.");
            return;
        }

        // Dispense item
        boolean dispensed = inventory.useItem(item);
        if (dispensed) {
            currentBalance -= item.getPrice();
            System.out.println("✅ Dispensing: " + item.getName());
            returnChange();
        } else {
            System.out.println("❌ Unable to dispense item.");
        }
    }

    // Return remaining change
    private void returnChange() {
        if (currentBalance > 0) {
            System.out.println("💸 Returning change: ₹" + currentBalance);
            currentBalance = 0;
        }
        insertedCoins.clear();
    }

    // Show available items
    public void showItems() {
        inventory.printStock();
    }

    // Refill item (admin method)
    public void refillItem(Item item, int quantity) {
        inventory.addItem(item, quantity);
    }
}
```

---

## ✅ Explanation of Key Methods

| Method           | What It Does                                   |
| ---------------- | ---------------------------------------------- |
| `insertCoin()`   | Adds coin to balance                           |
| `selectItem()`   | Dispenses item if available and enough balance |
| `returnChange()` | Returns leftover money                         |
| `refillItem()`   | Admin-only refill operation                    |
| `showItems()`    | Displays inventory stock                       |

---

## 🧪 Sample Usage (Main)

```java
public class Main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        VendingMachine machine = new VendingMachine(inventory);

        // Refill inventory
        machine.refillItem(Item.COKE, 3);
        machine.refillItem(Item.PEPSI, 2);

        machine.showItems();

        // User session
        machine.insertCoin(Coin.TWENTY); // ₹20
        machine.insertCoin(Coin.TEN);    // ₹10

        machine.selectItem(Item.COKE);   // ₹25 item
        machine.selectItem(Item.PEPSI);  // Not enough balance now
    }
}
```

---

## ✅ Output Example

```
✔️ Refilled Coke by 3 units
✔️ Refilled Pepsi by 2 units
📦 Current Inventory:
• Coke: 3 units
• Pepsi: 2 units
• Juice: 0 units
• Snacks: 0 units
💰 Inserted: ₹20 | Current Balance: ₹20
💰 Inserted: ₹10 | Current Balance: ₹30
➡️ Selected Item: Coke costing ₹25
✅ Dispensing: Coke
💸 Returning change: ₹5
➡️ Selected Item: Pepsi costing ₹35
⚠️ Not enough balance. Please insert ₹35 more.
```

---

## 📌 What You’ve Achieved So Far

* Used **enums** for controlled input.
* Designed an **Inventory system** for stock.
* Designed a **VendingMachine** that follows OOP principles.
* No advanced patterns yet — still beginner-friendly and SOLID-compliant.

---

Would you like to now:

* Extend this with **State pattern** (`IDLE`, `PROCESSING`, etc.)?
* Or simulate **multi-user access** (add multithreading)?
* Or add **coin change system** (return coins, not just amount)?

Let me know your preferred next step 🔁
Absolutely! You're progressing really well — now it's time to introduce the **State Design Pattern** to make your vending machine behavior even more organized and extensible.

---

## 🎯 Step 1: What is the **State Design Pattern**?

### 🧠 Definition:

The **State Pattern** allows an object to change its behavior when its **internal state** changes.
Think of it as **switching modes** in a machine, each with its own behavior.

---

## 🎮 Real-Life Analogy: Vending Machine

Imagine you're at a vending machine. It behaves differently based on its state:

| State          | User Action | Machine Reaction                         |
| -------------- | ----------- | ---------------------------------------- |
| `IDLE`         | Insert coin | Accept coin                              |
| `HAS_MONEY`    | Select item | Check stock and dispense if enough money |
| `DISPENSING`   | Wait        | Dispensing item                          |
| `OUT_OF_STOCK` | Select item | Show error                               |

---

## ✅ Why Use State Pattern?

| Without State Pattern               | With State Pattern                   |
| ----------------------------------- | ------------------------------------ |
| Long if-else or switch blocks       | Clean separation of logic            |
| Hard to add new states              | Easily extendable                    |
| Logic of one state leaks into other | Each state encapsulates its behavior |

---

## 🏗️ Step 2: How Do We Implement It?

We'll break the implementation like this:

### 📦 Interfaces and States

1. `VendingMachineState` (interface)
2. Concrete states:

    * `IdleState`
    * `HasMoneyState`
    * `DispensingState`

### 🏢 Main Machine Class

3. `VendingMachine` class holds a reference to current state and delegates all actions to it.

---

## ✅ Step-by-Step Implementation Plan

Shall we begin with `VendingMachineState` and the first state: `IdleState`?

This will include:

* Interface definition
* First state (Idle) behavior
* And explain how transition will happen

Let me know and I’ll continue with code + line-by-line explanation.
Excellent! Let’s implement the **State Pattern** for your Vending Machine.

---

## ✅ Step 1: Define the `VendingMachineState` Interface

This interface defines **what actions a state must support**:

```java
public interface VendingMachineState {
    void insertCoin(VendingMachine machine, Coin coin);
    void selectItem(VendingMachine machine, Item item);
    void dispenseItem(VendingMachine machine);
}
```

### ✅ Line-by-Line Explanation:

| Line                            | Meaning                                       |
| ------------------------------- | --------------------------------------------- |
| `interface VendingMachineState` | Base interface for all states                 |
| `insertCoin(...)`               | What to do when a coin is inserted            |
| `selectItem(...)`               | What to do when an item is selected           |
| `dispenseItem(...)`             | What to do when the machine tries to dispense |

Every **state class** (Idle, HasMoney, etc.) will implement this interface.

---

## ✅ Step 2: Implement `IdleState`

```java
public class IdleState implements VendingMachineState {

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addToBalance(coin);
        System.out.println("💰 Coin inserted: ₹" + coin.getValue());
        machine.setState(new HasMoneyState()); // Transition to next state
    }

    @Override
    public void selectItem(VendingMachine machine, Item item) {
        System.out.println("❌ Insert coin before selecting an item.");
    }

    @Override
    public void dispenseItem(VendingMachine machine) {
        System.out.println("❌ No item to dispense. Insert coin and select item first.");
    }
}
```

---

### ✅ Line-by-Line Explanation:

| Line                             | Meaning                                                                          |
| -------------------------------- | -------------------------------------------------------------------------------- |
| `implements VendingMachineState` | Fulfills the contract — defines behavior for this state                          |
| `insertCoin(...)`                | When a coin is inserted in IDLE state, add to balance and move to HasMoney state |
| `machine.addToBalance(coin)`     | Adds value to the current balance                                                |
| `machine.setState(...)`          | Changes the current state of machine                                             |
| `selectItem(...)`                | Not allowed in IDLE state — prompts to insert coin first                         |
| `dispenseItem(...)`              | Not allowed — machine hasn’t received coin or item yet                           |

---

## ✅ Step 3: Modify `VendingMachine` to use states

Now let’s prepare the `VendingMachine` skeleton to:

* Hold a reference to current state.
* Delegate actions to the state class.

```java
public class VendingMachine {
    private VendingMachineState currentState;
    private final Inventory inventory;
    private int currentBalance;
    private Item selectedItem;

    public VendingMachine(Inventory inventory) {
        this.inventory = inventory;
        this.currentState = new IdleState(); // Starts in IDLE
        this.currentBalance = 0;
    }

    public void insertCoin(Coin coin) {
        currentState.insertCoin(this, coin);
    }

    public void selectItem(Item item) {
        currentState.selectItem(this, item);
    }

    public void dispenseItem() {
        currentState.dispenseItem(this);
    }

    // Setters & helpers for states to access internal data

    public void setState(VendingMachineState newState) {
        this.currentState = newState;
    }

    public void addToBalance(Coin coin) {
        this.currentBalance += coin.getValue();
    }

    public int getCurrentBalance() {
        return currentBalance;
    }

    public void deductBalance(int amount) {
        this.currentBalance -= amount;
    }

    public void setSelectedItem(Item item) {
        this.selectedItem = item;
    }

    public Item getSelectedItem() {
        return selectedItem;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void reset() {
        this.currentBalance = 0;
        this.selectedItem = null;
        this.setState(new IdleState());
    }
}
```

---

### ✅ What Changed in `VendingMachine`?

| Part                                       | Purpose                                             |
| ------------------------------------------ | --------------------------------------------------- |
| `currentState`                             | Delegates behavior to current state                 |
| `setState(...)`                            | Used by state classes to switch the machine's state |
| `insertCoin()/selectItem()/dispenseItem()` | All delegate to the state instance                  |
| `reset()`                                  | Used to reset machine to IDLE after dispensing      |

---

## 🛠️ Next Step

Now that we’ve completed `IdleState`, shall we move on to:

✅ `HasMoneyState` — handles selecting item after inserting coins?

Let me know and we’ll continue with that + code explanation.
Great! Let’s move on to the next concrete state: **`HasMoneyState`**.

This state represents the machine **after the user has inserted coins** but **hasn’t yet completed the transaction** (item not yet dispensed).

---

## ✅ Role of `HasMoneyState`

When the vending machine is in `HasMoneyState`, it should allow:

1. 👉 Accepting more coins.
2. ✅ Selecting an item (if sufficient funds and item available).
3. ❌ Not allow dispensing directly without item selection.

---

## 🧱 `HasMoneyState.java`

```java
public class HasMoneyState implements VendingMachineState {

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addToBalance(coin);
        System.out.println("💰 Additional coin inserted: ₹" + coin.getValue());
        System.out.println("💵 Current Balance: ₹" + machine.getCurrentBalance());
    }

    @Override
    public void selectItem(VendingMachine machine, Item item) {
        System.out.println("🛍️ Item selected: " + item.getName() + " costing ₹" + item.getPrice());

        if (!machine.getInventory().isAvailable(item)) {
            System.out.println("❌ Item out of stock. Please select a different item.");
            return;
        }

        if (machine.getCurrentBalance() < item.getPrice()) {
            int required = item.getPrice() - machine.getCurrentBalance();
            System.out.println("⚠️ Insufficient balance. Insert ₹" + required + " more.");
            return;
        }

        // Save selected item
        machine.setSelectedItem(item);

        // Move to next state (Dispensing)
        machine.setState(new DispensingState());
        machine.dispenseItem(); // Proceed to dispense
    }

    @Override
    public void dispenseItem(VendingMachine machine) {
        System.out.println("❌ Please select an item before dispensing.");
    }
}
```

---

## ✅ Line-by-Line Explanation

### `insertCoin(...)`

* Allows inserting more coins while in this state.
* Updates balance.

### `selectItem(...)`

* Checks:

    * Is item available?
    * Does the user have enough balance?
* If yes:

    * Saves selected item in machine.
    * Switches to `DispensingState`.
    * Automatically calls `machine.dispenseItem()`.

### `dispenseItem(...)`

* Prevents direct dispense without selecting an item.

---

## 📌 At this point:

We’ve now completed:

✅ `VendingMachineState` interface
✅ `IdleState` — accepts coins, waits for selection
✅ `HasMoneyState` — accepts more coins, allows item selection and validates balance

---

### 🔜 Next State?

Ready to implement `DispensingState`?

It will:

* Deduct balance
* Reduce item from inventory
* Return change if needed
* Reset machine to `IdleState`

Let me know and we’ll build it next!
Perfect! Let's implement the final and most crucial state: **`DispensingState`**.

This is the state where the **transaction is finalized**:
The machine **dispenses the selected item**, **deducts the balance**, and **resets itself to IDLE**.

---

## 🎯 Responsibilities of `DispensingState`

| Action              | Behavior                                                        |
| ------------------- | --------------------------------------------------------------- |
| `insertCoin(...)`   | Not allowed while dispensing                                    |
| `selectItem(...)`   | Not allowed — already in dispensing process                     |
| `dispenseItem(...)` | Deducts balance, dispenses item, returns change, resets machine |

---

## ✅ `DispensingState.java`

```java
public class DispensingState implements VendingMachineState {

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        System.out.println("❌ Cannot insert coins while dispensing.");
    }

    @Override
    public void selectItem(VendingMachine machine, Item item) {
        System.out.println("❌ Already processing another item. Please wait.");
    }

    @Override
    public void dispenseItem(VendingMachine machine) {
        Item selected = machine.getSelectedItem();
        if (selected == null) {
            System.out.println("❌ No item selected to dispense.");
            machine.setState(new IdleState()); // Reset to IDLE anyway
            return;
        }

        System.out.println("✅ Dispensing: " + selected.getName());

        // Update inventory
        boolean success = machine.getInventory().useItem(selected);
        if (!success) {
            System.out.println("❌ Dispensing failed. Item suddenly out of stock.");
            machine.reset(); // Back to idle and clear state
            return;
        }

        // Deduct item price
        machine.deductBalance(selected.getPrice());

        // Return change if any
        int remaining = machine.getCurrentBalance();
        if (remaining > 0) {
            System.out.println("💸 Returning change: ₹" + remaining);
        }

        // Reset machine state
        machine.reset();
    }
}
```

---

## ✅ Line-by-Line Explanation

### `insertCoin(...)` and `selectItem(...)`

* Blocked in this state because we're already processing.

### `dispenseItem(...)`

* Gets the selected item.
* Checks inventory again (just in case).
* Dispenses item.
* Deducts balance.
* Returns any remaining change.
* Calls `machine.reset()` to:

    * Reset balance to `0`
    * Clear selected item
    * Switch back to `IdleState`

---

## 🔁 `reset()` in `VendingMachine`

Just as a reminder:

```java
public void reset() {
    this.currentBalance = 0;
    this.selectedItem = null;
    this.setState(new IdleState());
}
```

This ensures that the machine is ready for the next user.

---

## 🎉 Your State Pattern Setup is Complete!

| State             | Description                    |
| ----------------- | ------------------------------ |
| `IdleState`       | Accepts coins                  |
| `HasMoneyState`   | Accepts coins + item selection |
| `DispensingState` | Finalizes transaction          |

---

## 🧪 Sample Usage

```java
public class Main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        VendingMachine machine = new VendingMachine(inventory);

        machine.refillItem(Item.COKE, 2);

        machine.insertCoin(Coin.TWENTY);
        machine.insertCoin(Coin.TEN);
        machine.selectItem(Item.COKE); // Costs ₹25
    }
}
```

---

## 🧠 Benefits of State Pattern Here

✅ Eliminates condition-heavy logic
✅ Clean separation of behavior per state
✅ Easy to extend (e.g., add `OutOfServiceState`, `MaintenanceState`)
✅ Each state is testable independently

---

Would you like me to:

* Bundle all state classes and `VendingMachine` in one Java file for easy use?
* Or move to multithreading or concurrency-safe version?

Let me know how you'd like to proceed 🚀
