import java.util.ArrayList;
import java.util.List;

public class ExactSplitStrategy implements SplitStrategy {
    private List<Double> exactAmounts;

    public ExactSplitStrategy(List<Double> exactAmounts) {
        this.exactAmounts = exactAmounts;
    }

    @Override
    public List<Split> calculateSplits(User paidBy, double amount, List<User> participants) {
        List<Split> splits = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            splits.add(new Split(participants.get(i), exactAmounts.get(i)));
        }
        return splits;
    }
}
