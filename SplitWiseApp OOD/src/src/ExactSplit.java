import java.util.ArrayList;
import java.util.List;

public class ExactSplit implements SplitStrategy {
    @Override
    public List<Split> calculateSplit(double totalAmount, List<String> userIds, List<Double> values) {
        List<Split> splits = new ArrayList<>();
        //validations and null checks
        if (userIds == null || values == null || userIds.isEmpty() || values.isEmpty()) {
            throw new RuntimeException("User IDs and values cannot be null or empty.");
        }
        if (userIds.size() != values.size()) {
            throw new RuntimeException("User IDs and values must have the same length.");
        }
        double sum = 0;
        for (Double value : values) {
            if (value < 0) {
                throw new RuntimeException("Values cannot be negative.");
            }
            sum += value;
        }
        if (Math.abs(sum - totalAmount) > 0.01) {
            throw new RuntimeException("Sum of values must equal total amount.");
        }
        //create splits
        for (int i = 0; i < userIds.size(); i++) {
            splits.add(new Split(userIds.get(i), values.get(i)));
        }
        return splits;
    }
}
