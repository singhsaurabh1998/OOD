package FacadeLayer;

import enums.VehicleType;
import factory.VehicleFactory;
import model.Ticket;
import model.Vehicle;
import service.ParkingLotService;

/**
 * EntrancePanel acts as a facade for parking lot entry operations.
 * It simplifies the process of scanning a vehicle and creates a parking ticket.
 */
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
