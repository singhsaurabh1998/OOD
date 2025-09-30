package service;

import model.ParkingFloor;
import model.ParkingSpot;
import model.Ticket;
import model.Vehicle;
import observer.NotificationService;
import observer.Observer;
import repository.TicketRepository;
import strategy.SpotAllocationStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * ParkingLotService manages parking and unparking of vehicles,
 * ticket generation, and observer notifications.
 * It uses a SpotAllocationStrategy to allocate parking spots.
 * It maintains a list of parking floors and a repository for tickets.
 * It provides methods to park and unpark vehicles, add observers,
 * and display all active tickets.
 * This class is the main service layer for parking lot operations.
 */
public class ParkingLotService {
    private final List<ParkingFloor> floors = new ArrayList<>();
    private final SpotAllocationStrategy allocator;
    private final TicketRepository ticketRepo;
    private final NotificationService notificationService = new NotificationService();

    public ParkingLotService(List<ParkingFloor> initialFloors, SpotAllocationStrategy strategy) {
        floors.addAll(initialFloors);
        this.allocator = strategy;
        this.ticketRepo = new TicketRepository();
    }

    public void addObserver(Observer obs) {
        notificationService.addObserver(obs);
    }

    public Ticket parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = allocator.allocateSpot(floors, vehicle);

        if (spot == null) {
            System.out.println("No spot available for " + vehicle.getNumber());
            return null;
        }

        boolean assigned = spot.assign(vehicle); //synchronised call
        if (!assigned) {
            System.out.println("No slots left ");
            return null;
        }

        Ticket ticket = new Ticket(vehicle.getNumber(), spot.getId());
        ticketRepo.save(ticket);
        notificationService.notifyObservers("PARKED", ticket);

        return ticket;
    }

    public void unparkVehicle(Ticket ticket) {
        if (ticket == null) {
            System.out.println("Ticket is not valid !");
            return;
        }
        String tickedId = ticket.getTicketId();
        if (ticketRepo.find(tickedId) != null) {
            ticketRepo.deleteTicket(tickedId);//delete from DB
        } else
            System.out.println("This Id is not in our DB");
    }

    public void showAllTickets() {
        System.out.println("All active tickets: " + ticketRepo.showAllActiveTickets());
    }

}

