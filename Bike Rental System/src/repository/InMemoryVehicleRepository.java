package repository;

import model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryVehicleRepository implements VehicleRepository {
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

