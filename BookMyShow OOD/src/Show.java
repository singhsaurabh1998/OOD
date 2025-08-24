import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public String getShowId() {
        return showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public Screen getScreen() {
        return screen;
    }

    public List<Seat> getSeats() {
        return seats;
    }

}
