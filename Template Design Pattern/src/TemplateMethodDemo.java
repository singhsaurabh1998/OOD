abstract class NotificationSender {

    // Template method
    public final void sendNotification(String recipient, String message) {
        if (!validate(recipient)) {
            System.out.println("Validation failed.");
            return;
        }

        composeMessage(message);
        send(recipient);
        log();
    }

    protected abstract boolean validate(String recipient);
    protected abstract void composeMessage(String message);
    protected abstract void send(String recipient);

    protected void log() {
        System.out.println("Notification logged.");
    }
}

// --- Email ---
class EmailNotification extends NotificationSender {
    private String composedMessage;
    @Override
    protected boolean validate(String recipient) {
        return recipient.contains("@");
    }

    @Override
    protected void composeMessage(String message) {
        this.composedMessage = "Email Content: " + message;
    }

    @Override
    protected void send(String recipient) {
        System.out.println("Sending EMAIL to " + recipient + ": " + composedMessage);
    }
}

// --- SMS ---
class SMSNotification extends NotificationSender {
    private String composedMessage;

    @Override
    protected boolean validate(String recipient) {
        return recipient.matches("\\d{10}");
    }

    @Override
    protected void composeMessage(String message) {
        this.composedMessage = "SMS Content: " + message;
    }

    @Override
    protected void send(String recipient) {
        System.out.println("Sending SMS to " + recipient + ": " + composedMessage);
    }
}

// --- Push ---
class PushNotification extends NotificationSender {
    private String composedMessage;

    @Override
    protected boolean validate(String recipient) {
        return recipient.startsWith("device_");
    }

    @Override
    protected void composeMessage(String message) {
        this.composedMessage = "Push Alert: " + message;
    }

    @Override
    protected void send(String recipient) {
        System.out.println("Sending PUSH to " + recipient + ": " + composedMessage);
    }
}


class SlackNotification extends NotificationSender{
    private String composedMessage;
    @Override
    protected boolean validate(String recipient) {
        return recipient.contains("salesforce");
    }

    @Override
    protected void composeMessage(String message) {
        this.composedMessage = "This is slack :";
    }

    @Override
    protected void send(String recipient) {
        System.out.println("Sending Slack message to "+recipient+" "+composedMessage);
    }
}
// --- Main ---
public class TemplateMethodDemo {
    public static void main(String[] args) {
        NotificationSender email = new EmailNotification();
        NotificationSender sms = new SMSNotification();
        NotificationSender push = new PushNotification();
        NotificationSender slack = new SlackNotification();

        email.sendNotification("user@example.com", "Welcome to our service!");
        sms.sendNotification("9876543210", "Your OTP is 123456");
        push.sendNotification("device_abc123", "You have a new message");
        slack.sendNotification("salesforce", "You have a slack message");
    }
}