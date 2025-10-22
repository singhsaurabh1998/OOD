// Forward declarations equivalent - not needed in Java due to automatic resolution

// Factory for split strategies
public class SplitFactory {
    public static SplitStrategy getSplitStrategy(SplitType type) {
        switch (type) {
            case EXACT:
                return new ExactSplit();
            case PERCENTAGE:
                return new PercentageSplit();
            default:
                return new EqualSplit();
        }
    }
}

