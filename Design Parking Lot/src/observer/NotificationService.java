package observer;

import model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    public void notifyAll(String event, Ticket ticket) {
        for (Observer obs : observers) {
            obs.notify(event, ticket);
        }
    }
}
