import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Product {
    private final int sku;
    private final String name;
    private final double price;

    public Product(int id, String nm, double pr) {
        sku = id;
        name = nm;
        price = pr;
    }

    // Getters & Setters
    public int getSku() {
        return this.sku;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }
}
/////////////////////////////////////////////
// DarkStoreManager (Singleton)

