import enums.BikeSize;
import enums.FuelType;
import facade.RentalSystemFacade;
import model.Customer;
import model.Rental;
import strategy.HourlyPricingStrategy;

import java.util.Optional;

/**
 * Bike Rental System (In-Memory Implementation)
 * ---------------------------------------------
 */
public class Main {
    public static void main(String[] args) {
        // Create facade
        RentalSystemFacade rentalSystem = RentalSystemFacade.getInstance(new HourlyPricingStrategy());

        // Add vehicles through facade
        rentalSystem.addBike("B1", BikeSize.SMALL);
        rentalSystem.addBike("B2", BikeSize.LARGE);
        rentalSystem.addScooter("S1", FuelType.ELECTRIC);
        rentalSystem.addScooter("S2", FuelType.PETROL);

        // Create a customer
        Customer saurabh = new Customer("C1", "Saurabh");

        // Show available vehicles
        System.out.println("Available Vehicles:");
        rentalSystem.getAvailableVehicles().forEach(System.out::println);

        // Rent vehicles
        Optional<Rental> rental1 = rentalSystem.rentVehicle(saurabh, "S1");
        //Optional<Rental> rental2 = rentalSystem.rentVehicle(saurabh, "S2");
        System.out.println("\nAfter Renting S1 and S2:"+rentalSystem.getCustomerByName(saurabh.getName()));
        // Show all rentals
        System.out.println("\nAll Rentals:");
        rentalSystem.getAllRentals().forEach(System.out::println);

        // Show remaining available vehicles
        System.out.println("\nRemaining Available Vehicles:");
        rentalSystem.getAvailableVehicles().forEach(System.out::println);
        rentalSystem.returnVehicle(rental1.get().getRentalId());

        System.out.println("\nAfter Return S1 and S2:"+rentalSystem.getCustomerByName(saurabh.getName()));

    }
}



