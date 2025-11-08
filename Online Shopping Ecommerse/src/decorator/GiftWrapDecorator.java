package decorator;


import models.Product;

public class GiftWrapDecorator extends ProductDecorator {
    private static final double GIFT_WRAP_COST = 5.00;
    private final Product product;

    public GiftWrapDecorator(Product product) {
        super(product);
        this.product = product;
    }

    //price is decorated with gift wrap cost
    @Override
    public double getPrice() {
        return super.getPrice() + GIFT_WRAP_COST;
    }

    //description is decorated to indicate gift wrap
    @Override
    public String getDescription() {
        return super.getDescription() + " (Gift Wrapped Attached)";
    }
}
