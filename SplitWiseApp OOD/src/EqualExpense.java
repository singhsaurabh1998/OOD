import java.util.List;

public class EqualExpense extends Expense {
    public EqualExpense(User paidBy, double amount, List<Split> splits, String description) {
        super(paidBy, amount, splits, description);
    }

    @Override
    public boolean validate() {
        // Validate if splits are equal and sum == amount
        double eachShare = amount / splits.size();
        for (Split s : splits) {
            if (Math.abs(s.getAmount() - eachShare) > 0.01) return false;
        }
        return true;
    }
}
