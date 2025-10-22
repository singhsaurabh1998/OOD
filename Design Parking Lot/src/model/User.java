package model;

public class User {
    private final String userId;
    private final String name;
    private final String email;
    private final Vehicle vehicle;

    public User(String userId, String name, String email, Vehicle vehicle) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.vehicle = vehicle;
    }

    public String getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
}
