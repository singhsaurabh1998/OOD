public class PaymentRequest {
    private final int amount;
    private final String sender;
    private final String receiver;
    private final String currency;
    private final String idempotencyKey; //this is for checking duplicate transactions
    public PaymentRequest(int amount, String sender, String receiver, String currency,String idempotencyKey) {
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
        this.currency = currency;
        this.idempotencyKey = idempotencyKey;
    }

    public int getAmount() {
        return amount;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getCurrency() {
        return currency;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }
}
