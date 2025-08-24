import methods.Operation;
import model.Document;
import model.User;
import service.CollaborativeSessionService;
import service.DocumentSessionManager;

public class CollaborativeEditorApp {
    public static void main(String[] args) {
        Document doc = new Document("d1", "Design Notes");
        DocumentSessionManager manager = new DocumentSessionManager(doc);

        User alice = new User("u1", "Alice", "alice@example.com");
        User bob = new User("u2", "Bob", "bob@example.com");

        CollaborativeSessionService aliceSession = manager.createSession(alice);
        CollaborativeSessionService bobSession = manager.createSession(bob);

        System.out.println("Initial content: " + doc.getContent());

        // Alice inserts "Hello"
        aliceSession.insertText("Hello", manager);

        // Bob inserts " World!" at cursor 0 (should move cursor in real app, but keep simple)
        bobSession.moveCursor(5); // move after "Hello"
        bobSession.insertText(" World!", manager);

        System.out.println("Final content: " + doc.getContent());

        // Show history
        for (Operation op : doc.getHistory()) {
            System.out.println("Op: " + op.getClass().getSimpleName() + " by userId " + op.getUserId());
        }
    }
}
