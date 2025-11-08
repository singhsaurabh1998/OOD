import FacadeLayer.EntrancePanel;
import enums.VehicleType;
import model.ParkingFloor;
import model.ParkingSpot;
import model.Ticket;
import observer.SMS;
import observer.WhatsApp;
import service.ParkingLotService;
import strategy.FirstAvailableSpotAllocator;
import strategy.HourlyPricing;
import strategy.SpotAllocationStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoMain {
    public static void main(String[] args) {
        System.out.println("[Demo] Starting demo main...");
        try {
            // Build parking floors
            List<ParkingFloor> floors = new ArrayList<>();
            List<ParkingSpot> floor0Spots = Arrays.asList(
                    new ParkingSpot(1, VehicleType.CAR, 0),
                    new ParkingSpot(2, VehicleType.BIKE, 0)
            );
            List<ParkingSpot> floor1Spots = Arrays.asList(
                    new ParkingSpot(3, VehicleType.CAR, 1),
                    new ParkingSpot(4, VehicleType.TRUCK, 1)
            );
            floors.add(new ParkingFloor(0, floor0Spots));
            floors.add(new ParkingFloor(1, floor1Spots));
            System.out.println("[Demo] Floors initialized: " + floors.size());

            SpotAllocationStrategy strategy = new FirstAvailableSpotAllocator();
            ParkingLotService service = new ParkingLotService(floors, strategy, new HourlyPricing(5, 4));
            System.out.println("[Demo] Service created.");
            service.addObserver(new SMS());
            service.addObserver(new WhatsApp());
            System.out.println("[Demo] Observers registered.");

            EntrancePanel entrance = new EntrancePanel(service);
            System.out.println("[Demo] Entrance panel ready.");

            Ticket t1 = entrance.scanAndPark("CAR-123", VehicleType.CAR);
            System.out.println("[Demo] Ticket1=" + t1);
            Ticket t2 = entrance.scanAndPark("BIKE-9", VehicleType.BIKE);
            System.out.println("[Demo] Ticket2=" + t2);
            Ticket t3 = entrance.scanAndPark("TRUCK-77", VehicleType.TRUCK);
            System.out.println("[Demo] Ticket3=" + t3);
            Ticket t4 = entrance.scanAndPark("CAR-999", VehicleType.CAR); // second car spot
            System.out.println("[Demo] Ticket4=" + t4);
            Ticket t5 = entrance.scanAndPark("CAR-NO-SPOT", VehicleType.CAR); // should fail
            System.out.println("[Demo] Ticket5=" + t5);

            service.showAllTickets();

            double fee = service.unparkVehicle(t2); // free bike
            System.out.println("Fee for ticket2: " + fee);
            service.unparkVehicle(t1); // free car
            service.showAllTickets();

            Ticket t6 = entrance.scanAndPark("CAR-NEW", VehicleType.CAR);
            System.out.println("[Demo] Ticket6=" + t6);
            service.showAllTickets();
            System.out.println("[Demo] Demo completed successfully.");
        } catch (Exception e) {
            System.err.println("[Demo] Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
