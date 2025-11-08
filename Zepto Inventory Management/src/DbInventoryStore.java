import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbInventoryStore implements InventoryStore {
    private final Map<Integer, Integer> stock;         // SKU -> quantity
    private final Map<Integer, Product> products;      // SKU -> Product

    public DbInventoryStore() {
        stock = new HashMap<>();
        products = new HashMap<>();
    }

    @Override
    public void addProduct(Product prod, int qty) {
        int sku = prod.getSku();
        if (!products.containsKey(sku)) {
            products.put(sku, prod);
        }
        // else drop the extra prod instance
        stock.put(sku, stock.getOrDefault(sku, 0) + qty);
    }

    @Override
    public void removeProduct(int sku, int qty) {
        if (!stock.containsKey(sku))
            return;

        int currentQuantity = stock.get(sku);
        int remainingQuantity = currentQuantity - qty;
        if (remainingQuantity > 0) {
            stock.put(sku, remainingQuantity);
        } else {
            stock.remove(sku);
            products.remove(sku);
        }
    }

    @Override
    public int checkStock(int sku) {
        return stock.getOrDefault(sku, 0);
    }

    @Override
    public List<Product> listAvailableProducts() {
        List<Product> available = new ArrayList<>();
        for (Map.Entry<Integer, Integer> it : stock.entrySet()) {
            int sku = it.getKey();
            int qty = it.getValue();
            if (qty > 0 && products.containsKey(sku)) {
                available.add(products.get(sku));
            }
        }
        return available;
    }
}
/// //////////////////////////////////////////
// InventoryManager

