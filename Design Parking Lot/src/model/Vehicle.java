package model;

import enums.VehicleType;

public class Vehicle {
    private final String vehicleNumber;
    private final VehicleType type;

    public Vehicle(String vehicleNumber, VehicleType type) {
        this.vehicleNumber = vehicleNumber;
        this.type = type;
    }

    public String getNumber() { return vehicleNumber; }
    public VehicleType getType() { return type; }
}
