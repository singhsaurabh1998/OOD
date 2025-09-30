package model;

import enums.SeatType;

public class Seat {
    private static int seatCounter = 0; // for generating unique seat IDs
    private final int seatId;
    private final int row;
    private final int columNumber;
    private final SeatType type;
    private boolean isBooked;  // for now (later: booking status via enums or object)

    public Seat(int row, int columNumber, SeatType type) {
        this.row = row;
        this.columNumber = columNumber;
        this.type = type;
        this.isBooked = false;
        this.seatId = ++seatCounter;
    }

    public boolean isBooked() {
        return this.isBooked;
    }

    public void bookSeat() {
        this.isBooked = true;
    }

    public void cancelSeat() {
        this.isBooked = false;
    }

    // Getters
    public int getSeatId() {
        return seatId;
    }

    public int getRow() {
        return row;
    }

    public int getNumber() {
        return columNumber;
    }

    public SeatType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatId='" + seatId + '\'' +
                ", row=" + row +
                ", number=" + columNumber +
                ", type=" + type +
                ", isBooked=" + isBooked +
                '}';
    }
}
