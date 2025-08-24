public class Main {
    public static void main(String[] args) {
        // Create a building with 2 elevators with strategy
        BestElevatorStrategy strategy = new NearestElevator();
        ElevatorController controller = new ElevatorController(2,strategy);

        // Simulate external requests
        System.out.println("🔔 External Requests:");
        controller.handleExternalRequest(0, "UP");   // Someone pressed UP on floor 3
        controller.handleExternalRequest(7, "DOWN"); // Someone pressed DOWN on floor 7
        controller.handleExternalRequest(1, "UP");   // Someone pressed UP on ground floor
        controller.handleExternalRequest(5, "DOWN");   // Someone pressed UP on ground floor

        // Simulate internal requests after reaching floors
        // We'll simulate user inside elevator pressing button to go to 5 and 9 later

        // Simulation loop
        int tick = 1;
        while (!controller.allElevatorsIdle() || tick <= 10) {
            System.out.println("\n🔄 Tick: " + tick);
            controller.stepAllElevators();

            // Optional: Simulate a user inside elevator 0 requesting floor 5 after reaching floor 3
            if (tick == 3) {
                controller.handleInternalRequest(0, 1); // Passenger in elevator 0 pressed floor 5
            }

            if (tick == 5) {
                controller.handleInternalRequest(1, 2); // Passenger in elevator 1 pressed floor 9
            }

            tick++;

            try {
                Thread.sleep(0); // wait a bit for readability
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        System.out.println("\n✅ Simulation Complete.");
    }
}
