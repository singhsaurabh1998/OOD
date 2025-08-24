import java.util.*;

public class BookingService {
    private final Map<String, Booking> bookingMap = new HashMap<>();
    private final SeatLockProvider seatLockProvider;
    private final List<BookingObserver> observers = new ArrayList<>();

    private int bookingCounter = 1;

    public BookingService(SeatLockProvider seatLockProvider) {
        this.seatLockProvider = seatLockProvider;
    }
    // Register observers
    public void registerObserver(BookingObserver observer) {
        observers.add(observer);
    }

    // Notify all observers after booking confirmation
    private void notifyObservers(Booking booking) {
        for (BookingObserver observer : observers) {
            observer.onBookingConfirmed(booking);
        }
    }
    /**
     * Step 1: User selects seats → system locks them temporarily.
     */
    public void lockSeats(User user, Show show, List<Seat> seats) {
        try {
            seatLockProvider.lockSeats(show, seats, user);
            System.out.println("✅ Locked seats for user: " + user.getName());
        } catch (RuntimeException e) {
            System.out.println("❌ Failed to lock seats: " + e.getMessage());
        }
    }

    /**
     * Books seats for a user for a given show.
     *
     * @param user    the user trying to book
     * @param show    the show being booked
     * @param seatIds the list of seat IDs user wants to book
     * @return Booking object or null if booking fails
     */
//    public Booking bookSeats(User user, Show show, List<String> seatIds) {
//
//        List<Seat> selectedSeats = new ArrayList<>();
//        for (String id : seatIds) {
//            Seat valid = null;
//            for (Seat seat : show.getSeats()) {
//                if (seat.getSeatId().equals(id)) {
//                    valid = seat;
//                    break;
//                }
//            }
//            if (valid == null) {
//                System.out.println("Bro this id " + id + " doesnt exists , can't proceed !");
//                return null;
//            }
//            if (valid.isBooked()) {
//                System.out.println(valid + "is Already Boooked !!");
//                return null;
//            }
//            valid.bookSeat();
//            selectedSeats.add(valid);
//
//        }
//
//        String bookingId = "BKID" + bookingCounter++;
//        Booking booking = new Booking(bookingId, user, show, selectedSeats);
//        user.addBooking(booking);
//        bookingMap.put(bookingId, booking);
//        System.out.println("✅ Booking successful! Booking ID: " + bookingId);
//        return booking;
//    }

    /**
     * Step 2: Booking confirmation → only allowed if seats were locked by the same user.
     */
    public void confirmBooking(User user, Show show, List<Seat> seats) {
        for (Seat seat : seats) {
            if (!seatLockProvider.isSeatLockedByUser(seat, user)) {
                System.out.println("❌ Seat not locked by user: "+ user.getName() +"  "+ seat.getSeatId());
                return ;
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

        seatLockProvider.unlockSeats(seats, user); // unlock after successful booking
        System.out.println("✅ Booking confirmed! ID: " + bookingId+" User is "+user.getName());
        // 🔔 Notify
        notifyObservers(booking);
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
