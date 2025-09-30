import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private final String userId;
    private final String name;
    private final String email;
    private final String password;
    //any digital wallet systems allow a user to have multiple accounts (
    // e.g., savings, checking, credit, or different currencies) linked to their profile.
    // This provides flexibility and supports various use cases.
    private final List<Account> accounts;

    public User(String id, String name, String email, String password) {
        this.userId = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    @Override
    public String toString() {
        return "User{id='" + userId + "', name='" + name + "'}";
    }
}
