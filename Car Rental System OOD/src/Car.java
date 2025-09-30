import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
public class Car {
    private final String makeCompany;
    private final String model;
    private final int year;
    private final String licensePlate;
    private final double rentalPricePerDay;
    private boolean available;

    public Car(String makeCompany, String model, int year, String licensePlate, double rentalPricePerDay) {
        this.makeCompany = makeCompany;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.rentalPricePerDay = rentalPricePerDay;
        this.available = true;
    }

}
