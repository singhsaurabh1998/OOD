import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * ParkingLot is the main service that orchestrates floors, slots, tickets, and vehicles.
 * — “has-a” relationship with Floor and Ticket.
 */
public class ParkingLotService {
    private final String lotId;
    private final Map<Integer, Floor> floors;           // floorNumber → Floor
    private final Map<String, Ticket> activeTickets;    // ticketId → Ticket

    /**
     * Construct a ParkingLot.
     *
     * @param lotId unique identifier, e.g. "PR1234"
     */
    public ParkingLotService(String lotId) {
        this.lotId = lotId;
        this.floors = new TreeMap<>();      // TreeMap so floors are sorted by key
        this.activeTickets = new HashMap<>();
    }

    /**
     * Add a new floor with a given number of slots.
     *
     * @param floorNumber 1-based
     * @param totalSlots  total slots on that floor
     */
    public void addFloor(int floorNumber, int totalSlots) {
        if (floors.containsKey(floorNumber)) {
            System.out.println("⚠️ Floor " + floorNumber + " already exists.");
            return;
        }
        floors.put(floorNumber, new Floor(floorNumber, totalSlots));
        System.out.println("✔️ Added Floor " + floorNumber + " with " + totalSlots + " slots.");
    }

    /**
     * Parks a vehicle by finding the first available slot.
     *
     * @param vehicle to park
     * @return Ticket if successful; null otherwise
     */
    public Ticket parkVehicle(Vehicle vehicle) {
        for (Floor floor : floors.values()) {
            ParkingSlot parkingSlot = floor.findFirstAvailable(vehicle.vehicleType);

            if (parkingSlot != null && parkingSlot.tryToPark(vehicle)) {
                Ticket ticket = new Ticket(vehicle, parkingSlot.getFloorNumber(), parkingSlot.getSlotNumber());
                activeTickets.put(ticket.getTicketId(), ticket);
                System.out.println("✅ Parked " + vehicle.getType()+" "+vehicle.getRegistrationNumber() +
                        " at " + ticket.getTicketId());
                return ticket;
            }
        }
        System.out.println("❌ Parking Full or No Suitable Slot for " + vehicle.getType());
        return null;
    }

    /**
     * Unparks a vehicle given its ticketId.
     *
     * @param ticketId returned when parking
     */
    public void unparkVehicle(String ticketId) {
        Ticket ticket = activeTickets.remove(ticketId);
        if (ticket == null) {
            System.out.println("⚠️ Invalid ticket: " + ticketId);
            return;
        }
        // Find the floor & slot to unpark
        String[] parts = ticketId.split("_");
        int floorNum = Integer.parseInt(parts[1]);
        int slotNum = Integer.parseInt(parts[2]);

        Floor floor = floors.get(floorNum);
        if (floor == null) {
            System.out.println("❌ Floor not found for ticket: " + ticketId);
            return;
        }

        ParkingSlot unpark = null;

        for (ParkingSlot slot : floor.getAllSlots()) {
            if (slot.getSlotNumber() == slotNum) {
                unpark = slot;
                break;
            }
        }
        if (unpark == null) {
            System.out.println("❌ Slot not found for ticket:May unparked earlier theft " + ticketId);
            return;
        }
        Vehicle vehicle = unpark.unpark();
        ticket.markExit();
        System.out.println("✅ Unparked " + vehicle.getRegistrationNumber() +
                " from " + ticketId +
                ". Entry: " + ticket.getEntryTime() +
                ", Exit: " + ticket.getExitTime());
    }

    //Displays the count of free slots on each floor for the given vehicle type.
    public void displayFreeCountPerFloor(VehicleType type) {
        System.out.println("Free slot count for " + type + ":");
        for (Map.Entry<Integer, Floor> entry : floors.entrySet()) {
            int freeCount = entry.getValue().getFreeSlotCount(type);
            System.out.println("  Floor " + entry.getKey() + ": " + freeCount);
        }
    }

    // Displays all free slot IDs per floor for the given vehicle type.
    public void displayFreeSlotsPerFloor(VehicleType type) {
        System.out.println("Free slots for " + type + ":");
        for (Map.Entry<Integer, Floor> entry : floors.entrySet()) {
            List<ParkingSlot> free = entry.getValue().getFreeSlots(type);
            System.out.print("  Floor " + entry.getKey() + ": ");
            free.forEach(s -> System.out.print(s.getSlotNumber() + " "));
            System.out.println();
        }
    }

    //Displays all occupied slot IDs per floor for the given vehicle type.
    public void displayOccupiedSlotsPerFloor(VehicleType type) {
        System.out.println("Occupied slots for " + type + ":");
        for (Map.Entry<Integer, Floor> entry : floors.entrySet()) {
            List<ParkingSlot> occ = entry.getValue().getOccupiedSlots(type);
            System.out.print("  Floor " + entry.getKey() + ": ");
            occ.forEach(s -> System.out.print(s.getSlotNumber() + " "));
            System.out.println();
        }
    }
}
