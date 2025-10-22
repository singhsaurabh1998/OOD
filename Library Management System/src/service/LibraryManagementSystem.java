package service;

import enums.ItemType;
import factory.ItemFactory;
import models.BookCopy;
import models.LibraryItem;
import models.Member;
import strategy.SearchStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Certainly! Here’s an end-to-end flow of how the `LibraryManagementSystem` works:
"Checked out" means that a library item (such as a book) has been borrowed by a member and is not currently
available for others to borrow until it is returned.
1. **Initialization**
   The system is a singleton (`getInstance()`), ensuring only one instance manages the library.

2. **Adding Items**
   - Use `addItem(ItemType type, String id, String title, String author, int numCopies)`.
   - The `ItemFactory` creates a `LibraryItem` (e.g., Book or Magazine).
   - The item is added to the catalog.
   - For each copy, a `BookCopy` is created and stored.

3. **Adding Members**
   - Use `addMember(String id, String name)`.
   - A new `Member` is created and stored.

4. **Checkout**
   - Use `checkout(String memberId, String copyId)`.
   - Finds the `Member` and `BookCopy`.
   - Calls `copy.checkout(member)` to update state and associate the copy with the member.

5. **Return Item**
   - Use `returnItem(String copyId)`.
   - Finds the `BookCopy` and calls `copy.returnItem()` to update its state.

6. **Place Hold**
   - Use `placeHold(String memberId, String itemId)`.
   - Finds the `Member` and `LibraryItem`.
   - Places a hold on a checked-out copy for the member.

7. **Search**
   - Use `search(String query, SearchStrategy strategy)`.
   - Applies the chosen search strategy (by title, author, etc.) to the catalog.

8. **Print Catalog**
   - Use `printCatalog()` to display all items and their availability.

**Design Patterns Used:**
- **Factory:** For creating items.
- **Strategy:** For searching.
- **State:** For managing item availability (available, checked out, on hold).

This flow covers adding items/members, borrowing/returning, placing holds, searching, and viewing the catalog.
*/
public class LibraryManagementSystem {
    private static LibraryManagementSystem INSTANCE = null;
    Map<String, LibraryItem> catalog;
    Map<String, Member> members;
    Map<String, BookCopy> copies;

    private LibraryManagementSystem() {
        this.catalog = new HashMap<>();
        this.members = new HashMap<>();
        this.copies = new HashMap<>();
    }

    public static LibraryManagementSystem getInstance() {
        if (INSTANCE == null) {
             INSTANCE = new LibraryManagementSystem();
        }
        return INSTANCE;
    }

    // --- Item Management ---
    public List<BookCopy> addItem(ItemType type, String id, String title, String author, int numCopies) {
        List<BookCopy> bookCopies = new ArrayList<>(); // Represents a physical copies of a library item
        LibraryItem item = ItemFactory.createItem(type, id, title, author);
        catalog.put(id, item);
        for (int i = 0; i < numCopies; i++) {
            String copyId = id + "-c" + (i + 1);
            BookCopy copy = new BookCopy(copyId, item);
            copies.put(copyId, new BookCopy(copyId, item));
            bookCopies.add(copy);
        }
        System.out.println("Added " + numCopies + " copies of '" + title + "'");
        return bookCopies;
    }

    // --- User Management ---
    public Member addMember(String id, String name) {
        Member member = new Member(id, name);
        members.put(id, member);
        return member;
    }

    // --- Core Actions ---
    public void checkout(String memberId, String copyId) {
        Member member = members.get(memberId);
        BookCopy copy = copies.get(copyId);
        if (member != null && copy != null) {
            copy.checkout(member);
        } else {
            System.out.println("Error: Invalid member or copy ID.");
        }
    }

    public void returnItem(String copyId) {
        BookCopy copy = copies.get(copyId);
        if (copy != null) {
            copy.returnItem();
        } else {
            System.out.println("Error: Invalid copy ID.");
        }
    }

    public void placeHold(String memberId, String itemId) {
        Member member = members.get(memberId);
        LibraryItem item = catalog.get(itemId);
        if (member != null && item != null) {
            for (BookCopy copy : item.getCopies()) {
                if (!copy.isAvailable()) {
                    copy.placeHold(member);
                    break;
                }
            }
        }
    }


    // --- Search (Using Strategy Pattern) ---
    public List<LibraryItem> search(String query, SearchStrategy strategy) {
        return strategy.search(query, new ArrayList<>(catalog.values()));
    }

    public void printCatalog() {
        System.out.println("\n--- Library Catalog ---");
        for (LibraryItem item : catalog.values()) {
            System.out.printf("ID: %s, Title: %s, Author/Publisher: %s, Available: %d\n",
                    item.getId(), item.getTitle(), item.getAuthorOrPublisher(), item.getAvailableCopyCount());
        }

        System.out.println("-----------------------\n");
    }

    public Map<String, LibraryItem> getCatalog() {
        return catalog;
    }

    public Map<String, Member> getMembers() {
        return members;
    }

    public Map<String, BookCopy> getCopies() {
        return copies;
    }
}
