import java.time.LocalDateTime;
import java.util.List;

enum BookingStatus {
    BOOKED, CANCELLED, PAYMENT_FAILED
}

public class Booking {
    private final String bookingId;
    private final User user;
    private final Show show;
    private final List<Seat> seatsBooked;
    private final LocalDateTime bookingTime;
    private BookingStatus status; // BOOKED, CANCELLED, etc.

    public Booking(String bookingId, User user, Show show, List<Seat> seatsBooked) {
        this.bookingId = bookingId;
        this.user = user;
        this.show = show;
        this.seatsBooked = seatsBooked;
        this.bookingTime = LocalDateTime.now();
        this.status = BookingStatus.BOOKED;
    }
    public void cancelBooking(){
        this.status = BookingStatus.CANCELLED;
        for (Seat seat:seatsBooked){
            seat.cancelSeat();
        }
    }

    public String getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeatsBooked() {
        return seatsBooked;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public BookingStatus getBookingStatus() {
        return status;
    }
}
