package repository;

import model.Rental;
import java.util.*;

public class InMemoryRentalRepository implements RentalRepository {
    private final Map<String, Rental> rentals = new HashMap<>();//rentalId -> Rental

    @Override
    public void save(Rental rental) {
        rentals.put(rental.getRentalId(), rental);//save or update
    }

    @Override
    public Optional<Rental> findById(String rentalId) {
        return Optional.ofNullable(rentals.get(rentalId));//null if not found
    }

    @Override
    public List<Rental> getAll() {
        return new ArrayList<>(rentals.values());
    }
}

