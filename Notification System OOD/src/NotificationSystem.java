// Java version of Notification System using Decorator, Observer, Strategy, and Singleton Patterns

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*============================
      Notification & Decorators
=============================*/

interface INotification {
    String getContent();
}

// Concrete Notification: simple text notification.
class SimpleNotification implements INotification {
    private final String text;

    public SimpleNotification(String msg) {
        this.text = msg;
    }

    public String getContent() {
        return text;
    }
}

// Abstract Decorator: wraps a Notification object.
abstract class INotificationDecorator implements INotification {
    protected INotification notification;

    public INotificationDecorator(INotification n) {
        this.notification = n;
    }
}

// Decorator to add a timestamp to the content.
class TimestampDecorator extends INotificationDecorator {
    public TimestampDecorator(INotification n) {
        super(n);
    }

    public String getContent() {
        return LocalDateTime.now() + notification.getContent();
    }
}

// Decorator to append a signature to the content.
class SignatureDecorator extends INotificationDecorator {
    private final String signature;

    public SignatureDecorator(INotification n, String sig) {
        super(n);
        this.signature = sig;
    }

    public String getContent() {
        return notification.getContent() + "\n-- " + signature + "\n\n";
    }
}

/*============================
  Observer Pattern Components
=============================*/

interface IObserver {
    void update();
}

interface Subject {
    void addObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void notifyObservers();
}

class NotificationObservable implements Subject {
    private final List<IObserver> observers = new ArrayList<>();//store all the observers
    private INotification currentNotification;

    public void addObserver(IObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(IObserver obs) {
        observers.remove(obs);
    }

    public void notifyObservers() {
        for (IObserver observer : observers) {
            observer.update();
        }
    }

    public void setNotification(INotification notification) {
        this.currentNotification = notification;
        notifyObservers();
    }

    public String getNotificationContent() {
        return currentNotification.getContent();
    }
}

// Concrete Observer 2
class NotificationEngine implements IObserver {
    private final NotificationObservable notificationObservable;
    private final List<INotificationStrategy> notificationStrategies = new ArrayList<>();

    public NotificationEngine(NotificationObservable observable) {
        this.notificationObservable = observable;
    }

    public void addNotificationStrategy(INotificationStrategy strategy) {
        this.notificationStrategies.add(strategy);
    }

    public void update() {
        String msg = notificationObservable.getNotificationContent();
        for (INotificationStrategy strategy : notificationStrategies) {
            strategy.sendNotification(msg);
        }
    }
}

// Concrete Observer 1
class Logger implements IObserver {
    private final NotificationObservable notificationObservable;

    public Logger(NotificationObservable observable) {
        this.notificationObservable = observable;
    }

    public void update() {
        System.out.println("Logging New Notification : \n" + notificationObservable.getNotificationContent());
    }
}

/*============================
  Strategy Pattern Components (Concrete Observer 2)
=============================*/

interface INotificationStrategy {
    void sendNotification(String content);
}

class EmailStrategy implements INotificationStrategy {
    private final String emailId;

    public EmailStrategy(String emailId) {
        this.emailId = emailId;
    }

    public void sendNotification(String content) {
        System.out.println("Sending email Notification to: " + emailId + "\n" + content);
    }
}

class SMSStrategy implements INotificationStrategy {
    private final String mobileNumber;

    public SMSStrategy(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void sendNotification(String content) {
        System.out.println("Sending SMS Notification to: " + mobileNumber + "\n" + content);
    }
}

class PopUpStrategy implements INotificationStrategy {
    public void sendNotification(String content) {
        System.out.println("Sending Popup Notification: \n" + content);
    }
}



/*============================
       NotificationService
=============================*/

// The NotificationService manages notifications. It keeps track of notifications.
// Any client code will interact with this service.

// Singleton class
class NotificationService {
    private final NotificationObservable observableService;
    private static NotificationService instance;
    private final List<INotification> notifications = new ArrayList<>(); //just for tracking purpose

    private NotificationService() {
        observableService = new NotificationObservable();
    }

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public NotificationObservable getObservable() {
        return observableService;
    }

    public void sendNotification(INotification notification) {
        notifications.add(notification);
        observableService.setNotification(notification);
    }
}

public class NotificationSystem {
    public static void main(String[] args) {

        // Create NotificationService.
        NotificationService notificationService = NotificationService.getInstance();

        // Get Observable
        NotificationObservable notificationObservable = notificationService.getObservable();

        // Create Logger Observer
        Logger logger = new Logger(notificationObservable);
        // Create NotificationEngine observers.
        NotificationEngine notificationEngine = new NotificationEngine(notificationObservable);

        notificationEngine.addNotificationStrategy(new EmailStrategy("random.person@gmail.com"));
        notificationEngine.addNotificationStrategy(new SMSStrategy("+91 9876543210"));
        notificationEngine.addNotificationStrategy(new PopUpStrategy());

        // Attach these observers.
        notificationObservable.addObserver(logger);
        notificationObservable.addObserver(notificationEngine);

        // Create a notification with decorators.
        INotification notification = new SimpleNotification("Your order has been shipped!");
        notification = new TimestampDecorator(notification);
        notification = new SignatureDecorator(notification, "Customer Care - SignatureDecorator");

        notificationService.sendNotification(notification);
    }
}
