import java.util.Map; /////////////////////////////////////////////
// Replenishment Strategy (Strategy Pattern)

public class ThresholdReplenishStrategy implements ReplenishStrategy {
    private int threshold;

    public ThresholdReplenishStrategy(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void replenish(InventoryManager manager, Map<Integer, Integer> itemsToReplenish) {
        System.out.println("[ThresholdReplenish] Checking threshold...");
        for (Map.Entry<Integer, Integer> it : itemsToReplenish.entrySet()) {
            int sku = it.getKey();
            int qtyToAdd = it.getValue();
            int current = manager.checkStock(sku);
            if (current < threshold) {
                manager.addStock(sku, qtyToAdd);
                System.out.println("  -> SKU " + sku + " was " + current
                        + ", replenished by " + qtyToAdd);
            }
        }
    }
}
