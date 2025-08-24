import java.util.Random;

public class RazorpayGateway implements PaymentStrategy {
    private final Random rng = new Random();
    @Override
    public String process(PaymentRequest request) {
        if (request.getAmount() <= 0) {
            System.out.println("[Razorpay] Invalid amount.");
            return null;
        }
        System.out.println("[Razorpay] Charging " + request.getCurrency() + " " + request.getAmount());
        boolean ok =  rng.nextInt(100) < 90;
        return ok ? "TRXN-RP" + System.currentTimeMillis() : null;

    }
    @Override
    public boolean refund(String transactionId, double amount) {
        System.out.println("[Paytm] Refunding ₹" + amount + " for txn " + transactionId);
        // simulate 95% success
        return rng.nextInt(100) < 95;
    }
}
