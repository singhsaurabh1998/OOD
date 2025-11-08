import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/////////////////////////////////////////////

public class Order {
    private static int nextId = 1;
    public int orderId;
    public User user;
    // what items and their quantities
    public List<Pair<Product, Integer>> items = new ArrayList<>();
    // which delivery partners are assigned
    public List<DeliveryPartner> partners = new ArrayList<>();
    public double totalAmount;

    public Order(User u) {
        orderId = nextId++;
        user = u;
        totalAmount = 0.0;
    }
}
