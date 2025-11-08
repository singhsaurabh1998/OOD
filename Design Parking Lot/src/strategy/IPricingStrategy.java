package strategy;

import java.time.LocalDateTime;

public interface IPricingStrategy {
    double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime);
}
