import java.util.Random;

public class PaytmGateway implements PaymentStrategy {
    private final Random rng = new Random();
//here we're mocking the real bank behavior
    @Override
    public String process(PaymentRequest request) {
        if (request.getAmount() <= 0 || !"INR".equals(request.getCurrency())) {
            System.out.println("[Paytm] Invalid currency or amount.");
            return null;
        }
        System.out.println("[Paytm] Charging ₹" + request.getAmount());

        boolean success = rng.nextInt(100)<80;
        return success?"TRXN-PayTm"+System.currentTimeMillis():null; //only 80 % success rate
    }
    @Override
    public boolean refund(String transactionId, double amount) {
        System.out.println("[Paytm] Refunding ₹" + amount + " for txn " + transactionId);
        // simulate 95% success
        return rng.nextInt(100) < 95;
    }
}
