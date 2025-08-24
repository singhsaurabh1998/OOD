import java.util.HashMap;
import java.util.Map;

public class BalanceSheet {
    private final Map<User, Double> balanceMap = new HashMap<>();

    public void update(User user, double amount) {
        balanceMap.put(user, balanceMap.getOrDefault(user, 0.0) + amount);
    }

    public void showBalances() {
        for (Map.Entry<User, Double> entry : balanceMap.entrySet()) {
            System.out.println(entry.getKey().getName() + " owes: " + entry.getValue());
        }
    }
}
