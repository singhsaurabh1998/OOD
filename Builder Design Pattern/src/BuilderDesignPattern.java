
/*✅ Why this is great?

No need for 10 constructors.

Clear & readable.

Object is immutable (all fields are final).

Flexible: you only set what you need.*/
class User {
    // fields
    private final String name;     // required
    private final int age;         // optional
    private final String email;    // optional
    private final String phone;    // optional
    private final String address;  // optional

    // private constructor so object can't be created directly
    private User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.email = builder.email;
        this.phone = builder.phone;
        this.address = builder.address;
    }

    // static nested Builder class
    public static class Builder {
        private final String name;  // required
        private int age;
        private String email;
        private String phone;
        private String address;

        public Builder(String name) {
            this.name = name;
        }

        public Builder age(int age) {
            this.age = age;
            return this; // return builder for chaining
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', age=" + age +
                ", email='" + email + "', phone='" + phone +
                "', address='" + address + "'}";
    }
}
public class BuilderDesignPattern {
    public static void main(String[] args) {
        User user1 = new User.Builder("Saurabh")
                .age(25)
                .email("saurabh@gmail.com")
                .build();

        User user2 = new User.Builder("Amit")
                .phone("1234567890")
                .address("Delhi")
                .email("aa")
                .build();

        System.out.println(user1);
        System.out.println(user2);
    }
}
