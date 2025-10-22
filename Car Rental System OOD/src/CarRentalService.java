import payment.PaymentProcessor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CarRentalService {
    private static final CarRentalService instance = null;
    private final Map<String, Car> cars; //number plate -> Car
    //reservationId -> Reservation,
    // the reservations map in CarRentalService acts like an in-memory database.
    //  If a reservation exists in the map, it means that the car is reserved for the specified date range.
    private final Map<String, Reservation> reservations;

    private PaymentProcessor paymentProcessor;

    private CarRentalService() {
        cars = new ConcurrentHashMap<>();
        reservations = new ConcurrentHashMap<>();
    }
    public void setPaymentProcessor(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }
    public static CarRentalService getInstance() {
        if (instance == null)
            return new CarRentalService();
        return instance;
    }

    public void addCar(Car car) {
        cars.put(car.getLicensePlate(), car);
    }

    public void removeCar(String licensePlate) {
        cars.remove(licensePlate);
    }

    /**
     * Search for available cars based on make, model, and date range.
     */
    public List<Car> searchCars(String make, String model, LocalDate startDate, LocalDate endDate) {
        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars.values()) {
            if (car.getMakeCompany().equalsIgnoreCase(make) && car.getModel().equalsIgnoreCase(model) && car.isAvailable()) {
                if (isCarAvailable(car, startDate, endDate)) {
                    availableCars.add(car);
                }
            }
        }
        return availableCars;
    }

    /**
     * Check if a car is available for the given date range.
     *
     */
    private boolean isCarAvailable(Car car, LocalDate startDate, LocalDate endDate) {
        for (Reservation reservation : reservations.values()) {
            if (reservation.getCar().equals(car)) {
                // Check for date overlap
                if (startDate.isBefore(reservation.getEndDate()) && endDate.isAfter(reservation.getStartDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Make a reservation for a car if available.
     * If you remove synchronized from the makeReservation method, multiple threads could execute it at the
     * same time. This can cause:
     * Double booking: Two threads might reserve the same car for overlapping dates.
     * Inconsistent state: The reservations map and car availability flag could be updated incorrectly,
     * leading to data corruption.
     * Using synchronized ensures thread safety for reservation creation in a concurrent environment.
     */
    public synchronized Reservation makeReservation(Customer customer, Car car, LocalDate startDate, LocalDate endDate) {
        if (isCarAvailable(car, startDate, endDate)) {
            String reservationId = generateReservationId();
            Reservation reservation = new Reservation(reservationId, customer, car, startDate, endDate);
            reservations.put(reservationId, reservation);
            car.setAvailable(false);
            return reservation;
        }
        return null;
    }

    public synchronized void cancelReservation(String reservationId) {
        Reservation reservation = reservations.remove(reservationId);
        if (reservation != null) {
            reservation.getCar().setAvailable(true);
        }
    }

    /**
     * Process payment for a reservation.
     */
    public boolean processPayment(Reservation reservation) {
        return paymentProcessor.processPayment(reservation.getTotalPrice());
    }

    /**
     * Generate a unique reservation ID.
     */
    private String generateReservationId() {
        return "RES" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
