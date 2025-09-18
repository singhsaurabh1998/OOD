package strategy;

import model.ParkingFloor;
import model.ParkingSpot;
import model.Vehicle;

import java.util.List;

public interface SpotAllocationStrategy {
    ParkingSpot allocateSpot(List<ParkingFloor> floors, Vehicle vehicle);
}
