package test;

import enums.VehicleType;
import model.*;
import observer.NotificationService;
import service.ParkingLotService;
import strategy.FirstAvailableSpotAllocator;
import strategy.SpotAllocationStrategy;

import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private static int failures = 0;

    private static void assertTrue(String message, boolean condition) {
        if (!condition) {
            failures++;
            System.err.println("FAIL: " + message);
        } else {
            System.out.println("PASS: " + message);
        }
    }

    public static void main(String[] args) {
        testParkAndUnparkFlow();
        testNoAvailableSpot();
        testObserverNotifications();
        System.out.println("---- TEST SUMMARY ----");
        if (failures == 0) {
            System.out.println("All tests passed.");
        } else {
            System.out.println(failures + " test(s) failed.");
        }
    }

    private static ParkingLotService createService(int carSpots, int bikeSpots) {
        List<ParkingFloor> floors = new ArrayList<>();
        List<ParkingSpot> floor0 = new ArrayList<>();
        int id = 1;
        for (int i = 0; i < carSpots; i++) floor0.add(new ParkingSpot(id++, VehicleType.CAR, 0));
        for (int i = 0; i < bikeSpots; i++) floor0.add(new ParkingSpot(id++, VehicleType.BIKE, 0));
        floors.add(new ParkingFloor(0, floor0));
        SpotAllocationStrategy strategy = new FirstAvailableSpotAllocator();
        return new ParkingLotService(floors, strategy);
    }

    private static void testParkAndUnparkFlow() {
        ParkingLotService service = createService(1,1);
        Vehicle car = new Vehicle("CAR-1", VehicleType.CAR);
        Ticket ticket = service.parkVehicle(car);
        assertTrue("Ticket should not be null when spot available", ticket != null);
        assertTrue("SpotId should match first spot", ticket.getSpotId() == 1);
        service.unparkVehicle(ticket);
        Ticket ticket2 = service.parkVehicle(new Vehicle("CAR-2", VehicleType.CAR));
        assertTrue("Should reuse freed car spot", ticket2 != null && ticket2.getSpotId() == 1);
    }

    private static void testNoAvailableSpot() {
        ParkingLotService service = createService(1,0);
        Ticket t1 = service.parkVehicle(new Vehicle("CAR-A", VehicleType.CAR));
        Ticket t2 = service.parkVehicle(new Vehicle("CAR-B", VehicleType.CAR));
        assertTrue("Second car should not find spot", t2 == null);
    }

    private static void testObserverNotifications() {
        ParkingLotService service = createService(1,0);
        TestObserver obs = new TestObserver();
        service.addObserver(obs);
        Ticket t = service.parkVehicle(new Vehicle("CAR-X", VehicleType.CAR));
        service.unparkVehicle(t);
        assertTrue("Observer should have received two events", obs.getEvents().size() == 2);
        assertTrue("First event should be PARKED", obs.getEvents().get(0).name().equals("PARKED"));
        assertTrue("Second event should be UNPARKED", obs.getEvents().get(1).name().equals("UNPARKED"));
    }
}

