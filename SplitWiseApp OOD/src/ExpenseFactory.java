import java.util.List;

public class ExpenseFactory {
    public static Expense createExpense(ExpenseType type, User paidBy, double amount, List<Split> splits, String desc) {
        switch (type) {
            case EQUAL:
                return new EqualExpense(paidBy, amount, splits, desc);
            case EXACT:
                return new ExactExpense(paidBy, amount, splits, desc);
            case PERCENT:
                return new PercentExpense(paidBy, amount, splits, desc);
            default:
                throw new IllegalArgumentException("Invalid expense type");
        }
    }
}
