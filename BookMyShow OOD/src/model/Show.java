package model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
public class Show {
    private final String showId;
    private final Movie movie;
    private final LocalDateTime showTime;
    private final Screen screen;
    private final List<Seat> seats;

    public Show(String showId, Movie movie, LocalDateTime showTime, Screen screen, List<Seat> seats) {
        this.showId = showId;
        this.movie = movie;
        this.showTime = showTime;
        this.screen = screen;
        this.seats = seats;
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> availableSeats = new ArrayList<>();

        for (Seat seat : seats) {
            if (!seat.isBooked()) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }


    // Prints all details related to the show
    public void printAllDetails() {
        System.out.println("Show ID: " + showId);
        System.out.println("Movie: " + movie);
        System.out.println("Show Time: " + showTime);
        System.out.println("Screen: " + screen);
        System.out.println("Seats:");
        for (Seat seat : seats) {
            System.out.println("  " + seat);
        }
    }
}
