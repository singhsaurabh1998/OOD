package Observer;

import model.Booking;

public interface BookingObserver {
    void onBookingConfirmed(Booking booking);
}

