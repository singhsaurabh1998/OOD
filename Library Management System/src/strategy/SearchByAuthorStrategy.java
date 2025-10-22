package strategy;



import models.LibraryItem;

import java.util.ArrayList;
import java.util.List;

public class SearchByAuthorStrategy implements SearchStrategy {
    // Search items by author or publisher
    @Override
    public List<LibraryItem> search(String query, List<LibraryItem> items) {
        List<LibraryItem> result = new ArrayList<>();
        items.stream()
                .filter(item -> item.getAuthorOrPublisher().toLowerCase().contains(query.toLowerCase()))
                .forEach(result::add);
        return result;
    }
}
