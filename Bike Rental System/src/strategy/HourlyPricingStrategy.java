package strategy;

import model.Rental;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Simple hourly pricing strategy
 */
public class HourlyPricingStrategy implements PricingStrategy {
    private static final double RATE_PER_HOUR = 50.0;

    @Override
    public double calculatePrice(Rental rental) {
        Duration duration = Duration.between(rental.getStartTime(), LocalDateTime.now());
        long hours = Math.max(1, duration.toHours());
        return RATE_PER_HOUR * hours;
    }
}

