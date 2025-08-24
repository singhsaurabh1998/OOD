import java.util.HashMap;
import java.util.Map;

enum Coin {
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    HUNDRED(100);//just like initialise the object by calling constructor

    private int value;

    //Coin.HUNDRED.getValue() ->aise access kr skte
    Coin(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
}

enum Item {

    COKE("Coke", 25),
    PEPSI("Pepsi", 35),
    JUICE("Juice", 45),
    SNACKS("Snacks", 50);

    private String itemName;
    private int price;

    Item(String coke, int price) {
        this.itemName = coke;
        this.price = price;
    }

    public String getName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }
}

class Inventory {
    private final Map<Item, Integer> itemStock;//maps each item to its available count per item.

    public Inventory() {
        this.itemStock = new HashMap<>();//initialize here
        for (Item item : Item.values()) {
            itemStock.put(item, 0);
        }
    }

    public void addItem(Item item, int qty) {
        int currQty = itemStock.getOrDefault(item, 0);
        itemStock.put(item, currQty + qty);
        System.out.println("✔️ Added " + item.getName() + " by " + qty + " units");
    }

    public boolean dispenseItem(Item item) {
        int currQty = itemStock.getOrDefault(item, 0);
        if (currQty == 0)
            return false;
        itemStock.put(item, currQty - 1);// Reduce stock (if available)
        return true;
    }

    // Check if item is available
    public boolean isAvailable(Item item) {
        return itemStock.getOrDefault(item, 0) > 0;
    }

    // Print current stock
    public void printStock() {
        System.out.println("📦 Current Inventory:");
        for (Item item : itemStock.keySet()) {
            System.out.println("• " + item.getName() + ": " + itemStock.get(item) + " units");
        }
    }
}

interface VendingMachineState {
    void insertCoin(VendingMachine machine, Coin coin);

    void selectItem(VendingMachine machine, Item item);

    void dispenseItem(VendingMachine machine);
}

class IdleState implements VendingMachineState {
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

class HasMoneyState implements VendingMachineState {

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

 class DispensingState implements VendingMachineState {

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
        boolean success = machine.getInventory().dispenseItem(selected);
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

class VendingMachine {
    private VendingMachineState currentState;
    private final Inventory inventory;
    private int currentBalance;
    private Item selectedItem;

    public VendingMachine(Inventory inventory) {
        this.inventory = inventory;
        this.currentState = new IdleState(); // Starts in IDLE
        this.currentBalance = 0;
    }

    public void refillItem(Item item, int count) {
        inventory.addItem(item, count);
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

    public void showItems() {
        inventory.printStock();
    }

    public void reset() {
        this.currentBalance = 0;
        this.selectedItem = null;
        this.setState(new IdleState());
    }
}

public class VendingMachineDemo {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        VendingMachine machine = new VendingMachine(inventory);
        // Refill inventory
        machine.refillItem(Item.COKE, 3);
        machine.refillItem(Item.PEPSI, 2);
        machine.showItems();

        machine.insertCoin(Coin.HUNDRED); // ₹20
        machine.insertCoin(Coin.HUNDRED); // ₹20
        machine.insertCoin(Coin.HUNDRED); // ₹20
        machine.insertCoin(Coin.TEN);
        machine.selectItem(Item.COKE);

    }
}
