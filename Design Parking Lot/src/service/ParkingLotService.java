package service;

import enums.EventType;
import model.ParkingFloor;
import model.ParkingSpot;
import model.Ticket;
import model.Vehicle;
import observer.NotificationService;
import observer.Observer;
import repository.TicketRepository;
import strategy.IPricingStrategy;
import strategy.SpotAllocationStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private final NotificationService notificationService;
    private final Map<Integer, ParkingSpot> spotIndex = new ConcurrentHashMap<>();
    private final IPricingStrategy pricingStrategy;

    public ParkingLotService(List<ParkingFloor> initialFloors, SpotAllocationStrategy strategy, IPricingStrategy pricingStrategy) {
        floors.addAll(initialFloors);
        this.allocator = strategy;
        this.ticketRepo = new TicketRepository();
        this.notificationService = new NotificationService();
        this.pricingStrategy = pricingStrategy;
        indexSpots();
    }

    private void indexSpots() {
        for (ParkingFloor floor : floors)
            for (ParkingSpot spot : floor.getSpots())
                spotIndex.put(spot.getId(), spot);
    }

    public void addObserver(Observer obs) {
        notificationService.addObserver(obs);
    }

    public void removeObserver(Observer obs) {
        notificationService.removeObserver(obs);
    }

    public Ticket parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = allocator.allocateSpot(floors, vehicle);
        if (spot == null) {
            System.out.println("No spot available for " + vehicle.getNumber());
            return null;
        }
        // Try to assign the spot to the vehicle Synchronized
        boolean assigned = spot.assignSpot(vehicle);
        if (!assigned) {
            System.out.println("Spot assignment failed for " + vehicle.getNumber());
            return null;
        }
        System.out.println("Vehicle " + vehicle.getNumber() + " parked at spot " + spot.getId() + " at floor " + spot.getFloorId());
        Ticket ticket = new Ticket(vehicle.getNumber(), spot.getId(), spot.getFloorId());
        ticketRepo.save(ticket);
        notificationService.notifyObservers(EventType.PARKED, ticket);
        return ticket;
    }

    public double unparkVehicle(Ticket ticket) {
        if (ticket == null) {
            System.out.println("Ticket is not valid !");
            return 0;
        }
        Ticket stored = ticketRepo.find(ticket.getTicketId());
        if (stored != null) {
            ParkingSpot spot = spotIndex.get(stored.getSpotId());
            if (spot != null) {
                spot.vacate();
            }
            notificationService.notifyObservers(EventType.UNPARKED, stored);
            ticketRepo.deleteTicket(ticket.getTicketId());
        } else {
            System.out.println("This Id is not in our DB");
            return 0;
        }
        // Calculate parking fee
        double totalfee = pricingStrategy.calculateFee(ticket.getEntryTime(), LocalDateTime.now());
        return totalfee;
    }

    public void showAllTickets() {
        System.out.println("All active tickets: " + ticketRepo.showAllActiveTickets());
    }
}
