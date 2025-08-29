import java.util.concurrent.locks.ReentrantLock;

// Step 1: Abstraction
interface RateLimiter {
    boolean allowRequest();
}

// Step 2: Concrete Implementation (Token Bucket)
class TokenBucketRateLimiter implements RateLimiter {
    private final int capacity;         // Max tokens bucket can hold
    private final int refillRate;       // Tokens added per second
    private double currentTokens;       // Current tokens available
    private long lastRefillTimestamp;   // Last refill time
    private final ReentrantLock lock;   // Thread safety

    public TokenBucketRateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.currentTokens = capacity;  // Initially bucket is full
        this.lastRefillTimestamp = System.nanoTime();
        this.lock = new ReentrantLock();
    }

    @Override
    public boolean allowRequest() {
        lock.lock();
        try {
            refill();
            if (currentTokens >= 1) {
                currentTokens -= 1;
                return true;  // Request allowed
            }
            return false; // Rate limited
        } finally {
            lock.unlock();
        }
    }

    // Step 3: Token refill logic
    private void refill() {
        long now = System.nanoTime();
        double tokensToAdd = ((now - lastRefillTimestamp) / 1e9) * refillRate;
        currentTokens = Math.min(capacity, currentTokens + tokensToAdd);
        if (tokensToAdd > 0) {
            lastRefillTimestamp = now;
        }
    }
}
 class LeakyBucketRateLimiter implements RateLimiter {
    private final int capacity;
    private final int leakRate;
    private int water;
    private long lastLeakTimestamp;

    public LeakyBucketRateLimiter(int capacity, int leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.water = 0;
        this.lastLeakTimestamp = System.nanoTime();
    }

    @Override
    public synchronized boolean allowRequest() {
        leak();
        if (water < capacity) {
            water++;
            return true;
        }
        return false;
    }

    private void leak() {
        long now = System.nanoTime();
        long elapsed = now - lastLeakTimestamp;
        int leaked = (int) (elapsed / 1_000_000_000L * leakRate);
        if (leaked > 0) {
            water = Math.max(0, water - leaked);
            lastLeakTimestamp = now;
        }
    }
}
enum Algorithm {
    TOKEN_BUCKET,
    LEAKY_BUCKET
    // can add more like SLIDING_WINDOW, FIXED_WINDOW...
}
 class RateLimiterFactory {
    public static RateLimiter getRateLimiter(Algorithm algo, int capacity, int rate) {
        switch (algo) {
            case TOKEN_BUCKET:
                return new TokenBucketRateLimiter(capacity, rate);
            case LEAKY_BUCKET:
                return new LeakyBucketRateLimiter(capacity, rate);
            default:
                throw new IllegalArgumentException("Unknown Rate Limiter Algo: " + algo);
        }
    }
}

// Step 5: Client Example
public class RateLimiterDemo {
    public static void main(String[] args) throws InterruptedException {
        RateLimiter limiter = RateLimiterFactory.getRateLimiter(Algorithm.TOKEN_BUCKET, 5, 2);
        // bucket of 5, refill 2 tokens/sec

        for (int i = 1; i <= 20; i++) {
            boolean allowed = limiter.allowRequest();
            System.out.println("Request " + i + " → " + (allowed ? "ALLOWED" : "BLOCKED"));
            Thread.sleep(100); // simulate 0.3s delay between requests
        }
    }
}
