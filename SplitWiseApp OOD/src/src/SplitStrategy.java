import java.util.List;

public interface SplitStrategy {
    List<Split> calculateSplit(double totalAmount, List<String> userIds, List<Double> values);
}
