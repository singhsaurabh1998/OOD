import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DigitalWalletService {
    private static DigitalWalletService instance;
    private final Map<String, User> users; // Key: userId => Value: User
    private final Map<String, Account> accounts; // Key: accountId => Value: Account
    private final Map<String, PaymentMethod> paymentMethods; // Key: paymentMethodId => Value: PaymentMethod

    private DigitalWalletService() {
        users = new HashMap<>();
        accounts = new HashMap<>();
        paymentMethods = new HashMap<>();
    }

    public static synchronized DigitalWalletService getInstance() {
        if (instance == null) {
            instance = new DigitalWalletService();
        }
        return instance;
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public void createAccount(Account account) {
        accounts.put(account.getId(), account);
        account.getUser().addAccount(account);// Link account to user

    }

    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    public void addPaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.put(paymentMethod.getId(), paymentMethod);
    }

    public PaymentMethod getPaymentMethod(String paymentMethodId) {
        return paymentMethods.get(paymentMethodId);
    }

    public synchronized void transferFunds(Account sourceAccount, Account destinationAccount, double amount, Currency currency) {
        if (sourceAccount.getCurrency() != currency) {
            amount = CurrencyConverter.convert(amount, currency, sourceAccount.getCurrency());
        }
        sourceAccount.withdraw(amount);

        if (destinationAccount.getCurrency() != sourceAccount.getCurrency()) {
            amount = CurrencyConverter.convert(amount, sourceAccount.getCurrency(), destinationAccount.getCurrency());
        }
        destinationAccount.deposit(amount);

        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(transactionId, sourceAccount, destinationAccount, amount, destinationAccount.getCurrency());
        sourceAccount.addTransaction(transaction);
        destinationAccount.addTransaction(transaction);
    }

    public List<Transaction> getTransactionHistory(Account account) {
        return account.getTransactions();
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
