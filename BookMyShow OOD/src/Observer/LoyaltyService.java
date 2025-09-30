package Observer;

import model.Booking;

public class LoyaltyService implements BookingObserver {
    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("[Loyalty Service] Points added for user: " + booking.getUser().getName());
    }
}
