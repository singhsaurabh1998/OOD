import java.util.Map;

public class FirstAvailableSlotStrategy implements SlotFindingStrategy {
    @Override
    public ParkingSlot findSlot(Vehicle vehicle, Map<Integer, Floor> floors) {
        for (Floor floor : floors.values()) {
            ParkingSlot slot = floor.findFirstAvailable(vehicle.getType());
            if (slot != null) {
                return slot;
            }
        }
        return null;
    }
}

