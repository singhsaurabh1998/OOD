// User class: represents a user in the system (also acts as an observer)
public class User {
    private String id;
    private String name;
    private String email;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() { return id; }

    // Called when there's a notification from the group
    public void notify(String message) {
        System.out.println("[Notification to " + name + "] " + message);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
