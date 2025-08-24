import java.util.*;

public class TrackingManager {
    private final TagNode root;
    private final Map<Integer, Entity> entityMap;

    public TrackingManager() {
        this.root = new TagNode("root");
        this.entityMap = new HashMap<>();
    }

    // Start tracking an entity with given ID and tags
    public void startTracking(int entityId, List<String> tags) {
        if (entityMap.containsKey(entityId)) {
            System.out.println("⚠️ Entity already being tracked: " + entityId);
            return;
        }

        Entity entity = new Entity(entityId, tags);
        entityMap.put(entityId, entity);

        TagNode current = root;
        for (String tag : tags) {
            current = current.getOrCreateChild(tag);
            current.incrementCount(); // Increase count on path
        }
    }

    // Stop tracking an entity by its ID
    public void stopTracking(int entityId) {
        Entity entity = entityMap.remove(entityId);
        if (entity == null) {
            System.out.println("⚠️ Entity not found: " + entityId);
            return;
        }

        TagNode current = root;
        for (String tag : entity.getTags()) {
            current = current.getChild(tag);
            if (current == null) {
                System.out.println("❌ Invalid state: tag not found while stopping tracking.");
                return;
            }
            current.decrementCount(); // Decrease count on path
        }
    }

    // Get count for a tag path (partial or full)
    public int getCounts(List<String> partialTags) {
        TagNode current = root;
        for (String tag : partialTags) {
            current = current.getChild(tag);//agr ek b tag missing hua to 0
            if (current == null) return 0;
        }
        return current.getCount();
    }
}
