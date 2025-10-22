import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Bike Rental System (In-Memory Implementation)
 * ---------------------------------------------
 * - Demonstrates Factory, Strategy, and Facade Patterns
 * - Follows SOLID principles
 * - Simple CLI-style main() simulation
 */

// ENUMS
enum VehicleCategory {BIKE, SCOOTER}

enum BikeSize {SMALL, MEDIUM, LARGE}

enum FuelType {PETROL, ELECTRIC}

enum RentalStatus {RESERVED, ONGOING, COMPLETED}

// -------------------- ENTITY LAYER -------------------- //

/**
 * Base Vehicle class — common attributes for all vehicles
 */
abstract class Vehicle {
    protected String id;
    protected VehicleCategory category;
    protected boolean available = true;

    public Vehicle(String id, VehicleCategory category) {
        this.id = id;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public VehicleCategory getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void markRented() {
        available = false;
    }

    public void markAvailable() {
        available = true;
    }

    @Override
    public String toString() {
        return "Vehicle{" + "id='" + id + '\'' + ", category=" + category + ", available=" + available + '}';
    }
}

/**
 * Bike subclass with size property
 */
class Bike extends Vehicle {
    private final BikeSize size;

    public Bike(String id, BikeSize size) {
        super(id, VehicleCategory.BIKE);
        this.size = size;
    }

    public BikeSize getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Bike{" + "id='" + id + '\'' + ", size=" + size + ", available=" + available + '}';
    }
}

/**
 * Scooter subclass with fuel type property
 */
class Scooter extends Vehicle {
    private FuelType fuelType;

    public Scooter(String id, FuelType fuelType) {
        super(id, VehicleCategory.SCOOTER);
        this.fuelType = fuelType;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    @Override
    public String toString() {
        return "Scooter{" + "id='" + id + '\'' + ", fuelType=" + fuelType + ", available=" + available + '}';
    }
}

/**
 * Customer entity
 */
class Customer {
    private final String customerId;
    private final String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Customer{" + "id='" + customerId + '\'' + ", name='" + name + '\'' + '}';
    }
}

/**
 * Rental entity — links customer and vehicle
 */
class Rental {
    private final String rentalId;
    private final Vehicle vehicle;
    private final Customer customer;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private RentalStatus status;
    private double amount;

    public Rental(String rentalId, Vehicle vehicle, Customer customer) {
        this.rentalId = rentalId;
        this.vehicle = vehicle;
        this.customer = customer;
        this.status = RentalStatus.ONGOING;
        this.startTime = LocalDateTime.now();
    }

    public void endRental(LocalDateTime endTime, double amount) {
        this.endTime = endTime;
        this.amount = amount;
        this.status = RentalStatus.COMPLETED;
    }

    public String getRentalId() {
        return rentalId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getAmount() {
        return amount;
    }

    public RentalStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Rental{" + "rentalId='" + rentalId + '\'' + ", vehicle=" + vehicle + ", customer=" + customer + ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", amount=" + amount + '}';
    }
}

// -------------------- FACTORY PATTERN -------------------- //

/**
 * VehicleFactory - responsible for creating Bike/Scooter objects
 */
class VehicleFactory {
    public static Vehicle createBike(String id, BikeSize size) {
        return new Bike(id, size);
    }

    public static Vehicle createScooter(String id, FuelType fuelType) {
        return new Scooter(id, fuelType);
    }
}

// -------------------- STRATEGY PATTERN -------------------- //

/**
 * PricingStrategy interface for dynamic pricing logic
 */
interface PricingStrategy {
    double calculatePrice(Rental rental);
}

/**
 * Simple hourly pricing strategy
 */
class HourlyPricingStrategy implements PricingStrategy {
    private static final double RATE_PER_HOUR = 50.0;

    @Override
    public double calculatePrice(Rental rental) {
        Duration duration = Duration.between(rental.getStartTime(), LocalDateTime.now());
        long hours = Math.max(1, duration.toHours());
        return RATE_PER_HOUR * hours;
    }
}

// -------------------- REPOSITORY LAYER -------------------- //

interface VehicleRepository {
    void addVehicle(Vehicle vehicle);

    List<Vehicle> getAll();

    Optional<Vehicle> findById(String id);
}

class InMemoryVehicleRepository implements VehicleRepository {
    private final List<Vehicle> vehicles = new ArrayList<>();

    @Override
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    @Override
    public List<Vehicle> getAll() {
        return vehicles;
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicles.stream().filter(v -> v.getId().equals(id)).findFirst();
    }
}

interface RentalRepository {
    void save(Rental rental);

    Optional<Rental> findById(String rentalId);

    List<Rental> getAll();
}

class InMemoryRentalRepository implements RentalRepository {
    private final Map<String, Rental> rentals = new HashMap<>();//rentalId -> Rental

    @Override
    public void save(Rental rental) {
        rentals.put(rental.getRentalId(), rental);//save or update
    }

