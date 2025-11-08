package model;

import enums.FuelType;
import enums.VehicleCategory;

/**
 * Scooter subclass with fuel type property
 */
public class Scooter extends Vehicle {
    private final FuelType fuelType;

    public Scooter(String id, FuelType fuelType) {
        super(id, VehicleCategory.SCOOTER);
        this.fuelType = fuelType;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    @Override
    public String toString() {
        return "Scooter{" + "id='" + id + '\'' + ", fuelType=" + fuelType + ", available=" + available + '}';
    }
}

