import java.util.ArrayList;
import java.util.List;

public class PercentSplitStrategy implements SplitStrategy {
    private List<Double> percents;

    public PercentSplitStrategy(List<Double> percents) {
        this.percents = percents;
    }

    @Override
    public List<Split> calculateSplits(User paidBy, double amount, List<User> participants) {
        List<Split> splits = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            double splitAmount = amount * percents.get(i) / 100.0;
            splits.add(new Split(participants.get(i), splitAmount));
        }
        return splits;
    }
}
