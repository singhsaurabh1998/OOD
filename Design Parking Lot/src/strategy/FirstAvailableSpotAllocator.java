package strategy;

import model.ParkingFloor;
import model.ParkingSpot;
import model.Vehicle;

import java.util.List;

public class FirstAvailableSpotAllocator implements SpotAllocationStrategy {
    public ParkingSpot allocateSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        // Iterate through all floors and spots to find the first available spot
        for (ParkingFloor floor : floors) {
            for (ParkingSpot spot : floor.getSpots()) {
                if (spot.isSpotFree() && spot.getSpotType() == vehicle.getType()) {
                    return spot;
                }
            }
        }
        return null; // no free spot
    }
}
