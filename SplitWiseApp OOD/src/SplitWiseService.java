
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SplitWiseService {
    private static final SplitWiseService instance = new SplitWiseService();
    private final Map<String, Group> groups = new HashMap<>();
    private Map<String, BalanceSheet> groupBalances = new ConcurrentHashMap<>();//thread safe

    private SplitWiseService() {}

    public static SplitWiseService getInstance() {
        return instance;
    }

    public void createGroup(String id, String name, List<User> members) {
        groups.put(id, new Group(id, name, members));
        groupBalances.put(id, new BalanceSheet());
    }

    public void addExpense(String groupId, ExpenseType type, User paidBy, double amount, List<Split> splits, String desc ){
        Group group = groups.get(groupId);
        if (group == null) {
            System.out.println("Group not found!");
            return;
        }

        Expense expense = ExpenseFactory.createExpense(type, paidBy, amount, splits, desc);
        if (!expense.validate()) {
            System.out.println("Invalid expense splits!");
            return;
        }

        group.addExpense(expense);
        BalanceSheet sheet = groupBalances.get(groupId);

        for (Split split : expense.getSplits()) {
            if (split.getUser() != paidBy) {
                sheet.update(split.getUser(), split.getAmount());
                sheet.update(paidBy, -split.getAmount());
            }
        }
    }

    public void showBalances(String groupId) {
        BalanceSheet sheet = groupBalances.get(groupId);
        if (sheet != null) sheet.showBalances();
    }
}
