package factory;

import enums.ItemType;
import models.Book;
import models.LibraryItem;
import models.Magazine;

public class ItemFactory {
    public static LibraryItem createItem(ItemType type, String id, String title, String authorOrPublisher) {
        if (type == ItemType.BOOK) {
            return new Book(id, title, authorOrPublisher);
        } else if (type == ItemType.MAGAZINE) {
            return new Magazine(id, title, authorOrPublisher);
        } else {
            throw new IllegalArgumentException("Unknown item type.");
        }
    }
}
