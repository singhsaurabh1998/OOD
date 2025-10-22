import java.util.ArrayList;
import java.util.List;

public class EqualSplit implements SplitStrategy {
    @Override
    public List<Split> calculateSplit(double totalAmount, List<String> userIds, List<Double> values) {
        List<Split> splits = new ArrayList<>();
        double amountPerUser = totalAmount / userIds.size();

        for (String userId : userIds) {
            splits.add(new Split(userId, amountPerUser));
        }
        return splits;
    }
}
