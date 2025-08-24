package methods;

public class InsertOperation extends Operation {
    private final String text;

    public InsertOperation(String opId, String userId, int position, long timestamp, String text) {
        super(opId, userId, position, timestamp);
        this.text = text;
    }

    @Override
    public void apply(StringBuilder content) {
        content.insert(position, text);
    }

    public String getText() { return text; }
}
