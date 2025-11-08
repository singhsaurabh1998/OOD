package model;

import enums.BikeSize;
import enums.VehicleCategory;

/**
 * Bike subclass with size property
 */
public class Bike extends Vehicle {
    private final BikeSize size;

    public Bike(String id, BikeSize size) {
        super(id, VehicleCategory.BIKE);
        this.size = size;
    }

    public BikeSize getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Bike{" + "id='" + id + '\'' + ", size=" + size + ", available=" + available + '}';
    }
}

