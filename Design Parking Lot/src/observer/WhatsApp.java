package observer;

import model.Ticket;

public class WhatsApp implements Observer {

    @Override
    public void notify(String event, Ticket ticket) {
        System.out.println("WhatsUp : " + "[EVENT] " + event + " TicketId=" + ticket.getTicketId());
    }
}
