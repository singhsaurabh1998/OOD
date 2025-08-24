import java.util.Map;

public interface SlotFindingStrategy {
    ParkingSlot findSlot(Vehicle vehicle, Map<Integer, Floor> floors);

}
