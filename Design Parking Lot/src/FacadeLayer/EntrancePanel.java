package FacadeLayer;

import enums.VehicleType;
import factory.VehicleFactory;
import model.Ticket;
import model.Vehicle;
import service.ParkingLotService;

public class EntrancePanel {
    private final ParkingLotService parkingLotService;

    public EntrancePanel(ParkingLotService parkingLot) {
        this.parkingLotService = parkingLot;
    }

    public Ticket scanAndPark(String vehicleNumber, VehicleType type) {
        Vehicle vehicle = VehicleFactory.createVehicle(vehicleNumber, type);
        return parkingLotService.parkVehicle(vehicle);
    }
}
