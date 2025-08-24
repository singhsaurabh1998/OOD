import java.util.ArrayList;
import java.util.List;

public class Group {
    private String id;
    private String name;
    private List<User> members;
    private List<Expense> expenses;

    public Group(String id, String name, List<User> members) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.expenses = new ArrayList<>();
    }

    public synchronized void addExpense(Expense expense) {
        expenses.add(expense);
        notifyAllObservers("New expense added: " + expense.getDescription() + ", Amount: " + expense.getAmount());
    }

    public void notifyAllObservers(String message) {
        for (User user : members) {
            user.notify(message);
        }
    }

    public List<User> getMembers() { return members; }
}
