import java.util.*;

// Class to represent a food item with name and price
class FoodItem {
    String name;
    int price;

    public FoodItem(String name, int price) {
        this.name = name;
        this.price = price;
    }
}

// Class to represent a restaurant
class Restaurant {
    int rating;
    String name;
    int capacity;
    Map<String, FoodItem> menu = new HashMap<>();//itemname->FoodItem
    int currentOrders = 0;//curr orders
    List<String> orderHistory = new ArrayList<>();

    public Restaurant(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    // Add or update a menu item
    public void updateItem(String itemName, int price) {
        menu.put(itemName, new FoodItem(itemName, price));
    }

    public boolean canAcceptOrder(List<String> items) {
        if (currentOrders + items.size() > capacity) return false;
        for (String item : items) {
            if (!menu.containsKey(item))
                return false;
        }
        return true;
    }

    public int calculateTotal(List<String> items) {
        int total = 0;
        for (String item : items) {
            total += menu.get(item).price;
        }
        return total;
    }

    public void placeOrder(String orderId, int itemCount) {
        currentOrders += itemCount;
        orderHistory.add(orderId);
    }

    public void dispatchOrder(String orderId, int itemCount) {
        currentOrders -= itemCount;
        orderHistory.remove(orderId);
        System.out.println("Order dispatched: " + orderId);
    }

    public void printMenu() {
        for (FoodItem item : menu.values()) {
            System.out.println(item.name + " => " + item.price);
        }
    }
}

// Order class
class Order {
    String orderId;
    String restaurantName;
    List<String> items;//multiple items per order

    public Order(String orderId, String restaurantName, List<String> items) {
        this.orderId = orderId;
        this.restaurantName = restaurantName;
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", items=" + items +
                '}';
    }
}

// Strategy Design
interface SelectionStrategy {
    Restaurant selectRestaurant(List<Restaurant> restaurants, List<String> items);
}

class HighestRatingSelectionStrategy implements SelectionStrategy {

    @Override
    public Restaurant selectRestaurant(List<Restaurant> restaurants, List<String> items) {
        Restaurant restaurant = null;
        int highestRating = 0;
        for (Restaurant r : restaurants) {
            if (r.canAcceptOrder(items)) {
                if (r.rating > highestRating) {
                    highestRating = r.rating;
                    restaurant = r;
                }
            }
        }
        return restaurant;
    }
}

// Strategy: Select restaurant offering items at lowest total price
class LowestPriceSelectionStrategy implements SelectionStrategy {
    public Restaurant selectRestaurant(List<Restaurant> restaurants, List<String> items) { //faster lookups use PQ
        Restaurant best = null;
        int lowestTotal = Integer.MAX_VALUE;

        for (Restaurant r : restaurants) {
            if (r.canAcceptOrder(items)) {
                int total = r.calculateTotal(items);
                if (total < lowestTotal) {
                    lowestTotal = total;
                    best = r;
                }
            }
        }

        return best;
    }
}

class RestaurantService {
    Map<String, Restaurant> restaurantMap = new HashMap<>();//RestorauntName,restrant

    public void onboardRestaurant(String restorantName, int capacity, Map<String, Integer> menuItems) {
        Restaurant r = new Restaurant(restorantName, capacity);
        for (String item : menuItems.keySet()) {
            r.updateItem(item, menuItems.get(item));
        }
        restaurantMap.put(restorantName, r);
    }

    public void updatePrice(String restaurantName, String item, int price) {
        Restaurant r = restaurantMap.get(restaurantName);
        if (r != null) {
            r.updateItem(item, price);
            System.out.println("Updated Menu for " + restaurantName);
            r.printMenu();
        } else
            System.out.println("Restaurant is not available");
    }

    public Restaurant getRestaurant(String name) {
        return restaurantMap.get(name);
    }

    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurantMap.values());
    }
}

// Service for managing orders
class OrderService {
    Map<String, Order> orderMap = new HashMap<>();
    List<Order> dispatchedOrders = new ArrayList<>();
    SelectionStrategy selectionStrategy;
    RestaurantService restaurantService;

