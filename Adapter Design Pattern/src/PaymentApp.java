// Target interface
interface PaymentGateway {
    void pay(String amount);
}

// Adaptee class (Third-party or legacy)
class PayPalPayment {
    public void makePayment(String amountInDollars) {
        System.out.println("Paid $" + amountInDollars + " using PayPal.");
    }
}

// Adapter class
class PayPalAdapter implements PaymentGateway {
    private final PayPalPayment payPalPayment;

    public PayPalAdapter(PayPalPayment payPalPayment) {
        this.payPalPayment = payPalPayment;
    }

    @Override
    public void pay(String amount) {
        // Adapter translates the interface
        payPalPayment.makePayment(amount);
    }
}

// Client code
public class PaymentApp {
    public static void main(String[] args) {
        // Client expects to work with PaymentGateway
        PaymentGateway paymentGateway = new PayPalAdapter(new PayPalPayment());

        // Works smoothly with PayPal using adapter
        paymentGateway.pay("100");
    }
}
