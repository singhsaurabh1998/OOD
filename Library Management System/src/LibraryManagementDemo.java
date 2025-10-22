import enums.ItemType;
import models.BookCopy;
import models.LibraryItem;
import models.Member;
import service.LibraryManagementSystem;
import strategy.SearchByAuthorStrategy;
import strategy.SearchByTitleStrategy;

import java.util.List;

public class LibraryManagementDemo {
    public static void main(String[] args) {
        LibraryManagementSystem library = LibraryManagementSystem.getInstance();
        System.out.println("=== Setting up the Library ===");
        List<BookCopy> cppCopy = library.addItem(ItemType.BOOK, "B001", "C++", "F. Scott Fitzgerald", 3);
        List<BookCopy> javaCopy = library.addItem(ItemType.BOOK, "B002", "Java", "Harper Lee", 2);
        Member alice = library.addMember("MEM01", "Alice");
        Member bob = library.addMember("MEM02", "Bob");
        Member charlie = library.addMember("MEM03", "Charlie");
        System.out.println("\n=== Initial State ===");
        library.printCatalog();

        // --- Scenario 1: Searching (Strategy Pattern) ---
        System.out.println("\n=== Scenario 1: Searching for Items ===");
        System.out.println("Searching for title 'C++':");
        List<LibraryItem> cppSearch = library.search("C++", new SearchByTitleStrategy());
        System.out.println("Found: " + cppSearch);
        System.out.println("\nSearching for author 'Harper Lee':");
        library.search("Harper Lee", new SearchByAuthorStrategy())
                .forEach(item -> System.out.println("Found: " + item.getTitle()));

        // --- Scenario 2: Checkout and Return (State Pattern) ---
       /* System.out.println("\n\n=== Scenario 2: Checkout and Return ===");
        library.checkout(alice.getId(), cppCopy.get(0).getId()); // Alice checks out The C++ copy 1
        library.checkout(charlie.getId(), cppCopy.get(1).getId()); // Charlie tries to check out C++ copy 1 (already checked out)
        library.checkout(bob.getId(), javaCopy.get(0).getId()); // Bob checks out JAVA copy 1
        library.printCatalog();

        library.returnItem(cppCopy.get(0).getId()); // Alice returns C++ copy 1
        //library.returnItem(javaCopy.get(0).getId()); // Bob returns JAVA copy 1
        library.printCatalog();
        library.placeHold(bob.getId(),"B002"); // Bob places hold on JAVA copy 1
        library.returnItem(javaCopy.get(0).getId());
        library.placeHold(alice.getId(),"B001"); // Charlie places hold on JAVA copy 1
        library.returnItem(cppCopy.get(1).getId());*/
    }
}
