package observer;

import enums.EventType;
import model.Ticket;

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(EventType event, Ticket ticket);
}
