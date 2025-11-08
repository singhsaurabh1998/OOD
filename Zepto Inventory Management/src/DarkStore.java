import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DarkStore {
    private final String name;
    private final double x;
    private final double y;                       // location coordinates
    private final InventoryManager inventoryManager;
    private ReplenishStrategy replenishStrategy;

    public DarkStore(String n, double x_coord, double y_coord) {
        name = n;
        x = x_coord;
        y = y_coord;

        // We could have made another factory called InventoryStoreFactory to get
        // DbInventoryStore by enum and hence make it loosely coupled.
        inventoryManager = new InventoryManager(new DbInventoryStore());
    }

    public double distanceTo(double ux, double uy) {
        return Math.sqrt((x - ux) * (x - ux) + (y - uy) * (y - uy));
    }

    public void runReplenishment(Map<Integer, Integer> itemsToReplenish) {
        if (replenishStrategy != null) {
            replenishStrategy.replenish(inventoryManager, itemsToReplenish);
        }
    }

    // Delegation Methods
    public List<Product> getAllProducts() {
        return inventoryManager.getAvailableProducts();
    }

    public int checkStock(int sku) {
        return inventoryManager.checkStock(sku);
    }

    public void removeStock(int sku, int qty) {
        inventoryManager.removeStock(sku, qty);
    }

    public void addStock(int sku, int qty) {
        inventoryManager.addStock(sku, qty);
    }

    // Getters & Setters
    public void setReplenishStrategy(ReplenishStrategy strategy) {
        this.replenishStrategy = strategy;
    }

    public String getName() {
        return this.name;
    }

    public double getXCoordinate() {
        return this.x;
    }

    public double getYCoordinate() {
        return this.y;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }
}

