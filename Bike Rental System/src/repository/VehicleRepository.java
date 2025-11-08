package repository;

import model.Vehicle;
import java.util.List;
import java.util.Optional;

//hold vehicle records
public interface VehicleRepository {
    void addVehicle(Vehicle vehicle);
    List<Vehicle> getAll();
    Optional<Vehicle> findById(String id);
}

