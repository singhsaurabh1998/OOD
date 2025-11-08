package repository;

import model.Rental;
import java.util.List;
import java.util.Optional;
//hold rental records
public interface RentalRepository {
    void save(Rental rental);
    Optional<Rental> findById(String rentalId);
    List<Rental> getAll();
}

