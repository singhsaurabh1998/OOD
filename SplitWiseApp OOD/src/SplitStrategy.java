import java.util.List;

public interface SplitStrategy {
    List<Split> calculateSplits(User paidBy, double amount, List<User> participants);

}
