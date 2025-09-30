package Observer;

import model.Booking;

public class EmailService implements BookingObserver {
    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("[Email Service] Confirmation email sent to " + booking.getUser().getEmail());
    }
}
