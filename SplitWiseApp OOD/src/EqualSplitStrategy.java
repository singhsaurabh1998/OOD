import java.util.*;

public class EqualSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> calculateSplits(User paidBy, double amount, List<User> participants) {
        double share = amount / participants.size();
        List<Split> splits = new ArrayList<>();
        for (User u : participants) {
            splits.add(new Split(u, share));
        }
        return splits;
    }
}
