package service;

import methods.Operation;
import model.Document;
import model.User;

import java.util.*;

public class DocumentSessionManager {
    private final Document document;
    private final List<CollaborativeSessionService> sessions;

    public DocumentSessionManager(Document document) {
        this.document = document;
        this.sessions = new ArrayList<>();
    }

    public CollaborativeSessionService createSession(User user) {
        CollaborativeSessionService session = new CollaborativeSessionService(UUID.randomUUID().toString(), user, document);
        sessions.add(session);
        return session;
    }

    public void removeSession(CollaborativeSessionService session) {
        sessions.remove(session);
    }

    // Notify all sessions except the one who submitted(khud ko notify nahi krega)
    public void broadcast(Operation op, CollaborativeSessionService originSession) {
        for (CollaborativeSessionService session : sessions) {
            if (!session.getSessionId().equals(originSession.getSessionId())) {
                session.receiveUpdate(op);
            }
        }
    }

    public List<CollaborativeSessionService> getSessions() {
        return sessions;
    }
}
