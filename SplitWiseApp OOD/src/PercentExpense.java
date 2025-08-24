import java.util.List;

public class PercentExpense extends Expense {
    public PercentExpense(User paidBy, double amount, List<Split> splits, String description) {
        super(paidBy, amount, splits, description);
    }

    @Override
    public boolean validate() {
        double totalPercent = 0;
        for (Split s : splits) totalPercent += s.getAmount();  // here, amount is used as percent
        return Math.abs(totalPercent - 100) < 0.01;
    }
}
