import java.util.List;

public class DigitalWalletDemo {
    public static void main(String[] args) {
        DigitalWalletService service = DigitalWalletService.getInstance();

        User saurabh = new User("user1", "Saurabh", "papa@email.com", "password123");
        User gaurav = new User("user2", "Gaurav", "kaka@email.com", "kaka123");
        service.addUser(saurabh);
        service.addUser(gaurav);

        Account account1 = new Account("acc1", saurabh, "1234567890", Currency.USD);
        Account account2 = new Account("acc2", gaurav, "0987654321", Currency.EUR);
        service.createAccount(account1);
        service.createAccount(account2);
//
        //System.out.println(service.getAccount(account1.getId()));
        // Add payment methods
        PaymentMethod creditCard = new CreditCard("PM001", saurabh, "1234567890123456", "12/25", "123");
        PaymentMethod bankAccount = new BankAccount("PM002", gaurav, "9876543210", "987654321");
        service.addPaymentMethod(creditCard);
        service.addPaymentMethod(bankAccount);
        // Deposit funds
        account1.deposit(1000.00);
        account2.deposit(500.00);
        service.transferFunds(account1, account2, 100.00, Currency.USD);
        // Get transaction history
        List<Transaction> transactionHistory1 = service.getTransactionHistory(account1);
        List<Transaction> transactionHistory2 = service.getTransactionHistory(account2);

        // Print transaction history
        System.out.println("Transaction History for Account 1:");
        for (Transaction transaction : transactionHistory1) {
            System.out.println("Transaction ID: " + transaction.getId());
            System.out.println("Amount: " + transaction.getAmount() + " " + transaction.getCurrency());
            System.out.println("Timestamp: " + transaction.getTimestamp());
            System.out.println();
        }

        System.out.println("Transaction History for Account 2:");
        for (Transaction transaction : transactionHistory2) {
            System.out.println("Transaction ID: " + transaction.getId());
            System.out.println("Amount: " + transaction.getAmount() + " " + transaction.getCurrency());
            System.out.println("Timestamp: " + transaction.getTimestamp());
            System.out.println();
        }
    }
}