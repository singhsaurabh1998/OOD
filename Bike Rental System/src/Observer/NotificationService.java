package Observer;

import java.util.ArrayList;
import java.util.List;

public class NotificationService implements Subject {

    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        System.out.println("Notifying observers...");
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
