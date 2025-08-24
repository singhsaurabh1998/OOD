import java.util.HashMap;
import java.util.Map;

public class PaymentService {
    private final PaymentStrategy processor;
    private final int maxRetries;
    private final Map<String, PaymentRequest> transaction = new HashMap<>();//Map<txnId, PaymentRequest> for refunds
    private final Map<String, String> idempotencyLedger = new HashMap<>(); //Map<idempotencyKey, txnId> for retry protection

    /**
     * @param processor  the strategy to use
     * @param maxRetries how many times to retry on failure
     */
    public PaymentService(PaymentStrategy processor, int maxRetries) {
        this.processor = processor;
        this.maxRetries = maxRetries;
    }

    public String pay(PaymentRequest request) {
        if (idempotencyLedger.containsKey(request.getIdempotencyKey())) {
            System.out.println("Transaction has been already performed !");
            return idempotencyLedger.get(request.getIdempotencyKey());
        }
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            System.out.println("Attempt " + attempt + " of " + maxRetries);
            String txnId = processor.process(request);
            if (txnId != null) {
                System.out.println("✅ Payment successful on attempt " + attempt);
                transaction.put(txnId, request);
                idempotencyLedger.put(request.getIdempotencyKey(), txnId);
                return txnId;
            }
            System.out.println("❌ Payment failed on attempt " + attempt);
        }
        System.out.println("⚠️ All attempts failed for " + request.getSender());
        return null;
    }

    /**
     * Refunds a previously successful transaction.
     * Returns true on success, false otherwise.
     */
    public boolean refund(String transactionId) {
        PaymentRequest req = transaction.get(transactionId);
        if (req == null) {
            System.out.println("❌ No such transaction: " + transactionId);
            return false;
        }
        boolean ok = processor.refund(transactionId, req.getAmount());
        if (ok) {
            System.out.println("✅ Refund successful for txn " + transactionId + "Amount is : " + req.getAmount());
            transaction.remove(transactionId);
        } else {
            System.out.println("❌ Refund failed for txn " + transactionId);
        }
        return ok;
    }
}
