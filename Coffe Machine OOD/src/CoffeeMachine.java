import java.util.HashMap;
import java.util.Map;

enum Ingredient {
    WATER, SUGAR, COFFEE_BEANS, MILK
}

class Inventory {
    HashMap<Ingredient, Integer> stock;

    public Inventory() {
        stock = new HashMap<>();
        for (Ingredient Ingredient : Ingredient.values()) {
            stock.put(Ingredient, 0);
        }
    }

    public void addIngredient(Ingredient ingredient, int qty) {
        stock.put(ingredient, stock.getOrDefault(ingredient, 0) + qty);
        System.out.println("Stock for " + ingredient.toString() + " has been updated !!");
    }

    // Check if ingredients are available
    public boolean hasEnoughIngredient(Map<Ingredient, Integer> required) {
        for (Ingredient ingredient : required.keySet()) {
            int needqty = required.get(ingredient);
            int curr = stock.get(ingredient);
            if (curr < needqty)
                return false;
        }
        return true;
    }

    public void useIngredient(Map<Ingredient, Integer> required) {
        for (Ingredient ingredient : required.keySet()) {
            int needqty = required.get(ingredient);
            int curr = stock.get(ingredient);
            stock.put(ingredient, curr - needqty);//update
        }
    }

    public void printStock() {
        System.out.println("Your Current stock is :");
        for (Ingredient ingredient : stock.keySet()) {
            System.out.println(ingredient.toString() + " : " + stock.get(ingredient));
        }
    }

}

interface BeverageFactory {
    Beverage createBeverageObj(String beverageName);
}

class SimpleBeverageFactory implements BeverageFactory {

    @Override
    public Beverage createBeverageObj(String beverageName) {
        switch (beverageName) {
            case "Espresso":
                return new Espresso();
            case "Latte":
                return new Latte();
            case "Cappuccino":
                return new Cappuccino();
            default:
                throw new IllegalArgumentException("Beverage Not Found !!");
        }
    }
}

interface Beverage {
    String getName();

    Map<Ingredient, Integer> getRequiredIngredients();
}

class Espresso implements Beverage {

    @Override
    public String getName() {
        return "Espresso ";
    }

    @Override
    public Map<Ingredient, Integer> getRequiredIngredients() {
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        ingredients.put(Ingredient.WATER, 100);
        ingredients.put(Ingredient.COFFEE_BEANS, 18);
        return ingredients;
    }
}

class Latte implements Beverage {
    @Override
    public String getName() {
        return "Coffee Latte ";
    }

    @Override
    public Map<Ingredient, Integer> getRequiredIngredients() {
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        ingredients.put(Ingredient.WATER, 100);
        ingredients.put(Ingredient.COFFEE_BEANS, 50);
        ingredients.put(Ingredient.MILK, 10);
        return ingredients;
    }
}

class Cappuccino implements Beverage {
    @Override
    public String getName() {
        return "Cappuccino ";
    }

    @Override
    public Map<Ingredient, Integer> getRequiredIngredients() {
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        ingredients.put(Ingredient.WATER, 200);
        ingredients.put(Ingredient.COFFEE_BEANS, 50);
        ingredients.put(Ingredient.MILK, 10);
        ingredients.put(Ingredient.SUGAR, 10);
        return ingredients;
    }
}

class CoffeeMachineService {
    private final Inventory inventory;
    BeverageFactory beverageFactory;

    public CoffeeMachineService(Inventory inventory, BeverageFactory beverageFactory) {
        this.inventory = inventory;
        this.beverageFactory = beverageFactory;
    }

    public void prepareBeverage(String beverageName) {
        Beverage beverage = beverageFactory.createBeverageObj(beverageName);
        if (!inventory.hasEnoughIngredient(beverage.getRequiredIngredients())) {
            System.out.println("❌ Cannot prepare " + beverage.getName() + ": Not enough ingredients.");
            return;
        }
        System.out.println("Lets prepare : " + beverage.getName());
        inventory.useIngredient(beverage.getRequiredIngredients());
        System.out.println(beverage.getName() + " is ready !!");
    }

    public void refillIngredient(Ingredient ingredient, int qty) {
        inventory.addIngredient(ingredient, qty);
    }

    public void showInventory() {
        inventory.printStock();
    }
}

public class CoffeeMachine {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        SimpleBeverageFactory simpleBeverageFactory = new SimpleBeverageFactory();
        CoffeeMachineService service = new CoffeeMachineService(inventory, simpleBeverageFactory);

        service.refillIngredient(Ingredient.WATER, 500);
        service.refillIngredient(Ingredient.MILK, 300);
        service.refillIngredient(Ingredient.COFFEE_BEANS, 100);
        service.refillIngredient(Ingredient.SUGAR, 100);

        service.showInventory();

        service.prepareBeverage("Espresso");
        service.prepareBeverage("Cappuccino");
        service.showInventory();
    }
}