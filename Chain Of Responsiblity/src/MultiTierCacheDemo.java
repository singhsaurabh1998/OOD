import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

interface EvictionPolicy {
    void recordAccess(String key);

    String evictKey();
}

class LRU implements EvictionPolicy {
    private final LinkedHashMap<String, Boolean> usage = new LinkedHashMap<>();

    public void recordAccess(String key) {
        usage.remove(key);
        usage.put(key, true);
    }

    public String evictKey() {
        String firstKey = usage.keySet().iterator().next();
        usage.remove(firstKey);
        return firstKey;
    }
}

// Base cache tier (Chain of Responsibility)
abstract class CacheHandler {
    protected int capacity;
    protected Map<String, String> storage = new HashMap<>();
    protected EvictionPolicy evictionPolicy;
    protected CacheHandler nextTier;

    public CacheHandler(int capacity, EvictionPolicy policy) {
        this.capacity = capacity;
        this.evictionPolicy = policy;
    }

    public void setNext(CacheHandler next) {
        this.nextTier = next;
    }

    public String get(String key) {
        if (storage.containsKey(key)) {
            evictionPolicy.recordAccess(key);
            return storage.get(key);
        }
        if (nextTier != null) {
            String val = nextTier.get(key);
            if (val != null) {
                // promote to higher tier
                put(key, val);
            }
            return val;
        }
        return null;
    }

    public void put(String key, String value) {
        if (storage.size() >= capacity) {
            String evictKey = evictionPolicy.evictKey();
            String evictVal = storage.remove(evictKey);
            if (nextTier != null) {
                nextTier.put(evictKey, evictVal);
            }
        }
        storage.put(key, value);
        evictionPolicy.recordAccess(key);
    }

    public void printContents(String tierName) {
        System.out.println(tierName + " cache: " + storage.keySet());
    }
}

// Concrete cache tiers
class InMemoryCache extends CacheHandler {
    public InMemoryCache(int capacity, EvictionPolicy policy) {
        super(capacity, policy);
    }
}

class SSDCache extends CacheHandler {
    public SSDCache(int capacity, EvictionPolicy policy) {
        super(capacity, policy);
    }
}

class HDDCache extends CacheHandler {
    public HDDCache(int capacity, EvictionPolicy policy) {
        super(capacity, policy);
    }
}

// Facade for client
class CacheManager {
    private final CacheHandler top;

    public CacheManager() {
        CacheHandler ram = new InMemoryCache(2, new LRU());  // small RAM for demo
        CacheHandler ssd = new SSDCache(3, new LRU());
        CacheHandler hdd = new HDDCache(5, new LRU());

        ram.setNext(ssd);
        ssd.setNext(hdd);

        this.top = ram;
    }

    public String get(String key) {
        return top.get(key);
    }

    public void put(String key, String value) {
        top.put(key, value);
    }

    public void printState() {
        ((InMemoryCache) top).printContents("RAM");
        ((SSDCache) top.nextTier).printContents("SSD");
        ((HDDCache) top.nextTier.nextTier).printContents("HDD");
    }
}

// Main driver
public class MultiTierCacheDemo {
    public static void main(String[] args) {
        CacheManager cache = new CacheManager();

        cache.put("A", "Alpha");  // goes to RAM
        cache.put("B", "Beta");   // goes to RAM
        cache.printState();

        cache.put("C", "Gamma");  // RAM full → evict LRU (A) → move A to SSD
        cache.printState();

        // Now RAM: [B, C], SSD: [A]
        String val = cache.get("A");  // Found in SSD → promoted back to RAM
        System.out.println("Fetched: " + val);
        cache.printState();
    }
}
