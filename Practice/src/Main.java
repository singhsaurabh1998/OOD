import java.util.ArrayList;
import java.util.List;

// Observer interface
interface YouTube {
    void update(String msg);
}

// A simple notification (base component)
class SimpleNotification implements YouTube {
    @Override
    public void update(String msg) {
        System.out.println("Plain Notification: " + msg);
    }
}

// Decorator base class
abstract class NotificationDecorator implements YouTube {
    protected YouTube youTube;

    NotificationDecorator(YouTube youTube) {
        this.youTube = youTube;
    }

    @Override
    public void update(String msg) {
        youTube.update(msg); // delegate to wrapped object
    }
}

// Add timestamp decoration
class NotificationWithTimeStamp extends NotificationDecorator {
    NotificationWithTimeStamp(YouTube youTube) {
        super(youTube);
    }

    @Override
    public void update(String msg) {
        youTube.update("12:04 " + msg);  // enhance and forward
    }
}

// Add signature decoration
class NotificationWithSign extends NotificationDecorator {
    NotificationWithSign(YouTube youTube) {
        super(youTube);
    }

    @Override
    public void update(String msg) {
        youTube.update("Saurabh :: " + msg); // enhance and forward
    }
}

// Observer manager
interface YouTubeObserver {
    void addYoutubeObserver(YouTube youTube);
    void removeYoutubeObserver(YouTube youTube);
    void notifyYoutubeObserver(String msg);
}

// Subject/Observable
class YoutubeNotificationObservable implements YouTubeObserver {
    private final List<YouTube> youTubeObserverList = new ArrayList<>();

    @Override
    public void addYoutubeObserver(YouTube youTube) {
        youTubeObserverList.add(youTube);
    }

    @Override
    public void removeYoutubeObserver(YouTube youTube) {
        youTubeObserverList.remove(youTube);
    }

    @Override
    public void notifyYoutubeObserver(String msg) {
        for (YouTube youTube : youTubeObserverList) {
            youTube.update(msg);
        }
    }
}

// Other notification channels
class SMS implements YouTube {
    @Override
    public void update(String msg) {
        System.out.println("SMS: " + msg);
    }
}

class WhatsApp implements YouTube {
    @Override
    public void update(String msg) {
        System.out.println("WhatsApp: " + msg);
    }
}

// Main testing
public class Main {
    public static void main(String[] args) {
        YoutubeNotificationObservable observable = new YoutubeNotificationObservable();

        // Decorate a notification
        YouTube decorated = new SimpleNotification();
        decorated = new NotificationWithSign(decorated);
        decorated = new NotificationWithTimeStamp(decorated);

        // Add different observers
        observable.addYoutubeObserver(new SMS());
        observable.addYoutubeObserver(new WhatsApp());
        observable.addYoutubeObserver(decorated);

        // Notify everyone
        observable.notifyYoutubeObserver("New Video Uploaded!");
    }
}
