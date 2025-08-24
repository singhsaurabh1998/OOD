/**
 * Try to charge `request.amount` from request.sender.
 * Returns true on success, false on failure.
 */
public interface PaymentStrategy {
    String process(PaymentRequest request);
    /**
     * Refunds (or cancels) a previously successful transaction.
     * @param transactionId the ID returned by `process()`
     * @param amount the amount to refund
     * @return true if the refund succeeded
     */
    boolean refund(String transactionId, double amount);
}
