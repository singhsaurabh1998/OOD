import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String name;
    private String email;
    private String city;
    List<Booking>bookings;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.bookings = new ArrayList<>();
    }
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Booking> getBookings() { return bookings; }
}
