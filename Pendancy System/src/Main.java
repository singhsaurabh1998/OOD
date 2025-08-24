//import java.util.*;
//
//class PendingEntityTracker {
//    private final Map<Integer, List<String>> activeEntities = new HashMap<>();
//
//    // Start tracking an entity with ID and tag hierarchy
//    public void startTracking(int id, List<String> tags) {
//        if (!activeEntities.containsKey(id)) {
//            activeEntities.put(id, new ArrayList<>(tags));
//        }
//    }
//
//    // Stop tracking the entity with given ID
//    public void stopTracking(int id) {
//        activeEntities.remove(id);
//    }
//
//    // Get count of all tracked entities matching given tag prefix
//    public int getCounts(List<String> queryTags) {
//        int count = 0;
//        for (List<String> tags : activeEntities.values()) {
//            if (isPrefixMatch(tags, queryTags)) {
//                count++;
//            }
//        }
//        return count;
//    }
//
//    // Check if queryTags is prefix of full tag path
//    private boolean isPrefixMatch(List<String> fullTags, List<String> queryTags) {
//        if (queryTags.size() > fullTags.size()) return false;
//        for (int i = 0; i < queryTags.size(); i++) {
//            if (!queryTags.get(i).equals(fullTags.get(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//}
