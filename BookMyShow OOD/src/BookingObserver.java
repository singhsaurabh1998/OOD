public interface BookingObserver {
    void onBookingConfirmed(Booking booking);
}

class EmailService implements BookingObserver {
    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("[Email Service] Confirmation email sent to " + booking.getUser().getEmail());
    }
}

class SMSService implements BookingObserver {
    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("[SMS Service] SMS sent to " + booking.getUser().getName());
    }
}

class LoyaltyService implements BookingObserver {
    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("[Loyalty Service] Points added for user: " + booking.getUser().getName());
    }
}
