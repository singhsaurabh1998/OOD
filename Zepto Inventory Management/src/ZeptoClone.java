public class ZeptoClone {
    public static void main(String[] args) {
        // 1) Initialize.
        ZeptoHelper.initialize();

        // 2) A User comes on Platform
        User user = new User("Aditya", 1.0, 1.0);
        System.out.println("\nUser with name " + user.name + " comes on platform");

        // 3) Show all available items via Zepto
        ZeptoHelper.showAllItems(user);

        // 4) User adds items to cart
        System.out.println("\nAdding items to cart");
        Cart cart = user.getCart();
        cart.addItem(101, 4);
        cart.addItem(102, 3);
        cart.addItem(103, 2);

        // 5) Place Order
        OrderManager.getInstance().placeOrder(user, cart);

        System.out.println("\n=== Demo Complete ===");
    }
}
