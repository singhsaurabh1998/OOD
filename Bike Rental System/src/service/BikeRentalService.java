package service;

import Observer.NotificationService;
import model.Customer;
import model.Rental;
import model.Vehicle;
import repository.RentalRepository;
import strategy.PricingStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * BikeRentalService - Handles rental business logic
 * Core service for managing rental operations
 */
public class BikeRentalService {
    private final InventoryManager inventoryManager;
    private final RentalRepository rentalRepo;
    private final PricingStrategy pricingStrategy;
    private NotificationService notificationService;

    public BikeRentalService(InventoryManager inventoryManager,
                             RentalRepository rentalRepo,
                             PricingStrategy pricingStrategy) {
        this.inventoryManager = inventoryManager;
        this.rentalRepo = rentalRepo;
        this.pricingStrategy = pricingStrategy;
    }

    // Setter for NotificationService
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Rent a vehicle to a customer and save rental record into repository
     */
    public Optional<Rental> rentVehicle(Customer customer, String vehicleId) {
        // Step 1: Try to allocate vehicle from inventory
        Optional<Vehicle> vehicleOpt = inventoryManager.allocateVehicle(vehicleId);


        if (vehicleOpt.isEmpty()) {
            System.out.println("Vehicle not available: " + vehicleId);
            return Optional.empty();
        }

        // Step 2: Create rental record
        Vehicle vehicle = vehicleOpt.get();
        Rental rental = new Rental(UUID.randomUUID().toString(), vehicle, customer);
        rentalRepo.save(rental);
        System.out.println("Rental started: " + rental.getRentalId() + " for vehicle: " + vehicleId);
        return Optional.of(rental);
    }

    public void returnVehicle(String rentalId) {
        Optional<Rental> rentalOpt = rentalRepo.findById(rentalId);

        if (rentalOpt.isEmpty()) {
            System.out.println("Rental not found: " + rentalId);
            return;
        }

        // Step 1: Complete the rental and calculate charges
        Rental rental = rentalOpt.get();
        double amount = pricingStrategy.calculatePrice(rental);
        rental.endRental(LocalDateTime.now(), amount);
        rentalRepo.save(rental);
        // Step 2: Release vehicle back to inventory
        inventoryManager.releaseVehicle(rental.getVehicle().getId());
        System.out.println("Rental completed: " + rentalId + " | Total amount: ₹" + rental.getAmount());
    }

    /**
     * Get all rentals
     */
    public List<Rental> getAllRentals() {
        return rentalRepo.getAll();
    }

    /**
     * Get rentals by customer
     */
    public List<Rental> getRentalsByCustomer(String customerId) {
        return rentalRepo.getAll().stream()
                .filter(r -> r.getCustomer().getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }
    public Optional<Rental> getCustomerByName(String name) {
        return rentalRepo.getAll().stream()
                .filter(c -> c.getCustomer().getName().equalsIgnoreCase(name))
                .findFirst();
    }
}

