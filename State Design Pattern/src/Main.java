
public class Main {
    public static void main(String[] args) {
        Fan fan = new Fan();
        fan.pressButton(); // Fan is turned ON to LOW speed.
        fan.pressButton(); // Fan is turned to HIGH speed.
        fan.pressButton(); // Fan is turned OFF.
        fan.pressButton(); // Fan is turned ON to LOW speed.
    }
}