    public OrderService(RestaurantService restaurantService, SelectionStrategy strategy) {
        this.restaurantService = restaurantService;
        this.selectionStrategy = strategy;
    }

    public void placeOrder(String orderId, List<String> items) {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        Restaurant selected = selectionStrategy.selectRestaurant(restaurants, items);

        if (selected == null) {
            System.out.println("No suitable restaurant found for " + orderId);
            return;
        }

        selected.placeOrder(orderId, items.size());
        orderMap.put(orderId, new Order(orderId, selected.name, items));//store in order map also
        int totalPaisa = selected.calculateTotal(items);
        System.out.println("Order placed: " + orderId + " => Rs. " + totalPaisa + " at " + selected.name);
    }

    public void dispatchOrder(String orderId) {
        Order order = orderMap.get(orderId);
        if (order != null) {
            Restaurant r = restaurantService.getRestaurant(order.restaurantName);
            if (r != null) {
                r.dispatchOrder(orderId, order.items.size());
                //addition functionality
                dispatchedOrders.add(order); //order dispatched
            }
        } else
            System.out.println("Can't place since the " + orderId + " did not placed");
    }
}

// Command Processor
class CommandProcessor {
    static class Command {
        int timestamp;
        String[] tokens;

        public Command(int timestamp, String[] tokens) {
            this.timestamp = timestamp;
            this.tokens = tokens;
        }
    }

    List<Command> commands = new ArrayList<>();
    RestaurantService restaurantService = new RestaurantService();
    OrderService orderService = new OrderService(restaurantService, new LowestPriceSelectionStrategy());//strategy selection

    public void addCommand(String input) {
        String[] parts = input.split(", ");
        int time = Integer.parseInt(parts[0]);//timestamp
        String[] tokens = new String[parts.length - 1];
        for (int i = 1; i < parts.length; i++) {
            tokens[i - 1] = parts[i];
        }
        commands.add(new Command(time, tokens));
    }

    public void executeCommands() {
        Collections.sort(commands, new Comparator<Command>() {
            public int compare(Command a, Command b) {
                return a.timestamp - b.timestamp;
            }
        });

        for (Command cmd : commands) {
            String action = cmd.tokens[0];
            if (action.equals("update-price")) {
                restaurantService.updatePrice(cmd.tokens[1], cmd.tokens[2], Integer.parseInt(cmd.tokens[3]));
            } else if (action.equals("place-order")) {
                String orderId = cmd.tokens[1];
                List<String> items = new ArrayList<>();
                for (int i = 2; i < cmd.tokens.length; i++) {
                    items.add(cmd.tokens[i]);//items
                }
                orderService.placeOrder(orderId, items);
            } else if (action.equals("dispatch-order")) {
                orderService.dispatchOrder(cmd.tokens[1]);
            }
        }
    }

    void printAllDispatchedOrder() {
        List<Order> list = orderService.dispatchedOrders;
        System.out.println("Dispatched Orders : ");
        for (Order order : list) {
            System.out.println(order);
        }
    }
}

// Driver class
public class FoodKartApp {
    public static void main(String[] args) {
        CommandProcessor processor = new CommandProcessor();

        // Onboard restaurants manually
        Map<String, Integer> menu1 = new HashMap<>();
        menu1.put("idly", 100);
        menu1.put("dosa", 150);
        processor.restaurantService.onboardRestaurant("restaurant1", 5, menu1);

        Map<String, Integer> menu2 = new HashMap<>();
        menu2.put("chicken", 80);
        menu2.put("dosa", 130);
        menu2.put("dal", 120);
        menu2.put("chawal", 90);
        processor.restaurantService.onboardRestaurant("restaurant2", 5, menu2);

        processor.addCommand("5, place-order, order2, dal, chawal, chicken");
        processor.addCommand("1, place-order, order5, dal, chawal, chicken");
        processor.addCommand("2, update-price, restaurant2, chicken, 50");
        processor.addCommand("8, dispatch-order, order2");
        processor.addCommand("4, dispatch-order, order8");


        processor.executeCommands();
        processor.printAllDispatchedOrder();
    }
}
