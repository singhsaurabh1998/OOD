package Observer;

import model.Booking;

import java.util.ArrayList;
import java.util.List;

public class NotificationService implements BookingSubject {

    List<BookingObserver> observers;

    public NotificationService() {
        observers = new ArrayList<>();
    }

    @Override
    public void addObserver(BookingObserver observer) {
        // Register observers
        observers.add(observer);
    }

    @Override
    public void removeObserver(BookingObserver observer) {
        observers.remove(observer);
    }
    // Notify all observers after booking confirmation
    @Override
    public void notifyObservers(Booking booking) {
        for (BookingObserver observer : observers) {
            observer.onBookingConfirmed(booking);
        }
    }
}
