import java.util.List;
import java.util.UUID;

public abstract class Expense {
    protected String id;
    protected User paidBy;
    protected double amount;
    protected List<Split> splits;
    protected String description;

    public Expense(User paidBy, double amount, List<Split> splits, String description) {
        this.paidBy = paidBy;
        this.amount = amount;
        this.splits = splits;
        this.description = description;
        this.id = UUID.randomUUID().toString();  // unique ID for expense
    }

    public abstract boolean validate();

    public String getId() { return id; }
    public User getPaidBy() { return paidBy; }
    public double getAmount() { return amount; }
    public List<Split> getSplits() { return splits; }
    public String getDescription() { return description; }
}
