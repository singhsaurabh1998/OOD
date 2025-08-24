package methods;

public abstract class Operation {
    protected final String opId;      // unique operation id
    protected final String userId;    // who performed it
    protected final int position;     // where in text
    protected final long timestamp;   // when

    public Operation(String opId, String userId, int position, long timestamp) {
        this.opId = opId;
        this.userId = userId;
        this.position = position;
        this.timestamp = timestamp;
    }

    // Applies the operation to document content
    public abstract void apply(StringBuilder content);

    public String getOpId() { return opId; }
    public String getUserId() { return userId; }
    public int getPosition() { return position; }
    public long getTimestamp() { return timestamp; }
}
