package model;

public class Professor {
    private final String id;
    private final String name;

    public Professor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }
}