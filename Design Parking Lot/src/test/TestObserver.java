package test;

import enums.EventType;
import model.Ticket;
import observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class TestObserver implements Observer {
    private final List<EventType> events = new ArrayList<>();
    private final List<String> ticketIds = new ArrayList<>();

    @Override
    public void notify(EventType event, Ticket ticket) {
        events.add(event);
        ticketIds.add(ticket.getTicketId());
    }

    public List<EventType> getEvents() { return events; }
    public List<String> getTicketIds() { return ticketIds; }
}

