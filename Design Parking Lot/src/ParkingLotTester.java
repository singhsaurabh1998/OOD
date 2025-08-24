public class ParkingLotTester {
    public static void main(String[] args) {
        // Step 1: Create a parking lot
        ParkingLotService parkingLot = new ParkingLotService("PR1234");

        // Step 2: Add 2 floors, each with 6 slots
        parkingLot.addFloor(1, 6);
        parkingLot.addFloor(2, 6);

        // Step 3: Create some vehicles
        Vehicle car1 = new Vehicle("KA-01-AB-1234", VehicleType.CAR);
        Vehicle bike1 = new Vehicle("KA-02-CD-5678", VehicleType.MOTORCYCLE);
        Vehicle truck1 = new Vehicle("KA-03-EF-9999", VehicleType.BUS);
        Vehicle truck2 = new Vehicle("KA-03-EF-9998", VehicleType.BUS);
        Vehicle car2 = new Vehicle("KA-04-GH-8888", VehicleType.CAR);
        // Step 4: Park vehicles
        Ticket ticket1 = parkingLot.parkVehicle(car1);
        Ticket ticket2 = parkingLot.parkVehicle(bike1);
        Ticket ticket3 = parkingLot.parkVehicle(truck1);
        Ticket ticket4 = parkingLot.parkVehicle(car2);

        // Step 5: Display free slot count per floor
        System.out.println();
        parkingLot.displayFreeCountPerFloor(VehicleType.CAR);
        parkingLot.displayFreeCountPerFloor(VehicleType.MOTORCYCLE);
        parkingLot.displayFreeCountPerFloor(VehicleType.BUS);

        // Step 6: Display free and occupied slots per floor
        System.out.println();
        parkingLot.displayFreeSlotsPerFloor(VehicleType.CAR);
        parkingLot.displayOccupiedSlotsPerFloor(VehicleType.CAR);

        System.out.println();
        parkingLot.displayFreeSlotsPerFloor(VehicleType.MOTORCYCLE);
        parkingLot.displayOccupiedSlotsPerFloor(VehicleType.MOTORCYCLE);

        System.out.println();
        parkingLot.displayFreeSlotsPerFloor(VehicleType.BUS);
        parkingLot.displayOccupiedSlotsPerFloor(VehicleType.BUS);

        // Step 7: Unpark a vehicle
        System.out.println();
        if (ticket2 != null) {
            parkingLot.unparkVehicle(ticket2.getTicketId());
        }

        // Step 8: Display again after un-parking
        System.out.println();
        parkingLot.displayFreeCountPerFloor(VehicleType.MOTORCYCLE);
        parkingLot.displayFreeSlotsPerFloor(VehicleType.MOTORCYCLE);
        parkingLot.displayOccupiedSlotsPerFloor(VehicleType.MOTORCYCLE);
    }
}
