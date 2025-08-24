import java.util.*;

public class SplitWiseApp {
    public static void main(String[] args) {
        // ✅ Step 1: Create Users
        User u1 = new User("U1", "Alice", "alice@mail.com");
        User u2 = new User("U2", "Bob", "bob@mail.com");
        User u3 = new User("U3", "Charlie", "charlie@mail.com");

        List<User> groupMembers = Arrays.asList(u1, u2, u3);

        // ✅ Step 2: Create Group
        SplitWiseService splitwise = SplitWiseService.getInstance();
        splitwise.createGroup("G1", "TripToGoa", groupMembers);

        // ✅ Step 3: Add Equal Expense
        System.out.println("\n🧾 Adding Equal Expense (Alice paid 3000 equally among all)");
        SplitStrategy equalStrategy = new EqualSplitStrategy();
        List<Split> equalSplits = equalStrategy.calculateSplits(u1, 3000, groupMembers);
        splitwise.addExpense("G1", ExpenseType.EQUAL, u1, 3000, equalSplits, "Hotel");

        // ✅ Step 4: Add Exact Expense
//        System.out.println("\n🧾 Adding Exact Expense (Bob paid 1500: Alice-500, Bob-500, Charlie-500)");
//        List<Double> exactAmounts = Arrays.asList(500.0, 500.0, 500.0);
//        SplitStrategy exactStrategy = new ExactSplitStrategy(exactAmounts);
//        List<Split> exactSplits = exactStrategy.calculateSplits(u2, 1500, groupMembers);
//        splitwise.addExpense("G1", ExpenseType.EXACT, u2, 1500, exactSplits, "Dinner");

        // ✅ Step 5: Add Percent Expense
//        System.out.println("\n🧾 Adding Percent Expense (Charlie paid 1200, Alice-50%, Bob-30%, Charlie-20%)");
//        List<Double> percents = Arrays.asList(50.0, 30.0, 20.0);
//        SplitStrategy percentStrategy = new PercentSplitStrategy(percents);
//        List<Split> percentSplits = percentStrategy.calculateSplits(u3, 1200, groupMembers);
//        splitwise.addExpense("G1", ExpenseType.PERCENT, u3, 1200, percentSplits, "Taxi");

        // ✅ Step 6: Show Final Balances
        System.out.println("\n📊 Final balances in group 'TripToGoa':");
        splitwise.showBalances("G1");
    }
}
