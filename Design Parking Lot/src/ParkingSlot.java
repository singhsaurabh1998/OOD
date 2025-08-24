//Key points:
//park() enforces slot-type vs vehicle-type compatibility.
//Encapsulates its own occupied state (SRP).
public class ParkingSlot {
    private final int floorNumber;
    private final int slotNumber;
    private final VehicleType slotType;
    private boolean occupied;
    private Vehicle parkedVehicle;

    public ParkingSlot(int floorNumber, int slotNumber, VehicleType slotType) {
        this.floorNumber = floorNumber;
        this.slotNumber = slotNumber;
        this.slotType = slotType;
        this.occupied = false;
    }

    /**
     * Attempts to park—the slot must be free and the vehicle must fit.
     */
    public boolean tryToPark(Vehicle vehicle) {
        if (occupied || vehicle.vehicleType != slotType) {
            return false;
        }
        this.parkedVehicle = vehicle;
        this.occupied = true;
        return true;
    }

    /**
     * Frees the slot and returns the vehicle.
     */
    public Vehicle unpark() {
        if (!occupied)
            return null;
        Vehicle v = parkedVehicle;
        this.parkedVehicle = null;
        this.occupied = false;
        return v;
    }
    public int getFloorNumber() { return floorNumber; }
    public int getSlotNumber() { return slotNumber; }
    public VehicleType getSlotType() { return slotType; }
    public boolean isOccupied() { return occupied; }
}
