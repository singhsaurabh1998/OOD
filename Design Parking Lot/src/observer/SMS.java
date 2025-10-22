package observer;

import enums.EventType;
import model.Ticket;

public class SMS implements Observer {
    @Override
    public void notify(EventType event, Ticket ticket) {
        System.out.println("[SMS] " + event + " TicketId=" + ticket.getTicketId());
    }
}
