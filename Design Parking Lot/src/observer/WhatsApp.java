package observer;

import enums.EventType;
import model.Ticket;

public class WhatsApp implements Observer {
    @Override
    public void notify(EventType event, Ticket ticket) {
        System.out.println("[WhatsUp] : " + event + " TicketId=" + ticket.getTicketId());
    }
}
