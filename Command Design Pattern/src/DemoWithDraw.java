import java.util.Stack;

// 1. Receiver
class Account {
    private int balance = 0;

    public void deposit(int amount) {
        balance += amount;
        System.out.println("Deposited: " + amount);
    }

    public void withdraw(int amount) {
        balance -= amount;
        System.out.println("Withdrawn: " + amount);
    }

    public int getBalance() {
        return balance;
    }
}

// 2. Command interface
interface ICommand {
    void execute();
    void undo();
}

// 3. Deposit command
class DepositCommand implements ICommand {
    private final Account account;
    private final int amount;

    public DepositCommand(Account acc, int amt) {
        account = acc;
        amount = amt;
    }

    public void execute() {
        account.deposit(amount);
    }

    public void undo() {
        account.withdraw(amount);
    }
}

// 4. Withdraw command
class WithdrawCommand implements ICommand {
    private final Account account;
    private final int amount;

    public WithdrawCommand(Account acc, int amt) {
        account = acc;
        amount = amt;
    }

    public void execute() {
        account.withdraw(amount);
    }

    public void undo() {
        account.deposit(amount);
    }
}

// 5. CommandManager for undo

class CommandManager {
    Stack<ICommand> history = new Stack<>();

    public void executeCommand(ICommand cmd) {
        cmd.execute();
        history.push(cmd);
    }

    public void undo() {
        if (!history.isEmpty()) {
            ICommand last = history.pop();
            last.undo();
        }
    }
}

// 6. Invoker
class BankService {
    private final CommandManager manager = new CommandManager();

    public void runCommand(ICommand cmd) {
        manager.executeCommand(cmd);
    }

    public void undoLast() {
        manager.undo();
    }
}

// 7. Client
public class DemoWithDraw {
    public static void main(String[] args) {
        Account acc = new Account();
        BankService bank = new BankService();

        bank.runCommand(new DepositCommand(acc, 100));
        bank.runCommand(new WithdrawCommand(acc, 50));

        System.out.println("Balance: " + acc.getBalance());

        bank.undoLast();  // Undo withdraw
        System.out.println("Balance after undo: " + acc.getBalance());
    }
}
