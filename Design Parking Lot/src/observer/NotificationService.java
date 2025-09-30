package observer;
import model.Ticket;
import java.util.ArrayList;
import java.util.List;

public class NotificationService implements Subject {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    public void notifyObservers(String event, Ticket ticket) {
        for (Observer obs : observers) {
            obs.notify(event, ticket);
        }
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
