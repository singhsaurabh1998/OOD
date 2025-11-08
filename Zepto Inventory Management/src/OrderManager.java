import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManager {
    private static OrderManager instance;
    private final List<Order> orders;

    private OrderManager() {
        orders = new ArrayList<>();
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void placeOrder(User user, Cart cart) {
        System.out.println("\n[OrderManager] Placing Order for: " + user.name);

        List<Pair<Product, Integer>> requestedItems = cart.getItems();

        // 1) Find nearby dark stores within 5 KM
        double maxDist = 5.0;
        List<DarkStore> nearbyDarkStores =
                DarkStoreManager.getInstance().getNearbyDarkStores(user.x, user.y, maxDist);

        if (nearbyDarkStores.isEmpty()) {
            System.out.println("  No dark stores within 5 KM. Cannot fulfill order.");
            return;
        }

        // 2) Check if closest store has all items
        DarkStore firstStore = nearbyDarkStores.get(0);
        boolean allInFirst = true;
        for (Pair<Product, Integer> item : requestedItems) {
            int sku = item.getKey().getSku();
            int qty = item.getValue();
            if (firstStore.checkStock(sku) < qty) {
                allInFirst = false;
                break;
            }
        }

        Order order = new Order(user);

        // One delivery partner required...
        if (allInFirst) {
            System.out.println("  All items at: " + firstStore.getName());

            for (Pair<Product, Integer> item : requestedItems) {
                int sku = item.getKey().getSku();
                int qty = item.getValue();
                firstStore.removeStock(sku, qty);
                order.items.add(new Pair<>(item.getKey(), qty));
            }

            order.totalAmount = cart.getTotal();
            order.partners.add(new DeliveryPartner("Partner1"));
            System.out.println("  Assigned Delivery Partner: Partner1");
        }

        // Multiple delivery partners required
        else {
            System.out.println("  Splitting order across stores...");
            Map<Integer, Integer> allItems = new HashMap<>();
            for (Pair<Product, Integer> item : requestedItems) {
                allItems.put(item.getKey().getSku(), item.getValue());
            }

            int partnerId = 1;
            for (DarkStore store : nearbyDarkStores) {
                if (allItems.isEmpty()) break;
                System.out.println("   Checking: " + store.getName());
                List<Integer> toErase = new ArrayList<>();
                for (Map.Entry<Integer, Integer> entry : allItems.entrySet()) {
                    int sku = entry.getKey();
                    int qtyNeeded = entry.getValue();
                    int availableQty = store.checkStock(sku);
                    if (availableQty <= 0) continue;
                    int takenQty = Math.min(availableQty, qtyNeeded);
                    store.removeStock(sku, takenQty);
                    System.out.println("     " + store.getName() + " supplies SKU " + sku
                            + " x" + takenQty);
                    order.items.add(new Pair<>(ProductFactory.createProduct(sku), takenQty));
                    if (qtyNeeded > takenQty) {
                        allItems.put(sku, qtyNeeded - takenQty);
                    } else {
                        toErase.add(sku);
                    }
                }
                for (int sku : toErase) {
                    allItems.remove(sku);
                }
                if (!toErase.isEmpty()) {
                    String pname = "Partner" + partnerId++;
                    order.partners.add(new DeliveryPartner(pname));
                    System.out.println("     Assigned: " + pname + " for " + store.getName());
                }
            }
            if (!allItems.isEmpty()) {
                System.out.println("  Could not fulfill:");
                for (Map.Entry<Integer, Integer> entry : allItems.entrySet()) {
                    System.out.println("    SKU " + entry.getKey()
                            + " x" + entry.getValue());
                }
            }
            double sum = 0;
            for (Pair<Product, Integer> it : order.items) {
                sum += it.getKey().getPrice() * it.getValue();
            }
            order.totalAmount = sum;
        }

        // Printing Order Summary
        System.out.println("\n[OrderManager] Order #" + order.orderId + " Summary:");
        System.out.println("  User: " + user.name + "\n  Items:");
        for (Pair<Product, Integer> item : order.items) {
            System.out.println("    SKU " + item.getKey().getSku()
                    + " (" + item.getKey().getName() + ") x" + item.getValue()
                    + " @ ₹" + item.getKey().getPrice());
        }
        System.out.println("  Total: ₹" + order.totalAmount + "\n  Partners:");
        for (DeliveryPartner dp : order.partners) {
            System.out.println("    " + dp.name);
        }
        System.out.println();

        orders.add(order);
    }

    public List<Order> getAllOrders() {
        return orders;
    }
}
/////////////////////////////////////////////
// Zepto Initialization & Main

