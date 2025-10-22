//Target Interface (what client expects)
interface PaymentProcessor {
    void pay(double amount);
}

//Adaptee (third-party API, can’t change)
class StripeAPI {
    void makePayment(double usd) {
        System.out.println("USD payment done by stripe :" + usd);
    }
}

//Adaptee (third-party API, can’t change)
class PaypalAPI {
    void sendMoney(double inr) {
        System.out.println(inr + " INR has been paid by PayPal");
    }
}

//Adapters
class StripeAdapter implements PaymentProcessor {

    private final StripeAPI api;

    StripeAdapter(StripeAPI api) {
        this.api = api;
    }

    @Override
    public void pay(double amount) {
        api.makePayment(amount);
    }
}

//Adapters
class PayPalAdapter implements PaymentProcessor {

    private final PaypalAPI api;

    PayPalAdapter(PaypalAPI api) {
        this.api = api;
    }

    @Override
    public void pay(double amount) {
        api.sendMoney(amount);
    }
}

public class PaymentApp {
    public static void main(String[] args) {
        PaymentProcessor stripeProcessor = new StripeAdapter(new StripeAPI());
        PaymentProcessor paypalProcessor = new PayPalAdapter(new PaypalAPI());
        stripeProcessor.pay(40);
        paypalProcessor.pay(50);
    }
}
