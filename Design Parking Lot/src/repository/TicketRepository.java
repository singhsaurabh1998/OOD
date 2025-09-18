package repository;

import model.Ticket;

import java.util.HashMap;
import java.util.Map;

public class TicketRepository {
    private final Map<String, Ticket> store = new HashMap<>();//id,ticket

    public void save(Ticket ticket) {
        store.put(ticket.getTicketId(), ticket);
    }

    public Ticket find(String ticketId) {
        return store.get(ticketId);
    }

    public Map<String, Ticket> getSavedTickets() {
        return store;
    }

    public void deleteTicket(String ticketId) {
        store.remove(ticketId);
    }
    public Map<String, Ticket> showAllActiveTickets(){
        return store;
    }
}
