# Parking Lot Demo & Tests

## Demo
Run the DemoMain to see parking interactions, observers logging events, freeing spots, and re-parking.

## Lightweight Tests
A simple `TestRunner` (no external frameworks) exercises:
- Park & reuse freed spot
- No spot available scenario
- Observer notifications (PARKED, UNPARKED)

## Run (Windows CMD)
```
set SRC="C:\Users\ssingh20\Downloads\Saurabh\Saurabh\New folder\System Design Material\OOD\Design Parking Lot\src"
set OUT="C:\Users\ssingh20\Downloads\Saurabh\Saurabh\New folder\System Design Material\OOD\Design Parking Lot\out"
if not exist %OUT% mkdir %OUT%
for /R %SRC% %f in (*.java) do javac -d %OUT% "%f"
java -cp %OUT% demo.DemoMain
java -cp %OUT% test.TestRunner
```

If you see FAIL lines, inspect the logic; otherwise all PASS.
package demo;

import enums.VehicleType;
import observer.NotificationService;
import observer.SMS;
import observer.WhatsApp;
import service.ParkingLotService;
import strategy.FirstAvailableSpotAllocator;
import strategy.SpotAllocationStrategy;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class DemoMain {
    public static void main(String[] args) {
        // Build parking floors
        List<ParkingFloor> floors = new ArrayList<>();
        List<ParkingSpot> floor0Spots = new ArrayList<>();
        floor0Spots.add(new ParkingSpot(1, VehicleType.CAR, 0));
        floor0Spots.add(new ParkingSpot(2, VehicleType.BIKE, 0));
        List<ParkingSpot> floor1Spots = new ArrayList<>();
        floor1Spots.add(new ParkingSpot(3, VehicleType.CAR, 1));
        floor1Spots.add(new ParkingSpot(4, VehicleType.TRUCK, 1));
        floors.add(new ParkingFloor(0, floor0Spots));
        floors.add(new ParkingFloor(1, floor1Spots));

        SpotAllocationStrategy strategy = new FirstAvailableSpotAllocator();
        ParkingLotService service = new ParkingLotService(floors, strategy);

        // Register observers
        service.addObserver(new SMS());
        service.addObserver(new WhatsApp());

        // Park vehicles
        Ticket t1 = service.parkVehicle(new Vehicle("CAR-123", VehicleType.CAR));
        Ticket t2 = service.parkVehicle(new Vehicle("BIKE-9", VehicleType.BIKE));
        Ticket t3 = service.parkVehicle(new Vehicle("TRUCK-77", VehicleType.TRUCK));
        Ticket t4 = service.parkVehicle(new Vehicle("CAR-999", VehicleType.CAR)); // second car spot
        Ticket t5 = service.parkVehicle(new Vehicle("CAR-NO-SPOT", VehicleType.CAR)); // should fail (no more car spots)

        service.showAllTickets();

        // Unpark vehicles
        service.unparkVehicle(t2);
        service.unparkVehicle(t1);
        service.showAllTickets();

        // Try to park again after freeing spot
        Ticket t6 = service.parkVehicle(new Vehicle("CAR-NEW", VehicleType.CAR));
        service.showAllTickets();
    }
}

