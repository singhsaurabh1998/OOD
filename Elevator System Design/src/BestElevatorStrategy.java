import java.util.List;

public interface BestElevatorStrategy {
    Elevator bestElevator(int floor, String direction,List<Elevator> elevators);
}

class NearestElevator implements BestElevatorStrategy{

    @Override
    public Elevator bestElevator(int floor, String direction, List<Elevator> elevators) {
        Elevator bestElevator = null;
        int dis = Integer.MAX_VALUE;

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
            bestElevator = elevators.get(0); //in case no elevators assigned
        return bestElevator;
    }
}
