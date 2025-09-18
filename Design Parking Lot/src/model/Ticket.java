package model;

import java.time.LocalDateTime;
import java.util.UUID;


public class Ticket {
    private final String ticketId;
    private final String vehicleNumber;
    private final int spotId;
    private final LocalDateTime entryTime;

    public Ticket(String vehicleNumber, int spotId) {
        this.ticketId = UUID.randomUUID().toString();
        this.vehicleNumber = vehicleNumber;
        this.spotId = spotId;
        this.entryTime = LocalDateTime.now();
    }

    public String getTicketId() { return ticketId; }
    public String getVehicleNumber() { return vehicleNumber; }
    public int getSpotId() { return spotId; }
    public LocalDateTime getEntryTime() { return entryTime; }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", spotId=" + spotId +
                ", entryTime=" + entryTime +
                '}';
    }
}
