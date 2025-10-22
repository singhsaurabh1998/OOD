import java.util.List;

/*
Generates a unique expenseId (auto-increment static counter).
Stores metadata: description, total amount, payer (paidByUserId), optional associated group (groupId).
Holds the computed list of Split objects that specify how the total is divided among participants (already calculated by a SplitStrategy; Expense does not compute splits itself).
Serves as a historical ledger entry: added to a group’s groupExpenses or the global individual expenses map.
Provides no business logic: settlement, balance updates, and simplification happen elsewhere (Group / Splitwise / DebtSimplifier).
 */
public class Expense {
    public static int nextExpenseId = 0;
    public String expenseId;
    public String description;
    public double totalAmount;
    public String paidByUserId;
    public List<Split> splits;
    public String groupId;

    // Constructor for group expenses
    public Expense(String desc, double amount, String paidBy,
                   List<Split> splits, String group) {
        this.expenseId = "expense" + (++nextExpenseId);
        this.description = desc;
        this.totalAmount = amount;
        this.paidByUserId = paidBy;
        this.splits = splits;
        this.groupId = group;
    }

    // Overloaded constructor for non-group expenses
    public Expense(String desc, double amount, String paidBy, List<Split> splits) {
        this(desc, amount, paidBy, splits, "");
    }
}
