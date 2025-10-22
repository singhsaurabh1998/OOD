package models;

import state.AvailableState;
import state.ItemState;
//context class for State pattern
public class BookCopy {  // Represents a physical copy of a library item
    private final String id;
    private final LibraryItem item; // The library item this copy belongs to
    private ItemState currentState;// Current state of the book copy

    public BookCopy(String id, LibraryItem item) {
        this.id = id;
        this.item = item;
        this.currentState = new AvailableState();
        item.addCopy(this);
    }

    public void checkout(Member member) {
        currentState.checkout(this, member);
    }

    public void returnItem() {
        currentState.returnItem(this);
    }

    public void placeHold(Member member) {
        currentState.placeHold(this, member);
    }

    public void setState(ItemState state) {
        this.currentState = state;
    }

    public String getId() {
        return id;
    }

    public LibraryItem getItem() {
        return item;
    }

    public boolean isAvailable() {
        return currentState instanceof AvailableState;
    }

    public void printDetails() {
        System.out.println("BookCopy ID: " + id + ", Title: " + item.getTitle() + ", State: " + currentState.getClass().getSimpleName());
    }

}
