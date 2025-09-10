import java.util.ArrayList;
import java.util.List;

// Observer interface
interface Observer {
    void update(String message);
}

// Subject interface
interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String message);
}

// Concrete Subject (Notification Service)
class NotificationService implements Subject {
    private final List<Observer> observers;

    public NotificationService() {
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
    }

    // Some trigger for notifications
    public void newUpdate(String message) {
        System.out.println("\nNotificationService: New message = " + message);
        notifyObservers(message);
    }
}

// Concrete Observer 1: SMS
class SMSNotifier implements Observer {
    @Override
    public void update(String message) {
        System.out.println("📱 SMS Notification: " + message);
    }
}

// Concrete Observer 2: Email
class EmailNotifier implements Observer {
    @Override
    public void update(String message) {
        System.out.println("📧 Email Notification: " + message);
    }
}

// Concrete Observer 3: WhatsApp
class WhatsAppNotifier implements Observer {
    @Override
    public void update(String message) {
        System.out.println("💬 WhatsApp Notification: " + message);
    }
}

// Client code
public class ObserverDesignPattern {
    public static void main(String[] args) {
        NotificationService notificationService = new NotificationService();

        Observer sms = new SMSNotifier();
        Observer email = new EmailNotifier();
        Observer whatsapp = new WhatsAppNotifier();

        notificationService.registerObserver(sms);
        notificationService.registerObserver(email);
        notificationService.registerObserver(whatsapp);

        notificationService.newUpdate("Transaction success!");

        // Remove one observer
        notificationService.removeObserver(email);

        notificationService.newUpdate("Server is back online!");
    }
}
