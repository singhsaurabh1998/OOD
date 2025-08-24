public class Request {
    private final int requestedFloor;
    private final String direction;
    private final String requestType ;//EXTERNAL/INTERNAL  Button pressed outside/inside elevator

    public Request(int floor, String direction, String type) {
        this.requestedFloor = floor;
        this.direction = direction;
        this.requestType = type;
    }
    public int getRequestedFloor() {
        return requestedFloor;
    }

    public String getDirection() {
        return direction;
    }

    public String getType() {
        return requestType;
    }
}
