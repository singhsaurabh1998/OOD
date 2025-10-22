package model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Ticket {
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private final String ticketId;
    private final String vehicleNumber;
    private final int spotId;
    private final int floorId;
    private final LocalDateTime entryTime;

    public Ticket(String vehicleNumber, int spotId, int floorId) {
        this.ticketId = "Ticket-" + idCounter.incrementAndGet();
        this.vehicleNumber = vehicleNumber;
        this.spotId = spotId;
        this.entryTime = LocalDateTime.now();
        this.floorId = floorId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public int getSpotId() {
        return spotId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public int getFloorId() { return floorId; }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", spotId=" + spotId +
                ", floorId=" + floorId +
                ", entryTime=" + entryTime +
                '}';
    }
}
