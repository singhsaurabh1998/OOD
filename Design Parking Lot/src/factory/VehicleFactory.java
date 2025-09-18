package factory;

import enums.VehicleType;
import model.Vehicle;

public class VehicleFactory {
    public static Vehicle createVehicle(String number, VehicleType type) {
        return new Vehicle(number, type);
    }
}
