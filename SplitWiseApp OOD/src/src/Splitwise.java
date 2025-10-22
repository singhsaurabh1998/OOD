import java.text.DecimalFormat;
import java.util.*;

// Forward declarations equivalent - not needed in Java due to automatic resolution

// Main ExpenseManager class (Singleton - Facade)
public class Splitwise {
    private final Map<String, User> users;
    private final Map<String, Group> groups;
    private final Map<String, Expense> expenses;

    private static Splitwise instance;
    
    private Splitwise() {
        users = new HashMap<>();
        groups = new HashMap<>();
        expenses = new HashMap<>();
    }
    
    public static Splitwise getInstance() {
        if(instance == null) {
            instance = new Splitwise();
        }
        return instance;
    }

    // User management
    public User createUser(String name, String email) {
        User user = new User(name, email);
        users.put(user.userId, user);
        System.out.println("User created: " + name + " (ID: " + user.userId + ")");
        return user;
    }
    
    public User getUser(String userId) {
        return users.get(userId);
    }
    
    // Group management
    public Group createGroup(String name) {
        Group group = new Group(name);
        groups.put(group.groupId, group);
        System.out.println("Group created: " + name + " (ID: " + group.groupId + ")");
        return group;
    }
    
    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }
    
    public void addUserToGroup(String userId, String groupId) {
        User user = getUser(userId);
        Group group = getGroup(groupId);
        
        if (user != null && group != null) {
            group.addMember(user);
        }
    }
    
    // Try to remove user from group - just delegates to group
    public boolean removeUserFromGroup(String userId, String groupId) {
        Group group = getGroup(groupId);
        
        if (group == null) {
            System.out.println("Group not found!");
            return false;
        }
        
        User user = getUser(userId);
        if (user == null) {
            System.out.println("User not found!");
            return false;
        }

        boolean userRemoved = group.removeMember(userId);
        
        if(userRemoved) {
            System.out.println(user.name + " successfully left " + group.name);
        }
        return userRemoved;
    }
    
    // Expense management - delegate to group
    public void addExpenseToGroup(String groupId, String description, double amount, 
                          String paidByUserId, List<String> involvedUsers, 
                          SplitType splitType, List<Double> splitValues) {
        
        Group group = getGroup(groupId);
        if (group == null) {
            System.out.println("Group not found!");
            return;
        }
        
        group.addExpense(description, amount, paidByUserId, involvedUsers, splitType, splitValues);
    }
    
    public void addExpenseToGroup(String groupId, String description, double amount, 
                          String paidByUserId, List<String> involvedUsers, 
                          SplitType splitType) {
        addExpenseToGroup(groupId, description, amount, paidByUserId, involvedUsers, splitType, new ArrayList<>());
    }
    
    // Settlement - delegate to group
    public void settlePaymentInGroup(String groupId, String fromUserId, 
                              String toUserId, double amount) {
        
        Group group = getGroup(groupId);
        if (group == null) {
            System.out.println("Group not found!");
            return;
        }
        
        group.settlePayment(fromUserId, toUserId, amount);
    }
    
    // Settlement
    public void settleIndividualPayment(String fromUserId, String toUserId, double amount) {
        User fromUser = getUser(fromUserId);
        User toUser = getUser(toUserId);
        
        if (fromUser != null && toUser != null) {
            fromUser.updateBalance(toUserId, amount);
            toUser.updateBalance(fromUserId, -amount);
            
            System.out.println(fromUser.name + " settled Rs" + amount + " with " + toUser.name);
        }
    }
    
    public void addIndividualExpense(String description, double amount, String paidByUserId,
                             String toUserId, SplitType splitType,
                            List<Double> splitValues) {

        SplitStrategy strategy = SplitFactory.getSplitStrategy(splitType);
        List<Split> splits = strategy.calculateSplit(amount, Arrays.asList(paidByUserId, toUserId), splitValues);

        Expense expense = new Expense(description, amount, paidByUserId, splits);
        expenses.put(expense.expenseId, expense);
        
        User paidByUser = getUser(paidByUserId);
        User toUser = getUser(toUserId);

        paidByUser.updateBalance(toUserId, amount);
        toUser.updateBalance(paidByUserId, -amount);
        
        System.out.println("Individual expense added: " + description + " (Rs " + amount 
                + ") paid by " + paidByUser.name +" for " + toUser.name);
    }
    
    public void addIndividualExpense(String description, double amount, String paidByUserId,
                             String toUserId, SplitType splitType) {
        addIndividualExpense(description, amount, paidByUserId, toUserId, splitType, new ArrayList<>());
    }
    
    // Display Method
    public void showUserBalance(String userId) {
        User user = getUser(userId);
        if (user == null) return;
        
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("\n=========== Balance for " + user.name +" ===================="); 
        System.out.println("Total you owe: Rs " + df.format(user.getTotalOwed()));
        System.out.println("Total others owe you: Rs " + df.format(user.getTotalOwing()));
        
        System.out.println("Detailed balances:");
        for (Map.Entry<String, Double> balance : user.balances.entrySet()) {
            User otherUser = getUser(balance.getKey());
            if (otherUser != null) {
                if (balance.getValue() > 0) {
                    System.out.println("  " + otherUser.name + " owes you: Rs" + balance.getValue());
                } else {
                    System.out.println("  You owe " + otherUser.name + ": Rs" + Math.abs(balance.getValue()));
                }
            }
        }
    }
    
    public void showGroupBalances(String groupId) {
        Group group = getGroup(groupId);
        if (group == null) return;
        
        group.showGroupBalances();
    }
    
    public void simplifyGroupDebts(String groupId) {
        Group group = getGroup(groupId);
        if (group == null) return;
                
        // Use group's balance data for debt simplification
        group.simplifyGroupDebts();
    }
}

