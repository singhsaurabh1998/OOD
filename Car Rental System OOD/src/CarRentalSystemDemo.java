import payment.PayPalPaymentProcessor;

import java.time.LocalDate;
import java.util.List;

public class CarRentalSystemDemo {
    public static void main(String[] args) {
        CarRentalService service = CarRentalService.getInstance();
        service.setPaymentProcessor(new PayPalPaymentProcessor());
        Car car1 = new Car("Toyota", "Camry", 2020, "ABC123", 50.0);
        Car car2 = new Car("Honda", "Civic", 2019, "XYZ789", 45.0);
        service.addCar(car1);
        service.addCar(car2);

        Customer customerSaurabh = new Customer("Saurabh Singh", "papa@gmail.com", "UP123456");
        Customer customerSunda = new Customer("Sundawa", "kaka@gmail.com", "RJ123456");
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);

        //we can use strategy pattern to implement different search strategies
        List<Car> availableCars = service.searchCars("Toyota", "Camry", startDate, endDate);
        System.out.println(availableCars);

        Reservation reservation1 = service.makeReservation(customerSaurabh, car1, startDate, endDate);
        Reservation reservation2 = service.makeReservation(customerSunda, car1, startDate, endDate);

        if(reservation1!=null){
            boolean paymentStatus = service.processPayment(reservation1);
            if(paymentStatus){
                System.out.println("Reservation1 successful. Reservation ID: " + reservation1.getReservationId());
            } else {
                System.out.println("Payment failed. Reservation1 canceled.");
                service.cancelReservation(reservation1.getReservationId());
            }
        }
        if(reservation2!=null){
            boolean paymentStatus = service.processPayment(reservation2);
            if(paymentStatus){
                System.out.println("Reservation2 successful. Reservation ID: " + reservation2.getReservationId());
            } else {
                System.out.println("Payment failed. Reservation2 canceled.");
                service.cancelReservation(reservation2.getReservationId());
            }
        }
        else
            System.out.println("Selected car is not available for the given dates.");
    }

}