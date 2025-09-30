package Observer;

import model.Booking;

public class SMSService implements BookingObserver {
    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("[SMS Service] SMS sent to " + booking.getUser().getName());
    }
}
