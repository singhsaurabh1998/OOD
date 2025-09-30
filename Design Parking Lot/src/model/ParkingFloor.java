package model;

import java.util.List;

/**
 * Represents a floor in the parking lot containing multiple parking spots.
 * Each floor has a unique identifier and a list of parking spots.
 * This class provides methods to access the floor ID and the list of parking spots.
 * It is used to organize parking spots by floors in a multi-level parking lot.
 */
public class ParkingFloor {
    private final int floorId;
    private final List<ParkingSpot> spots;

    public ParkingFloor(int floorId, List<ParkingSpot> spots) {
        this.floorId = floorId;
        this.spots = spots;
    }

    public int getFloorId() {
        return floorId;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }
}
