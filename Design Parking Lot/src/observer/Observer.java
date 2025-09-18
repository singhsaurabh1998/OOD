package observer;

import model.Ticket;

public interface Observer {
    void notify(String event, Ticket ticket);
}

