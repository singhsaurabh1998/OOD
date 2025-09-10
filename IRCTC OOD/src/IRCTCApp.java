import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Station {
    private final String code;
    private final String name;

    public Station(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

enum TICKET_ENUM {
    GENERAL,
    AC,
    TATKAL
}

class Seat {
    private final int seatNumber;
    private boolean isBooked;

    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.isBooked = false;
    }

    public boolean isAvailable() {
        return !isBooked;
    }

    public void book() {
        this.isBooked = true;
    }

    public int getSeatNumber() {
        return seatNumber;
    }
}

class Train {
    private final String trainNumber;
    private final String name;
    private final List<Station> route;
    private final List<Seat> seats;

    public Train(String trainNumber, String name, List<Station> route, int totalSeats) {
        this.trainNumber = trainNumber;
        this.name = name;
        this.route = route;
        this.seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            seats.add(new Seat(i));
        }
    }

    public List<Station> getRoute() {
        return route;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getTrainName() {
        return name;
    }

    public boolean passesThrough(Station from, Station to) {
        int startIndex = route.indexOf(from);
        int endIndex = route.indexOf(to);
        return (startIndex != -1 && endIndex != -1 && startIndex < endIndex);
    }

    public Seat bookSeat() {
        for (Seat seat : seats) {
            if (seat.isAvailable()) {
                seat.book();
                return seat;
            }
        }
        return null; // no seat available
    }
}

// TicketType hierarchy (Strategy for fare calculation)
interface TicketType {
    int calculateFare(Station from, Station to);
}

class GeneralTicket implements TicketType {

    @Override
    public int calculateFare(Station from, Station to) {
        return 500;
    }
}

class ACTicket implements TicketType {

    @Override
    public int calculateFare(Station from, Station to) {
        return 600;
    }
}

class TicketFactory {
    public static TicketType createTicket(TICKET_ENUM type) {
        switch (type) {
            case AC:
                return new ACTicket();
            case GENERAL:
                return new GeneralTicket();
            case null, default:
                System.out.println("Not found ticket type");
                return null;
        }
    }
}

class Ticket {
    private static int idCounter = 1;
    private final int ticketId;
    private final Train train;
    private final Station from;
    private final Station to;
    private final Seat seat;
    private final int fare;

    public Ticket(Train train, Station from, Station to, Seat seat, TicketType ticketTypeObj) {
        this.ticketId = idCounter++;
        this.train = train;
        this.from = from;
        this.to = to;
        this.seat = seat;
        this.fare = ticketTypeObj.calculateFare(from, to);
    }

    public String showDetails() {
        return "Ticket#" + ticketId + " Train:" + train.getTrainNumber() +
                " From:" + from.getName() + " To:" + to.getName() +
                " Seat:" + seat.getSeatNumber() +
                " Fare:" + fare;
    }
}

interface TrainSearchStrategy {
    List<Train> searchTrain(Station from, Station to, List<Train> trains);
}

// Fastest train search
class FastestTrainStrategy implements TrainSearchStrategy {
    @Override
    public List<Train> searchTrain(Station from, Station to, List<Train> allTrains) {
        System.out.println("Fastest train logic has been called!");
        List<Train> result = new ArrayList<>();
        for (Train train : allTrains) {
            if (train.passesThrough(from, to)) {
                result.add(train);
            }
        }
        return result;
    }
}

// Cheapest train search
class CheapestTrainStrategy implements TrainSearchStrategy {
    @Override
    public List<Train> searchTrain(Station from, Station to, List<Train> allTrains) {
        System.out.println("Cheapest train logic has been called!");
        List<Train> result = new ArrayList<>();
        for (Train train : allTrains) {
            if (train.passesThrough(from, to)) {
                result.add(train);
            }
        }
        return result;

    }
}

class IRCTCSystem {
    private final List<Train> trains = new ArrayList<>();
    private static final IRCTCSystem instance = new IRCTCSystem();
    private NotificationService notificationService;

    private IRCTCSystem() {

    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public static IRCTCSystem getInstance() {
        return instance;
    }

    public void addTrain(Train train) {
        trains.add(train);
    }

    //strategy pattern can be added to search the trains
    public List<Train> searchTrains(Station from, Station to, TrainSearchStrategy searchStrategy) {
        List<Train> result = searchStrategy.searchTrain(from, to, trains);

        return result;
    }

    public Ticket bookTicket(Train train, Station from, Station to, TICKET_ENUM type) {
        Seat seat = train.bookSeat();
        if (seat == null) {
            notificationService.notifyObserver("Failed");
            System.out.println("No seats available!");
            return null;
        }
        TicketType ticketTypeObj = TicketFactory.createTicket(type);
        Ticket ticket = new Ticket(train, from, to, seat, ticketTypeObj);
        notificationService.notifyObserver("Success!");
        return ticket;
    }
}

/*############Observer Design Pattern to show notifications to user ######################*/
interface Observer {
    void update(String msg);
}

interface Subject {
    void registerObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObserver(String msg);
}

class SMS implements Observer {

    @Override
    public void update(String msg) {
        System.out.println("Hey Bro ! This is sms notification : " + msg);
    }
}

class Email implements Observer {
    @Override
    public void update(String msg) {
        System.out.println("Hey Bro ! This is Email notification for your ticket : " + msg);
    }
}

class NotificationService implements Subject {
    List<Observer> observerList;

    NotificationService() {
        observerList = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        observerList.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observerList.remove(o);
    }

    @Override
    public void notifyObserver(String msg) {
        for (Observer o : observerList) {
            o.update(msg);
        }
    }
}

public class IRCTCApp {
    public static void main(String[] args) {
        Station delhi = new Station("NDLS", "Delhi");
        Station bareilly = new Station("Bek", "Bareilly");
        Station spn = new Station("SPN", "SHahajahanpur");
        Station kanpur = new Station("CNB", "Kanpur");
        Station lucknow = new Station("LKO", "Lucknow");

        List<Station> route1 = Arrays.asList(delhi, kanpur, lucknow);
        List<Station> route2 = Arrays.asList(delhi, bareilly, spn, lucknow, kanpur);
        Train train1 = new Train("12345", "Shatabdi Express", route1, 3);
        Train train2 = new Train("16094", "ShramJivi Exp", route2, 10);

        IRCTCSystem system = IRCTCSystem.getInstance();
        system.addTrain(train1);
        system.addTrain(train2);
        NotificationService notificationService = new NotificationService();
        Observer sms = new SMS();
        Observer email = new Email();
        notificationService.registerObserver(sms);
        notificationService.registerObserver(email);
        system.setNotificationService(notificationService);
        // Search trains
        List<Train> searchResult = system.searchTrains(delhi, kanpur,new FastestTrainStrategy());
        System.out.println("Trains from Delhi to Lucknow:");
        for (Train t : searchResult) {
            System.out.println(t.getTrainNumber() + " - " + t.getTrainName() + " stations");
        }

        // Book ticket
        Ticket ticket = system.bookTicket(searchResult.get(0), delhi, lucknow, TICKET_ENUM.AC);
        if (ticket != null) {
            System.out.println("Booking Successful: " + ticket.showDetails());
        }

    }
}
