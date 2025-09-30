package repository;

import model.Ticket;

import java.util.HashMap;
import java.util.Map;

/**
 * TicketRepository is responsible for managing parking tickets.
 * It provides methods to save, find, and delete tickets.
 * It uses an in-memory HashMap to store tickets, where the key is the ticket ID and the value is the Ticket object.
 */
public class TicketRepository {
    private final Map<String, Ticket> store = new HashMap<>();//id,ticket

    public void save(Ticket ticket) {
        store.put(ticket.getTicketId(), ticket);
    }

    public Ticket find(String ticketId) {
        return store.get(ticketId);
    }

    public void deleteTicket(String ticketId) {
        store.remove(ticketId);
    }

    public Map<String, Ticket> showAllActiveTickets() {
        return store;
    }
}
