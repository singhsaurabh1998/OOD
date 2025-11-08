import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// //////////////////////////////////////////

public class ZeptoHelper {
    public static void showAllItems(User user) {
        System.out.println("\n[Zepto] All Available products within 5 KM for " + user.name + ":");
        DarkStoreManager dsManager = DarkStoreManager.getInstance();
        List<DarkStore> nearbyStores = dsManager.getNearbyDarkStores(user.x, user.y, 5.0);
        Map<Integer, Double> skuToPrice = new HashMap<>();
        Map<Integer, String> skuToName = new HashMap<>();

        for (DarkStore ds : nearbyStores) {
            for (Product product : ds.getAllProducts()) {
                int sku = product.getSku();
                if (!skuToPrice.containsKey(sku)) {
                    skuToPrice.put(sku, product.getPrice());
                    skuToName.put(sku, product.getName());
                }
            }
        }

        for (Map.Entry<Integer, Double> entry : skuToPrice.entrySet()) {
            System.out.println("  SKU " + entry.getKey() + " - "
                    + skuToName.get(entry.getKey())
                    + " @ ₹" + entry.getValue());
        }
    }

    public static void initialize() {
        DarkStoreManager dsManager = DarkStoreManager.getInstance();

        // DarkStore A.......
        DarkStore darkStoreA = new DarkStore("DarkStoreA", 0.0, 0.0);
        darkStoreA.setReplenishStrategy(new ThresholdReplenishStrategy(3));
        System.out.println("\nAdding stocks in DarkStoreA....");
        darkStoreA.addStock(101, 5);
        darkStoreA.addStock(102, 2);

        // DarkStore B.......
        DarkStore darkStoreB = new DarkStore("DarkStoreB", 4.0, 1.0);
        darkStoreB.setReplenishStrategy(new ThresholdReplenishStrategy(3));
        System.out.println("\nAdding stocks in DarkStoreB....");
        darkStoreB.addStock(101, 3);
        darkStoreB.addStock(103, 10);

        // DarkStore C.......
        DarkStore darkStoreC = new DarkStore("DarkStoreC", 2.0, 3.0);
        darkStoreC.setReplenishStrategy(new ThresholdReplenishStrategy(3));
        System.out.println("\nAdding stocks in DarkStoreC....");
        darkStoreC.addStock(102, 5);
        darkStoreC.addStock(201, 7);

        dsManager.registerDarkStore(darkStoreA);
        dsManager.registerDarkStore(darkStoreB);
        dsManager.registerDarkStore(darkStoreC);
    }
}
