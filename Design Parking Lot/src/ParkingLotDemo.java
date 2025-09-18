import FacadeLayer.EntrancePanel;
import enums.VehicleType;
import model.ParkingFloor;
import model.ParkingSpot;
import model.Ticket;
import observer.ConsoleLogger;
import service.ParkingLotService;
import strategy.FirstAvailableSpotAllocator;

import java.util.Arrays;

public class ParkingLotDemo {
    public static void main(String[] args) {
        // Floor 1 par 2 car spots
        ParkingFloor floor1 = new ParkingFloor(1, Arrays.asList(
                new ParkingSpot(101, VehicleType.CAR),
                new ParkingSpot(102, VehicleType.CAR)
        ));

        // Floor 2 par1 bike, 1 car
        ParkingFloor floor2 = new ParkingFloor(2, Arrays.asList(
                new ParkingSpot(201, VehicleType.BIKE),
                new ParkingSpot(202, VehicleType.CAR)
        ));

        ParkingLotService parkingLot = new ParkingLotService(Arrays.asList(floor1, floor2), new FirstAvailableSpotAllocator());
        parkingLot.addObserver(new ConsoleLogger());

        EntrancePanel entrance = new EntrancePanel(parkingLot);

        Ticket ticket = entrance.scanAndPark("KA-01-1234", VehicleType.CAR);  // goes to floor 1
        //System.out.println(ticket);
        parkingLot.showAllTickets();
        parkingLot.unparkVehicle(ticket);
        parkingLot.showAllTickets();
        entrance.scanAndPark("KA-02-5678", VehicleType.CAR);  // goes to floor 1
        entrance.scanAndPark("KA-03-9999", VehicleType.BIKE); // goes to floor 2
        entrance.scanAndPark("KA-04-0000", VehicleType.CAR);  // goes to floor 2
        entrance.scanAndPark("KA-05-1111", VehicleType.CAR);  // no spot
    }
}
