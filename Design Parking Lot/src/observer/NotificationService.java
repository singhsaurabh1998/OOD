package observer;

import enums.EventType;
import model.Ticket;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationService implements Subject {
    // Use CopyOnWriteArrayList for safe iteration during notifications
    private final List<Observer> observers = new CopyOnWriteArrayList<>();

    @Override
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    @Override
    public void notifyObservers(EventType event, Ticket ticket) {
        for (Observer obs : observers) {
            obs.notify(event, ticket);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
