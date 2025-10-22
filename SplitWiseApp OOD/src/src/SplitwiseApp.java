import java.util.Arrays;
import java.util.List;

public class SplitwiseApp {
    public static void main(String[] args) {
        Splitwise manager = Splitwise.getInstance();
        System.out.println("\n=========== Creating Users ====================");
        User user1 = manager.createUser("Aditya", "aditya@gmail.com");
        User user2 = manager.createUser("Rohit", "rohit@gmail.com");
        User user3 = manager.createUser("Manish", "manish@gmail.com");
        User user4 = manager.createUser("Saurabh", "saurav@gmail.com");

        System.out.println("\n=========== Creating Group and Adding Members ====================");
        Group hostelGroup = manager.createGroup("Hostel Expenses");
        manager.addUserToGroup(user1.userId, hostelGroup.groupId);
        manager.addUserToGroup(user2.userId, hostelGroup.groupId);
        manager.addUserToGroup(user3.userId, hostelGroup.groupId);
        manager.addUserToGroup(user4.userId, hostelGroup.groupId);

        System.out.println("\n=========== Adding Expenses in group ====================");
        List<String> groupMembers = Arrays.asList(user1.userId, user2.userId, user3.userId, user4.userId);
        manager.addExpenseToGroup(hostelGroup.groupId, "Lunch", 800.0, user1.userId, groupMembers, SplitType.EQUAL);

        List<String> dinnerMembers = Arrays.asList(user1.userId, user3.userId, user4.userId);
        List<Double> dinnerAmounts = Arrays.asList(200.0, 300.0, 200.0);
        manager.addExpenseToGroup(hostelGroup.groupId, "Dinner", 700.0, user3.userId, dinnerMembers,
                SplitType.EXACT, dinnerAmounts);

        System.out.println("\n=========== printing Group-Specific Balances ====================");
        manager.showGroupBalances(hostelGroup.groupId);

        System.out.println("\n=========== Debt Simplification ====================");
        manager.simplifyGroupDebts(hostelGroup.groupId);

        System.out.println("\n=========== printing Group-Specific Balances ====================");
        manager.showGroupBalances(hostelGroup.groupId);

        System.out.println("\n=========== Adding Individual Expense ====================");
        manager.addIndividualExpense("Coffee", 40.0, user2.userId, user4.userId, SplitType.EQUAL);

        System.out.println("\n=========== printing User Balances ====================");
        manager.showUserBalance(user1.userId);
        manager.showUserBalance(user2.userId);
        manager.showUserBalance(user3.userId);
        manager.showUserBalance(user4.userId);

        System.out.println("\n==========Attempting to remove Rohit from group==========");
        manager.removeUserFromGroup(user2.userId, hostelGroup.groupId);

        System.out.println("\n======== Making Settlement to Clear Rohit's Debt ==========");
        manager.settlePaymentInGroup(hostelGroup.groupId, user2.userId, user3.userId, 200.0);

        System.out.println("\n======== Attempting to Remove Rohit Again ==========");
        manager.removeUserFromGroup(user2.userId, hostelGroup.groupId);

        System.out.println("\n=========== Updated Group Balances ====================");
        manager.showGroupBalances(hostelGroup.groupId);
    }
}
