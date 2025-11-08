package model;

import enums.VehicleCategory;

/**
 * Base Vehicle class — common attributes for all vehicles
 */
public abstract class Vehicle {
    protected String id;
    protected VehicleCategory category;
    protected boolean available;

    public Vehicle(String id, VehicleCategory category) {
        this.id = id;
        this.category = category;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public VehicleCategory getVehicleCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void markRented() {
        available = false;
    }

    public void markAvailable() {
        available = true;
    }

    @Override
    public String toString() {
        return "Vehicle{" + "id='" + id + '\'' + ", category=" + category + ", available=" + available + '}';
    }
}
