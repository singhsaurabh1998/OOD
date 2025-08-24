package service;

import methods.DeleteOperation;
import methods.InsertOperation;
import methods.Operation;
import model.Document;
import model.User;

import java.util.UUID;

public class CollaborativeSessionService {
    private final String sessionId;
    private final User user;
    private final Document document;
    private int cursorPosition;

    public CollaborativeSessionService(String sessionId, User user, Document document) {
        this.sessionId = sessionId;
        this.user = user;
        this.document = document;
        this.cursorPosition = 0;
    }

    public void insertText(String text, DocumentSessionManager manager) {
        String opId = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        InsertOperation op = new InsertOperation(opId, user.getUserId(), cursorPosition, timestamp, text);
        document.applyOperation(op);
        cursorPosition += text.length();

        // notify other users
        manager.broadcast(op, this);
    }

    public void deleteText(int length, DocumentSessionManager manager) {
        String opId = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        DeleteOperation op = new DeleteOperation(opId, user.getUserId(), cursorPosition, timestamp, length);
        document.applyOperation(op);

        // notify others
        manager.broadcast(op, this);
    }

    // Observer method: called when other users make changes
    public void receiveUpdate(Operation op) {
        System.out.println("[Notification] User " + user.getName() + " received update: "
                + op.getClass().getSimpleName() + " by userId " + op.getUserId());
        // In real app: update local cursor, apply transformation
    }

    // Getters and setters
    public String getSessionId() { return sessionId; }
    public User getUser() { return user; }
    public Document getDocument() { return document; }
    public int getCursorPosition() { return cursorPosition; }
    public void moveCursor(int newPosition) { this.cursorPosition = newPosition; }
}
