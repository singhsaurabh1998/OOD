import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {
    public static int nextGroupId = 0;
    public String groupId;
    public String name;
    public List<User> members; //observers
    public Map<String, Expense> groupExpenses; // Group's own expense book
    public Map<String, Map<String, Double>> groupBalances; // memberId -> {otherMemberId -> balance}

    public Group(String name) {
        this.groupId = "group" + (++nextGroupId);
        this.name = name;
        this.members = new ArrayList<>();
        this.groupExpenses = new HashMap<>();
        this.groupBalances = new HashMap<>();
    }

    private User getUserByuserId(String userId) {
        User user = null;

        for (User member : members) {
            if (member.userId.equals(userId)) {
                user = member;
            }
        }
        return user;
    }

    public void addMember(User user) {
        members.add(user);

        // Initialize balance map for new member
        groupBalances.put(user.userId, new HashMap<>());
        System.out.println(user.name + " added to group " + name);
    }

    public void notifyMembers(String message) {
        for (Observer observer : members) {
            observer.update(message);
        }
    }

    public boolean isMember(String userId) {
        return groupBalances.containsKey(userId);
    }

    public boolean removeMember(String userId) {
        // Check if user can be removed or not
        if (!canUserLeaveGroup(userId)) {
            System.out.println("\nUser not allowed to leave group without clearing expenses");
            return false;
        }

        // Remove from observers
        members.removeIf(user -> user.userId.equals(userId));

        // Remove from group balances
        groupBalances.remove(userId);

        // Remove this user from other members' balance maps
        for (Map.Entry<String, Map<String, Double>> memberBalance : groupBalances.entrySet()) {
            memberBalance.getValue().remove(userId);
        }
        return true;
    }

    // Check if user can leave group.
    public boolean canUserLeaveGroup(String userId) {
        if (!isMember(userId)) {
            throw new RuntimeException("user is not a part of this group");
        }

        // Check if user has any outstanding balance with other group members
        Map<String, Double> userBalanceSheet = groupBalances.get(userId);
        for (Map.Entry<String, Double> balance : userBalanceSheet.entrySet()) {
            if (Math.abs(balance.getValue()) > 0.01) {
                return false; // Has outstanding balance
            }
        }
        return true;
    }

    // Add expense to this group
    public boolean addExpense(String description, double amount, String paidByUserId,
                              List<String> involvedUsers, SplitType splitType,
                              List<Double> splitValues) {

        if (!isMember(paidByUserId)) {
            throw new RuntimeException("user is not a part of this group");
        }

        // Validate that all involved users are group members
        for (String userId : involvedUsers) {
            if (!isMember(userId)) {
                throw new RuntimeException("involvedUsers are not a part of this group");
            }
        }

        // Generate splits using strategy pattern
        SplitStrategy strategy = SplitFactory.getSplitStrategy(splitType);//get the strategy based on the split type
        List<Split> splits = strategy.calculateSplit(amount, involvedUsers, splitValues);

        // Create expense in group's own expense book
        Expense expense = new Expense(description, amount, paidByUserId, splits, groupId);
        groupExpenses.put(expense.expenseId, expense);

        // Update group balances
        for (Split split : splits) {
            if (!split.userId.equals(paidByUserId)) {
                // Person who paid gets positive balance, person who owes gets negative
                updateGroupBalance(paidByUserId, split.userId, split.amount);
            }
        }

        System.out.println("\n=========== Sending Notifications ====================");
        String paidByName = getUserByuserId(paidByUserId).name;
        notifyMembers("New expense added: " + description + " (Rs " + amount + ")");

        // Printing console message-------
        System.out.println("\n=========== Expense Message ====================");
        System.out.println("Expense added to " + name + ": " + description + " (Rs " + amount
                + ") paid by " + paidByName + " and involved people are : ");
        if (!splitValues.isEmpty()) {
            for (int i = 0; i < splitValues.size(); i++) {
                System.out.println(getUserByuserId(involvedUsers.get(i)).name + " : " + splitValues.get(i));
            }
        } else {//default equal split
            for (String user : involvedUsers) {
                System.out.print(getUserByuserId(user).name + ", ");
            }
            System.out.println("\nWill be Paid Equally");
        }
        //-----------------------------------

        return true;
    }

    public boolean addExpense(String description, double amount, String paidByUserId,
                              List<String> involvedUsers, SplitType splitType) {
        return addExpense(description, amount, paidByUserId, involvedUsers, splitType, new ArrayList<>());
    }

    // Update balance within group
    public void updateGroupBalance(String fromUserId, String toUserId, double amount) {
        groupBalances.get(fromUserId).put(toUserId,
                groupBalances.get(fromUserId).getOrDefault(toUserId, 0.0) + amount);
        groupBalances.get(toUserId).put(fromUserId,
                groupBalances.get(toUserId).getOrDefault(fromUserId, 0.0) - amount);

        // Remove if balance becomes zero
        if (Math.abs(groupBalances.get(fromUserId).get(toUserId)) < 0.01) {
            groupBalances.get(fromUserId).remove(toUserId);
        }
        if (Math.abs(groupBalances.get(toUserId).get(fromUserId)) < 0.01) {
            groupBalances.get(toUserId).remove(fromUserId);
        }
    }

    public void settlePayment(String fromUserId, String toUserId, double amount) {
        // Validate that both users are group members
        if (!isMember(fromUserId) || !isMember(toUserId)) {
            System.out.println("user is not a part of this group");
            return;
        }

        // Update group balances
        updateGroupBalance(fromUserId, toUserId, amount);

        // Get usernames for display
        String fromName = getUserByuserId(fromUserId).name;
        String toName = getUserByuserId(toUserId).name;

        // Notify group members
        notifyMembers("Settlement: " + fromName + " paid " + toName + " Rs " + amount);

        System.out.println("Settlement in " + name + ": " + fromName + " settled Rs "
                + amount + " with " + toName);
    }

    public void showGroupBalances() {
        System.out.println("\n=== Group Balances for " + name + " ===");
        DecimalFormat df = new DecimalFormat("#.##");

        for (Map.Entry<String, Map<String, Double>> pair : groupBalances.entrySet()) {
            String memberId = pair.getKey();
            String memberName = getUserByuserId(memberId).name;

            System.out.println(memberName + "'s balances in group:");

            Map<String, Double> userBalances = pair.getValue();
            if (userBalances.isEmpty()) {
                System.out.println("  No outstanding balances");
            } else {
                for (Map.Entry<String, Double> userBalance : userBalances.entrySet()) {
                    String otherMemberUserId = userBalance.getKey();
                    String otherName = getUserByuserId(otherMemberUserId).name;

                    double balance = userBalance.getValue();
                    if (balance > 0) {
                        System.out.println("  " + otherName + " owes: Rs " + df.format(balance));
                    } else {
                        System.out.println("  Owes " + otherName + ": Rs " + df.format(Math.abs(balance)));
                    }
                }
            }
        }
    }

    public void simplifyGroupDebts() {
        Map<String, Map<String, Double>> simplifiedBalances = DebtSimplifier.simplifyDebts(groupBalances);
        groupBalances = simplifiedBalances;

        System.out.println("\nDebts have been simplified for group: " + name);
    }
}
