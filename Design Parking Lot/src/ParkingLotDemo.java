import FacadeLayer.EntrancePanel;
import enums.VehicleType;
import model.*;
import observer.SMS;
import observer.WhatsApp;
import service.ParkingLotService;
import strategy.FirstAvailableSpotAllocator;

import java.util.Arrays;

public class ParkingLotDemo {
    public static void main(String[] args) {
        // Floor 1 par 2 car spots
        ParkingFloor floor1 = new ParkingFloor(1, Arrays.asList(
                new ParkingSpot(101, VehicleType.CAR,1),
                new ParkingSpot(102, VehicleType.CAR,1)
        ));

        // Floor 2 par 1 bike, 1 car
        ParkingFloor floor2 = new ParkingFloor(2, Arrays.asList(
                new ParkingSpot(201, VehicleType.BIKE,2),
                new ParkingSpot(202, VehicleType.CAR,2)
        ));
        ParkingLotService parkingLot = new ParkingLotService(Arrays.asList(floor1, floor2), new FirstAvailableSpotAllocator());

        User saurabh = new User("s1", "Saurabh", "ss@gmail.com",new Vehicle("Up23CD4567",VehicleType.CAR));

        parkingLot.addObserver(new SMS());
        parkingLot.addObserver(new WhatsApp());

        EntrancePanel entrance = new EntrancePanel(parkingLot);

        Ticket ticket = entrance.scanAndPark(saurabh.getVehicle().getNumber(), saurabh.getVehicle().getType());  // goes to floor 1
        //System.out.println(ticket);
//        parkingLot.showAllTickets();
//        parkingLot.unparkVehicle(ticket);
//        parkingLot.showAllTickets();
        //entrance.scanAndPark("KA-02-5678", VehicleType.CAR);  // goes to floor 1
        //entrance.scanAndPark("KA-03-9999", VehicleType.BIKE); // goes to floor 2
//        entrance.scanAndPark("KA-04-0000", VehicleType.CAR);  // goes to floor 2
//        entrance.scanAndPark("KA-05-1111", VehicleType.CAR);  // no spot
    }
}
