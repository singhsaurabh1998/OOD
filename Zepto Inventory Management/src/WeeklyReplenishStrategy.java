import java.util.List;
import java.util.Map;

public class WeeklyReplenishStrategy implements ReplenishStrategy {
    public WeeklyReplenishStrategy() {
    }

    @Override
    public void replenish(InventoryManager manager, Map<Integer, Integer> itemsToReplenish) {
        System.out.println("[WeeklyReplenish] Weekly replenishment triggered for inventory.");
    }
}
/////////////////////////////////////////////
// DarkStore (formerly Warehouse)

