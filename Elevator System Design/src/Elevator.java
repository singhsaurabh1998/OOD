import java.util.TreeSet;

public class Elevator {
    private final int elevatorId;
    private int currentFloor;
    private String direction;
    private String elevatorStatus;//MOVING,STOPPED,IDLE;

    // TreeSet to auto-sort requests
    private final TreeSet<Integer> upRequests = new TreeSet<>();
    private final TreeSet<Integer> downRequests = new TreeSet<>((a, b) -> b - a); // max to min

    public Elevator(int id) {
        this.elevatorId = id;
        this.currentFloor = 0;
        this.direction = "IDLE";
        this.elevatorStatus = "IDLE";
    }

    public void addRequest(Request request) {
        int requestedFloor = request.getRequestedFloor();
        if (requestedFloor == currentFloor) {
            openDoor(); //already reached to destination floor
            return;
        }
        if (requestedFloor > currentFloor) {
            upRequests.add(requestedFloor);
        } else {
            downRequests.add(requestedFloor);
        }

        if (direction.equals("IDLE")) {
            direction = requestedFloor > currentFloor ? "UP" : "DOWN";
        }

        System.out.println("✅ Request added: " + request.getType() + " to floor " + requestedFloor + " [" + direction + "]");

    }

    public void step() {
        if (direction.equals("UP")) {
            if (upRequests.isEmpty()) {
                if (!downRequests.isEmpty()) {
                    direction = "DOWN";
                } else {
                    direction = "IDLE";
                    elevatorStatus = "IDLE";
                    return;
                }
            } else {
                moveUp();
            }
        } else if (direction.equals("DOWN")) {
            if (downRequests.isEmpty()) {
                if (!upRequests.isEmpty()) {
                    direction = "UP";
                } else {
                    direction = "IDLE";
                    elevatorStatus = "IDLE";
                    return;
                }
            } else {
                moveDown();
            }
        }
    }

    private void moveUp() {
        elevatorStatus = "MOVING";
        currentFloor++;
        System.out.println("⬆ Elevator " + elevatorId + " moved to floor " + currentFloor);
        if (upRequests.contains(currentFloor)) {
            upRequests.remove(currentFloor);
            openDoor();
        }

    }


    private void moveDown() {
        elevatorStatus = "MOVING";
        currentFloor--;
        System.out.println("⬆ Elevator " + elevatorId + " moved to floor " + currentFloor);
        if (downRequests.contains(currentFloor)) {
            downRequests.remove(currentFloor);
            openDoor();
        }
    }

    private void openDoor() {
        elevatorStatus = "STOPPED";
        System.out.println("🚪 Elevator " + elevatorId + " stopped at floor " + currentFloor + " - Doors opening...");
        // simulate door open/wait/close
        try {
            Thread.sleep(2000);// 2 sec k baad door close ho jayega
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("🚪 Doors closing...");
    }


    public boolean isIdle() {
        return direction.equals("IDLE") && upRequests.isEmpty() && downRequests.isEmpty();
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public String getDirection() {
        return direction;
    }

    public String getStatus() {
        return elevatorStatus;
    }
}
