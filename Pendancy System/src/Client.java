import java.util.List;

public class Client {
    public static void main(String[] args) {
        TrackingManager manager = new TrackingManager();
//root (count: 4)
//├── UPI (count: 3)
//│   ├── Karnataka (count: 2)
//│   │   ├── Bangalore (count: 1)
//│   │   └── Mysore    (count: 1)
//│   └── Rajasthan (count: 1)
//│       └── Jaipur     (count: 1)
//└── Wallet (count: 1)
//    └── Karnataka (count: 1)
//        └── Bangalore (count: 1)
        manager.startTracking(1112, List.of("UPI", "Karnataka", "Bangalore"));
        manager.startTracking(2451, List.of("UPI", "Karnataka", "Mysore"));
        manager.startTracking(3421, List.of("UPI", "Rajasthan", "Jaipur"));
        manager.startTracking(1221, List.of("Wallet", "Karnataka", "Bangalore"));

        System.out.println(manager.getCounts(List.of("UPI"))); // Output: 3
        System.out.println(manager.getCounts(List.of("UPI", "Karnataka"))); // Output: 2
        System.out.println(manager.getCounts(List.of("UPI", "Karnataka", "Bangalore"))); // Output: 1
        System.out.println(manager.getCounts(List.of("Bangalore"))); // Output: 0

        manager.startTracking(4221, List.of("Wallet", "Karnataka", "Bangalore"));
        manager.stopTracking(1112);
        manager.stopTracking(2451);

        System.out.println(manager.getCounts(List.of("UPI"))); // Output: 1
        System.out.println(manager.getCounts(List.of("Wallet"))); // Output: 2
        System.out.println(manager.getCounts(List.of("UPI", "Karnataka", "Bangalore"))); // Output: 0
    }
}
