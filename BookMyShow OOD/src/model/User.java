package model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class User {
    private final String userId;
    private final String name;
    private final String email;
    private final List<Booking>bookings;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.bookings = new ArrayList<>();
    }
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
}
