public class PaymentApp {
    public static void main(String[] args) {
        // Example 1: Paytm, 3 retries
        PaymentRequest r1 = new PaymentRequest(14000, "Alice", "Bob", "INR","txn-unique-key-001");
        PaymentStrategy strategy = PaymentProcessorFactory.get(PaymentProcessorFactory.Type.PAYTM);
        PaymentService service = new PaymentService(strategy, 3);
        String trxnId = service.pay(r1);
        String trxnId2 = service.pay(r1);
//        if (trxnId != null) {
//            System.out.println("Transaction was successfull we can raise the refund request!");
//            boolean refund = service.refund(trxnId);
//
//
//        }
//        // Example 2: Razorpay, 2 retries
//        PaymentRequest r2 = new PaymentRequest(14000, "Alice", "Bob", "USD","twxn-unique-key-001");
//        PaymentStrategy strategy2 = PaymentProcessorFactory.get(PaymentProcessorFactory.Type.RAZORPAY);
//
//        PaymentService s2 = new PaymentService(strategy2, 2);
//        String trxnId2 = s2.pay(r2);
//        if (trxnId2 != null) {
//            System.out.println("Transaction was successfull we can raise the refund request!");
//            boolean refund = s2.refund(trxnId2);
//
//
//        }
    }
}
