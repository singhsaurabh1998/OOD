package factory;

import enums.BikeSize;
import enums.FuelType;
import model.*;

/**
 * VehicleFactory - responsible for creating Bike/Scooter objects
 */
public class VehicleFactory {
    public static Vehicle createBike(String id, BikeSize size) {
        return new Bike(id, size);
    }

    public static Vehicle createScooter(String id, FuelType fuelType) {
        return new Scooter(id, fuelType);
    }
}

