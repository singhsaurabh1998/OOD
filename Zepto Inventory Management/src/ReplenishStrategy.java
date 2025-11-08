import java.util.Map; /////////////////////////////////////////////

public interface ReplenishStrategy {
    //items kitni quantity se refill karni hai
    void replenish(InventoryManager manager, Map<Integer, Integer> itemsToReplenish);
}
