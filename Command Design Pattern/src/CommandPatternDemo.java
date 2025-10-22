// 1. Command Interface
interface Command {
    void execute();
}

// 2. Receiver (Actual worker)
class TV {
    public void turnOn() {
        System.out.println("TV is ON");
    }

    public void turnOff() {
        System.out.println("TV is OFF");
    }
}

// 3. Concrete Commands
class TurnOnCommand implements Command {
    private final TV tv;

    public TurnOnCommand(TV tv) {
        this.tv = tv;
    }

    public void execute() {
        tv.turnOn();
    }
}

class TurnOffCommand implements Command {
    private final TV tv;

    public TurnOffCommand(TV tv) {
        this.tv = tv;
    }

    public void execute() {
        tv.turnOff();
    }
}

// 4. Invoker (Doesn’t know how the TV works)
class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }
}

// 5. Client (User)
public class CommandPatternDemo {
    public static void main(String[] args) {
        TV tv = new TV(); // Receiver

        Command turnOn = new TurnOnCommand(tv);
        Command turnOff = new TurnOffCommand(tv);

        RemoteControl remote = new RemoteControl(); // Invoker

        remote.setCommand(turnOn);
        remote.pressButton(); // TV is ON

        remote.setCommand(turnOff);
        remote.pressButton(); // TV is OFF
    }
}
