package strategy;

import model.ParkingFloor;
import model.*;

import java.util.List;

public class FirstAvailableSpotAllocator implements SpotAllocationStrategy {
    public ParkingSpot allocateSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            for (ParkingSpot spot : floor.getSpots()) {
                if (spot.isFree() && spot.getSpotType() == vehicle.getType()) {
                    return spot;
                }
            }
        }
        return null; // no free spot
    }
}
