import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Transaction {
    private final String id;
    private final Account sourceAccount;
    private final Account destinationAccount;
    private final double amount;
    private final Currency currency;
    private final LocalDateTime timestamp;

    public Transaction(String id, Account sourceAccount, Account destinationAccount, double amount, Currency currency) {
        this.id = id;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = LocalDateTime.now();
    }

}
