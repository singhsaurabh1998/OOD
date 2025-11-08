package strategy;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class HourlyPricing implements IPricingStrategy {
    private final double baseFee;
    private final double hourlyRate;

    public HourlyPricing(double baseFee, double hourlyRate) {
        this.baseFee = baseFee;
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double calculateFee(LocalDateTime entry, LocalDateTime exit) {
        long hours = ChronoUnit.HOURS.between(entry, exit);
        if (hours == 0) hours = 1; // minimum 1 hour
        return baseFee + (hours * hourlyRate);
    }
}