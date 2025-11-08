package Observer;

public class SMS implements Observer{
    @Override
    public void update(String message) {
        System.out.println("SMS Notification: " + message);
    }
}
