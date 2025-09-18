package observer;

import model.Ticket;

public class ConsoleLogger implements Observer {
    @Override
    public void notify(String event, Ticket ticket) {
        System.out.println("[EVENT] " + event + " TicketId=" + ticket.getTicketId());
    }
}
