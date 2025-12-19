import Observer.EmailService;
import Observer.LoyaltyService;
import Observer.NotificationService;
import Observer.SMSService;
import enums.SeatType;
import model.*;
import security.SeatLockProvider;
import services.BookingService;

import java.time.LocalDateTime;
import java.util.List;

public class BookMyShowMain {
    public static void main(String[] args) throws InterruptedException {
        User user1 = new User("id1", "Saurabh", "papa@gmail.com");
        User user2 = new User("id2", "Sunda", "son@gmail.com");
        User user3 = new User("id3", "RK", "rk@gmail.com");

        Movie jawan = new Movie("movie1", "Jawan", "Hindi", 120, List.of("romance", "action"));
        Movie Pathan = new Movie("movie2", "Pathan", "Hindi", 140, List.of("romcom", "action"));

        Seat s1 = new Seat( 1, 1, SeatType.REGULAR);
        Seat s2 = new Seat( 2, 1, SeatType.PREMIUM);
        List<Seat> totalSeats = List.of(s1, s2);

        Screen screen1 = new Screen("screen1", "Audi1");
        Show show = new Show("show1", jawan, LocalDateTime.now().plusHours(1), screen1, totalSeats);
        screen1.addShow(show);

        Theatre theatre1 = new Theatre("T1", "Amba Talkies", "Spn");
        theatre1.addScreen(screen1);

        SeatLockProvider seatLockProvider = new SeatLockProvider(10);

        BookingService bookingService = new BookingService(seatLockProvider);
        NotificationService notificationService = new NotificationService();

        // Register notification observers
        notificationService.addObserver(new EmailService());
        notificationService.addObserver(new SMSService());
        notificationService.addObserver(new LoyaltyService());
        Runnable bookingTask1 = () -> {
            try {
                System.out.println("👤 Saurabh trying to lock seats...");
                bookingService.lockSeats(user1, totalSeats);//try to lock seats first
                Thread.sleep(100); // simulate some delay (e.g., payment)
                bookingService.confirmBooking(user1, show, totalSeats);
            } catch (Exception e) {
                System.out.println("❌ Saurabh booking failed: " + e.getMessage());
            }
        };

        Runnable bookingTask2 = () -> {
            try {
                System.out.println("👤 Sunda trying to lock seats...");
                bookingService.lockSeats(user2, totalSeats);
                Thread.sleep(100); // simulate some delay
                bookingService.confirmBooking(user2, show, totalSeats);
            } catch (Exception e) {
                System.out.println("❌ Sunda booking failed: " + e.getMessage());
            }
        };
        Runnable bookingTask3 = () -> {
            try {
                System.out.println("👤 Rk trying to lock seats...");
                bookingService.lockSeats(user3, totalSeats);
                Thread.sleep(100); // simulate some delay
                bookingService.confirmBooking(user3, show, totalSeats);
            } catch (Exception e) {
                System.out.println("❌ Rk booking failed: " + e.getMessage());
            }
        };

        // Run both in parallel
        Thread t1 = new Thread(bookingTask1);
        Thread t2 = new Thread(bookingTask2);
        Thread t3 = new Thread(bookingTask3);

        t1.start();
        t2.start();
        t3.start();

    }


}