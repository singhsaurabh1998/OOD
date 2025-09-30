package factory;

import enums.VehicleType;
import model.Vehicle;

/**
 * VehicleFactory is responsible for creating Vehicle instances.
 */
public class VehicleFactory {
    public static Vehicle createVehicle(String number, VehicleType type) {
        return new Vehicle(number, type);
    }
}
