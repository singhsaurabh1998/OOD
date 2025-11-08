package model;

import enums.RentalStatus;

import java.time.LocalDateTime;

/**
 * Rental entity — links customer and vehicle
 */
public class Rental {
    private final String rentalId;
    private final Vehicle vehicle;
    private final Customer customer;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private RentalStatus status;
    private double amount;

    public Rental(String rentalId, Vehicle vehicle, Customer customer) {
        this.rentalId = rentalId;
        this.vehicle = vehicle;
        this.customer = customer;
        this.status = RentalStatus.ONGOING;
        this.startTime = LocalDateTime.now();
    }

    public void endRental(LocalDateTime endTime, double amount) {
        this.endTime = endTime;
        this.amount = amount;
        this.status = RentalStatus.COMPLETED;
    }

    public String getRentalId() {
        return rentalId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getAmount() {
        return amount;
    }

    public RentalStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Rental{" + "rentalId='" + rentalId + '\'' + ", vehicle=" + vehicle + ", customer=" + customer + ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", amount=" + amount + '}';
    }
}

