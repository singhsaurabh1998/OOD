import java.util.List;

public class ExactExpense extends Expense {
    public ExactExpense(User paidBy, double amount, List<Split> splits, String description) {
        super(paidBy, amount, splits, description);
    }

    @Override
    public boolean validate() {
        double total = 0;
        for (Split s : splits) total += s.getAmount();
        return Math.abs(total - amount) < 0.01;
    }
}
