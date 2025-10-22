package strategy;


import models.LibraryItem;

import java.util.List;

public interface SearchStrategy {
    List<LibraryItem> search(String query, List<LibraryItem> items);
}
