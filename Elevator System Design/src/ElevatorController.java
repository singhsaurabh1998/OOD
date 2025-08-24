import java.util.ArrayList;
import java.util.List;

public class ElevatorController {
    private final List<Elevator> elevators;
    private final BestElevatorStrategy strategy;

    ElevatorController(int totalElevators, BestElevatorStrategy strategy) {
        elevators = new ArrayList<>();
        for (int i = 1; i <= totalElevators; i++) {
            elevators.add(new Elevator(i));
        }
        this.strategy = strategy;
    }

    /**
     * Assigns request to the best elevator (simplified strategy).
     */
    public void handleExternalRequest(int floor, String direction) {
        Elevator bestElevator = strategy.bestElevator(floor, direction, elevators);
       /* int dis = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            if (e.isIdle() || e.getDirection().equals(direction)) {
                int distance = Math.abs(e.getCurrentFloor() - floor);
                if (dis > distance) {
                    dis = distance;
                    bestElevator = e;
                }
            }
        }
        if (bestElevator == null)
            bestElevator =elevators.get(0); //in case no elevators assigned
*/
        bestElevator.addRequest(new Request(floor, direction, "EXTERNAL"));
    }

    /**
     * Handle an internal request (floor button pressed inside elevator).
     */
    public void handleInternalRequest(int elevatorId, int destinationFloor) {
        Elevator elevator = elevators.get(elevatorId);
        String dir = destinationFloor > elevator.getCurrentFloor() ? "UP" : "DOWN";
        elevator.addRequest(new Request(destinationFloor, dir, "INTERNAL"));

    }

    /**
     * Simulate each elevator moving one step (tick).
     */
    public void stepAllElevators() {
        for (Elevator elevator : elevators) {
            elevator.step();
        }
    }

    public boolean allElevatorsIdle() {
        for (Elevator elevator : elevators) {
            if (!elevator.isIdle()) return false;
        }
        return true;
    }

    public void printElevatorStates() {
        for (Elevator elevator : elevators) {
            System.out.println("Elevator " + elevator + " => Floor: " + elevator.getCurrentFloor() + " | Dir: " + elevator.getDirection() + " | Status: " + elevator.getStatus());
        }
    }

}
