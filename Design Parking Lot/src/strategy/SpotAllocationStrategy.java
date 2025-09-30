package strategy;

import model.ParkingFloor;
import model.ParkingSpot;
import model.Vehicle;

import java.util.List;
/**
 * Strategy interface for allocating parking spots based on different algorithms.
 * Implementations of this interface can define various strategies for selecting
 * an appropriate parking spot for a vehicle from a list of parking floors.
 */
public interface SpotAllocationStrategy {
    ParkingSpot allocateSpot(List<ParkingFloor> floors, Vehicle vehicle);
}