    @Override
    public Optional<Rental> findById(String rentalId) {
        return Optional.ofNullable(rentals.get(rentalId));//null if not found
    }

    @Override
    public List<Rental> getAll() {
        return new ArrayList<>(rentals.values());
    }
}

// -------------------- SERVICE LAYER -------------------- //

/**
 * InventoryManager - handles availability and allocation
 */
class InventoryManager {
    private final VehicleRepository vehicleRepo;
    //other ways to handle concurrency can be used
    private final ReentrantLock lock = new ReentrantLock();

    public InventoryManager(VehicleRepository vehicleRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> available = new ArrayList<>();
        for (Vehicle v : vehicleRepo.getAll()) {
            if (v.isAvailable()) available.add(v);
        }
        return available;
    }

    // Allocate vehicle if available
    public Optional<Vehicle> allocateVehicle(String vehicleId) {
        lock.lock();    // Thread-safe allocation
        try {
            Optional<Vehicle> vehicle = vehicleRepo.findById(vehicleId);
            if (vehicle.isPresent() && vehicle.get().isAvailable()) {
                vehicle.get().markRented();//mark as rented
                return vehicle;
            }
            return Optional.empty();
        } finally {
            lock.unlock();
        }
    }

    public synchronized void releaseVehicle(String vehicleId) {
        vehicleRepo.findById(vehicleId).ifPresent(Vehicle::markAvailable);
    }
}

/**
 * RentalService - main facade to handle rent/return workflows
 */
class BikeRentalService {
    private final InventoryManager inventoryManager;
    private final RentalRepository rentalRepo;
    private final PricingStrategy pricingStrategy;

    public BikeRentalService(InventoryManager inventoryManager, RentalRepository rentalRepo, PricingStrategy pricingStrategy) {
        this.inventoryManager = inventoryManager;
        this.rentalRepo = rentalRepo;
        this.pricingStrategy = pricingStrategy;
    }

    /**
     * Rent a vehicle
     */
    public Optional<Rental> rentVehicle(Customer customer, String vehicleId) {
        Optional<Vehicle> vehicleOpt = inventoryManager.allocateVehicle(vehicleId);
        if (vehicleOpt.isEmpty()) {
            System.out.println("Vehicle not available!");
            return Optional.empty();
        }

        Vehicle vehicle = vehicleOpt.get();
        Rental rental = new Rental(UUID.randomUUID().toString(), vehicle, customer);
        rentalRepo.save(rental);
        System.out.println("Rental started for vehicle: " + vehicle.getId());
        return Optional.of(rental);
    }

    /**
     * Return a vehicle
     */
    public void returnVehicle(String rentalId) {
        Optional<Rental> rentalOpt = rentalRepo.findById(rentalId);
        if (rentalOpt.isEmpty()) {
            System.out.println("Rental not found!");
            return;
        }

        Rental rental = rentalOpt.get();
        double amount = pricingStrategy.calculatePrice(rental);
        rental.endRental(LocalDateTime.now(), amount);

        inventoryManager.releaseVehicle(rental.getVehicle().getId());
        System.out.println("Rental completed. Total amount: ₹" + amount);
    }
}

// -------------------- MAIN (CLI Simulation) -------------------- //

public class Main {
    public static void main(String[] args) {
        // Setup repositories
        VehicleRepository vehicleRepo = new InMemoryVehicleRepository();
        RentalRepository rentalRepo = new InMemoryRentalRepository();

        // Create vehicles using factory
        vehicleRepo.addVehicle(VehicleFactory.createBike("B1", BikeSize.SMALL));
        vehicleRepo.addVehicle(VehicleFactory.createBike("B2", BikeSize.LARGE));
        vehicleRepo.addVehicle(VehicleFactory.createScooter("S1", FuelType.ELECTRIC));
        vehicleRepo.addVehicle(VehicleFactory.createScooter("S2", FuelType.PETROL));

        // Setup managers and services
        InventoryManager inventoryManager = new InventoryManager(vehicleRepo);
        PricingStrategy pricingStrategy = new HourlyPricingStrategy();
        BikeRentalService rentalService = new BikeRentalService(inventoryManager, rentalRepo, pricingStrategy);

        // Create a customer
        Customer saurabh = new Customer("C1", "Saurabh");

        // Show available vehicles
        System.out.println("Available Vehicles:");
        inventoryManager.getAvailableVehicles().forEach(System.out::println);

        // Rent a vehicle
        Optional<Rental> rental = rentalService.rentVehicle(saurabh, "S1");
        Optional<Rental> rental1 = rentalService.rentVehicle(saurabh, "S2");
        if (rental.isEmpty()) {
            System.out.println("Failed to rent vehicle.");
            return;
        }
        // Return vehicle
        //rental.ifPresent(r -> rentalService.returnVehicle(r.getRentalId()));

        // Show all rentals
        System.out.println("\nAll Rentals:");
        rentalRepo.getAll().forEach(System.out::println);
        inventoryManager.getAvailableVehicles().forEach(System.out::println);
    }
}
