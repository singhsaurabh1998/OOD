package model;

import enums.VehicleType;

/**
 * ParkingSpot represents a single parking spot in the parking lot.
 */
public class ParkingSpot {
    private final int id;
    private final VehicleType spotType;//for which type of vehicle it can be used
    private Vehicle vehicle; // null if free

    public ParkingSpot(int id, VehicleType spotType) {
        this.id = id;
        this.spotType = spotType;
    }

    public boolean isFree() {
        return vehicle == null;
    }

    public synchronized boolean assign(Vehicle v) {
        if (isFree() && v.getType() == spotType) {
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
}
