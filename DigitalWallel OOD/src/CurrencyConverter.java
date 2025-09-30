import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CurrencyConverter {
    private static final Map<Currency, Double> exchangeRates = new HashMap<>();

    static {
        // Initialize exchange rates
        exchangeRates.put(Currency.USD, 1.0);
        exchangeRates.put(Currency.EUR, 0.85);
        exchangeRates.put(Currency.GBP, 0.72);
        exchangeRates.put(Currency.JPY, 110.00);
        // Add more exchange rates as needed
    }

    public static double convert(double amount, Currency sourceCurrency, Currency targetCurrency) {
        Double sourceRate = exchangeRates.get(sourceCurrency);
        Double targetRate = exchangeRates.get(targetCurrency);
        if (sourceRate == null || targetRate == null) {
            throw new IllegalArgumentException("Invalid currency");
        }
        return amount * sourceRate / targetRate;
    }

    public List<Transaction> getTransactionHistory(Account account) {
        return account.getTransactions();
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
