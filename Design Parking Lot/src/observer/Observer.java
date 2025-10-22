package observer;

import enums.EventType;
import model.Ticket;

public interface Observer {
    // Observer contract for receiving parking lot events
    void notify(EventType event, Ticket ticket);
}
