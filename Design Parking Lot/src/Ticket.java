import java.time.LocalDateTime;

public class Ticket {
    private static final String LOT_ID = "PR1234";
    private final String ticketId;
    private final Vehicle vehicle;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public Ticket(Vehicle vehicle, int floorNumber, int slotNumber) {
        this.vehicle = vehicle;
        this.entryTime = LocalDateTime.now();
        this.exitTime = null;
        this.ticketId = String.format("%s_%d_%d", LOT_ID, floorNumber, slotNumber);
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    /**
     * Marks when the vehicle leaves.
     */
    public void markExit() {
        this.exitTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", vehicle=" + vehicle +
                ", entryTime=" + entryTime +
                ", exitTime=" + exitTime +
                '}';
    }
}
