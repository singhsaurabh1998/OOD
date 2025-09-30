import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Account {
    private final String id;
    private final User user;
    private final String accountNumber;
    private final Currency currency;
    private double balance;
    private final List<Transaction> transactions;

    public Account(String id, User user, String accountNumber, Currency currency) {
        this.id = id;
        this.user = user;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }

    public synchronized void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
    }

    public synchronized void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance -= amount;
    }

    public synchronized void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    @Override
    public String toString() {
        return "Account{id='" + id + "', userId='" + user.getUserId() + "'}";
    }
}
