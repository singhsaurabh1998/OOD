package strategy;

import model.Rental;

/**
 * PricingStrategy interface for dynamic pricing logic
 */
public interface PricingStrategy {
    double calculatePrice(Rental rental);
}

