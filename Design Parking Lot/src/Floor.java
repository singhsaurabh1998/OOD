import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Floor represents one level in the ParkingLot.
 * It “has-a” collection of ParkingSlot objects.
 */
public class Floor {
    private final int floorNumber;
    private final List<ParkingSlot> slots;

    /**
     * Construct a new Floor.
     *
     * @param floorNumber    1-based floor index.
     * @param slotsAvailable total number of slots to create on this floor.
     *                       We assume:
     *                       - slot 1 is MOTORCYCLE
     *                       - next 2 slots are BUS
     *                       - the rest are CAR
     */
    public Floor(int floorNumber, int slotsAvailable) {
        this.floorNumber = floorNumber;
        this.slots = new ArrayList<>(slotsAvailable);
        for (int i = 1; i <= slotsAvailable; i++) { //assign the floor
            VehicleType type;
            if (i == 1) {
                type = VehicleType.MOTORCYCLE;
            } else if (i == 2 || i == 3) {
                type = VehicleType.BUS;
            } else
                type = VehicleType.CAR;
            //floor has-a parking slots
            slots.add(new ParkingSlot(floorNumber, i, type));
        }

    }

    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Find the first available slot for the given vehicle type.
     * Rules:
     * 1) slot.slotType.canFit(vehicleType)
     * 2) lowest slotNumber
     *
     * @param vehicleType
     * @return the slot, or null if none free
     */
    public ParkingSlot findFirstAvailable(VehicleType vehicleType) {
        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied() && slot.getSlotType() == vehicleType) {
                return slot;
            }
        }
        return null;
    }

    /**
     * Returns a list of free slots for the given vehicle type,
     * sorted by slotNumber.
     */
    public List<ParkingSlot> getFreeSlots(VehicleType vehicleType) {
        List<ParkingSlot> result = new ArrayList<>();
        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied() && slot.getSlotType() == vehicleType) {
                result.add(slot);
            }
        }
        result.sort(Comparator.comparingInt(ParkingSlot::getSlotNumber));
        return result;
    }

    /**
     * Returns a list of occupied slots for the given vehicle type,
     * sorted by slotNumber.
     */
    public List<ParkingSlot> getOccupiedSlots(VehicleType vehicleType) {
        List<ParkingSlot> result = new ArrayList<>();
        for (ParkingSlot slot : slots) {
            if (slot.isOccupied() && slot.getSlotType() == vehicleType) {
                result.add(slot);
            }
        }
        Collections.sort(result, Comparator.comparingInt(ParkingSlot::getSlotNumber));
        return result;
    }

    /**
     * Returns the count of free slots for the given vehicle type.
     */
    public int getFreeSlotCount(VehicleType vehicleType) {
        int count = 0;
        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied() && slot.getSlotType() == vehicleType) {
                count++;
            }
        }
        return count;
    }
    public List<ParkingSlot> getAllSlots() {
        return slots;
    }
}
