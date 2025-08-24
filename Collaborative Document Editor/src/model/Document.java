package model;

import methods.Operation;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Document {
    private final String docId;
    private String title;
    private final StringBuilder content;         // actual text
    private final List<Operation> history;       // operations applied
    private final ReentrantLock lock = new ReentrantLock(); // add this

    public Document(String docId, String title) {
        this.docId = docId;
        this.title = title;
        this.content = new StringBuilder();
        this.history = new ArrayList<>();
    }

    // Apply an operation and record it
    public synchronized void applyOperation(Operation op) {
        op.apply(content);
        history.add(op);
    }
    // Instead of synchronized, use explicit lock for clarity
//    public void applyOperation(Operation op) {
//        lock.lock();
//        try {
//            op.apply(this);
//            history.add(op);
//        } finally {
//            lock.unlock();
//        }
//    }

    public String getDocId() { return docId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public StringBuilder getContent() { return content; }
    public List<Operation> getHistory() { return history; }
}

