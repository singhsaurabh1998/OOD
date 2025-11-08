package model;

import enums.VehicleType;

/**
 * ParkingSpot represents a single parking spot in the parking lot.
 */
public class ParkingSpot {
    private final int id;
    private final VehicleType spotType;//for which type of vehicle it can be used
    private Vehicle vehicle; // null if free
    private final int floorId;

    public ParkingSpot(int id, VehicleType spotType, int floorId) {
        this.id = id;
        this.spotType = spotType;
        this.floorId = floorId;
    }

    // Check if the parking spot is free
    public synchronized boolean isSpotFree() {
        return vehicle == null;
    }

    public synchronized boolean assignSpot(Vehicle v) {
        if (isSpotFree() && v.getType() == spotType) {
            this.vehicle = v;
            return true;
        }
        return false;
    }

    public synchronized void vacate() {
        this.vehicle = null;
    }

    public int getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public VehicleType getSpotType() {
        return spotType;
    }

    public int getFloorId() {
        return floorId;
    }
}
