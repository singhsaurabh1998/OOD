import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// //////////////////////////////////////////

public interface InventoryStore {
    void addProduct(Product prod, int qty);
    void removeProduct(int sku, int qty);
    int checkStock(int sku);
    List<Product> listAvailableProducts();
}
