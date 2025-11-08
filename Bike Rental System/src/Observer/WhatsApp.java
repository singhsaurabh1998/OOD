package Observer;

public class WhatsApp implements Observer{
    @Override
    public void update(String message) {
        System.out.println("WhatsApp Notification: " + message);

    }
}
