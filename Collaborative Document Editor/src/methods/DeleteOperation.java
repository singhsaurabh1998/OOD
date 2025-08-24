package methods;

public class DeleteOperation extends Operation {
    private final int length;

    public DeleteOperation(String opId, String userId, int position, long timestamp, int length) {
        super(opId, userId, position, timestamp);
        this.length = length;
    }

    @Override
    public void apply(StringBuilder content) {
        content.delete(position, position + length);
    }

    public int getLength() { return length; }
}

