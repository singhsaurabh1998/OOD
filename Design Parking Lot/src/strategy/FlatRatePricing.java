package strategy;

import java.time.LocalDateTime;

public class FlatRatePricing implements IPricingStrategy {
    private final double flatRate;

    public FlatRatePricing(double flatRate) {
        this.flatRate = flatRate;
    }

    @Override
    public double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime) {
        return flatRate;
    }
}
