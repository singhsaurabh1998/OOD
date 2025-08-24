public class PaymentProcessorFactory {
    public enum Type {PAYTM, RAZORPAY}

    public static PaymentStrategy get(Type type) {
        switch (type) {
            case PAYTM:
                return new PaytmGateway();
            case RAZORPAY:
                return new RazorpayGateway();
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }
}
