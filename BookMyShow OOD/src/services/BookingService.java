package services;

import Observer.NotificationService;
import enums.BookingStatus;
import model.Booking;
import model.Seat;
import model.Show;
import model.User;
import security.SeatLockProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookingService {
    private final Map<String, Booking> bookingMap = new HashMap<>();
    private final SeatLockProvider seatLockProvider;
    NotificationService notificationService;

    private static int bookingCounter = 1;

    public BookingService(SeatLockProvider seatLockProvider) {
        this.seatLockProvider = seatLockProvider;
        this.notificationService = new NotificationService();
    }


    /**
     * Step 1: User selects seats → system locks them temporarily.
     */
    public void lockSeats(User user, List<Seat> seats) {
        try {
            seatLockProvider.lockSeats(seats, user);
            System.out.println("✅ Locked seats for user: " + user.getName());
        } catch (RuntimeException e) {
            System.out.println("❌ Failed to lock seats: " + e.getMessage());
        }
    }

    /**
     * Step 2: Booking confirmation → only allowed if seats were locked by the same user.
     */
    public void confirmBooking(User user, Show show, List<Seat> seats) {
        for (Seat seat : seats) {
            // Check if seat is locked by the same user
            if (!seatLockProvider.isSeatLockedByUser(seat, user)) {
                System.out.println("❌ Seat not locked by user: " + user.getName() + "  " + seat.getSeatId());
                return;
            }
        }
        // Proceed with booking
        for (Seat seat : seats) {
            seat.bookSeat(); // mark as booked
        }
        String bookingId = "BKG" + bookingCounter++;
        Booking booking = new Booking(bookingId, user, show, seats);
        bookingMap.put(bookingId, booking);
        user.addBooking(booking);

        seatLockProvider.unlockSeats(seats, user); //
        System.out.println("✅ Booking confirmed! ID: " + bookingId + " User is " + user.getName());
        show.printAllDetails();
        // 🔔 Notify
        notificationService.notifyObservers(booking);
    }

    /**
     * Cancels the booking with given ID.
     *
     * @param bookingId id of booking to cancel
     * @return true if cancelled, false if not found
     */
    public boolean cancelBooking(String bookingId) {
        Booking booking = bookingMap.get(bookingId);
        if (booking == null) {
            System.out.println("❌ Booking not found: " + bookingId);
            return false;
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            System.out.println("⚠️ Booking already cancelled.");
            return false;
        }

        booking.cancelBooking();
        System.out.println("✅ Booking cancelled successfully: " + bookingId);
        return true;
    }

    /**
     * Displays available seats for a given show.
     */
    public void showAvailableSeats(Show show) {
        List<Seat> available = show.getAvailableSeats();
        System.out.println("Available seats for Show ID " + show.getShowId() + ":");
        if (available.isEmpty()) {
            System.out.println("Not seats");
            return;
        }
        for (Seat seat : available) {
            System.out.println("- Seat " + seat.getSeatId() + " (" + seat.getType() + ")");
        }
    }

}
