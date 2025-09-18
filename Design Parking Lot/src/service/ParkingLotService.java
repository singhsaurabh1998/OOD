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

        boolean assigned = spot.assign(vehicle);
        if (!assigned) {
            return null;
        }

        Ticket ticket = new Ticket(vehicle.getNumber(), spot.getId());
        ticketRepo.save(ticket);
        notificationService.notifyAll("PARKED", ticket);

        return ticket;
    }

    public void unparkVehicle(Ticket ticket) {
        if (ticket == null) {
            System.out.println("Ticket is not valid !");
            return;
        }
        String tickedId = ticket.getTicketId();
        if (ticketRepo.find(tickedId) != null) {
            ticketRepo.deleteTicket(tickedId);
        } else
            System.out.println("This Id is not in our DB");
    }

    public void showAllTickets() {
        System.out.println("All active tickets: " + ticketRepo.showAllActiveTickets());
    }

}

