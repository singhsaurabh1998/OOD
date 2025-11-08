package facade;

import enums.BikeSize;
import enums.FuelType;
import factory.VehicleFactory;
import model.Customer;
import model.Rental;
import model.Vehicle;
import repository.InMemoryRentalRepository;
import repository.InMemoryVehicleRepository;
import repository.RentalRepository;
import repository.VehicleRepository;
import service.BikeRentalService;
import service.InventoryManager;
import strategy.PricingStrategy;

import java.util.List;
import java.util.Optional;

/**
 * RentalSystemFacade - Single unified entry point for the entire rental system
 * Coordinates between Inventory, Rental, and other subsystems
 * Implements the Facade Pattern to simplify client interactions
 */
public class RentalSystemFacade {
    private static RentalSystemFacade instance;
    private final VehicleRepository vehicleRepo;
    private final RentalRepository rentalRepo;
    private final InventoryManager inventoryManager;
    private final BikeRentalService rentalService;

    private RentalSystemFacade(PricingStrategy pricingStrategy) {
        // Initialize repositories
        this.vehicleRepo = new InMemoryVehicleRepository();
        this.rentalRepo = new InMemoryRentalRepository();

        // Initialize services
        this.inventoryManager = new InventoryManager(vehicleRepo);
        this.rentalService = new BikeRentalService(inventoryManager, rentalRepo, pricingStrategy);
    }

    public synchronized static RentalSystemFacade getInstance(PricingStrategy strategy) {
        if (instance == null) {
            instance = new RentalSystemFacade(strategy);
        }
        return instance;
    }
    // ==================== VEHICLE MANAGEMENT ====================

    /**
     * Add a new bike to inventory
     */
    public void addBike(String id, BikeSize size) {
        Vehicle bike = VehicleFactory.createBike(id, size);
        vehicleRepo.addVehicle(bike);
        System.out.println("Added bike: " + bike);
    }

    /**
     * Add a new scooter to inventory
     */
    public void addScooter(String id, FuelType fuelType) {
        Vehicle scooter = VehicleFactory.createScooter(id, fuelType);
        vehicleRepo.addVehicle(scooter);
        System.out.println("Added scooter: " + scooter);
    }

    /**
     * Get all available vehicles
     */
    public List<Vehicle> getAvailableVehicles() {
        return inventoryManager.getAvailableVehicles();
    }

    /**
     * Get all vehicles (available + rented)
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleRepo.getAll();
    }

    // ==================== RENTAL OPERATIONS ====================

    /**
     * Rent a vehicle - Coordinates inventory allocation and rental creation
     */
    public Optional<Rental> rentVehicle(Customer customer, String vehicleId) {
        return rentalService.rentVehicle(customer, vehicleId);
    }

    /**
     * Return a vehicle - Coordinates rental completion and inventory release
     */
    public void returnVehicle(String rentalId) {
        rentalService.returnVehicle(rentalId);

    }

    /**
     * Get all rentals
     */
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    //track customer
    public Optional<Rental> getCustomerByName(String name) {
        return rentalService.getCustomerByName(name);
    }
}


