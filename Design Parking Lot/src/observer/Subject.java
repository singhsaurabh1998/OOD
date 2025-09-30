package observer;

import model.Ticket;

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String event, Ticket ticket);
}
