package service;

import model.Vehicle;
import repository.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * InventoryManager - handles availability and allocation
 * If you do not use thread safety in this use case, concurrent access to methods like `allocateVehicle` and
 * `releaseVehicle` can lead to race conditions. For example, two threads might allocate the same vehicle
 * simultaneously, causing inconsistent state (e.g., double booking). This can result in data corruption,
 * unexpected behavior, and bugs that are hard to reproduce and debug. Thread safety ensures that only one
 * thread can modify shared resources at a time, preventing such issues.
 */
public class InventoryManager {
    private final VehicleRepository vehicleRepo;
    //other ways to handle concurrency can be used
    private final ReentrantLock lock = new ReentrantLock();

    public InventoryManager(VehicleRepository vehicleRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> available = new ArrayList<>();
        for (Vehicle v : vehicleRepo.getAll()) {
            if (v.isAvailable()) {
                available.add(v);
            }
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
        for (Vehicle v : vehicleRepo.getAll()) {
            if (v.getId().equals(vehicleId)) {
                v.markAvailable();
                break;
            }
        }
    }
}

