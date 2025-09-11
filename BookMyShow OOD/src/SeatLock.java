import java.time.Duration;
import java.time.LocalDateTime;

public class SeatLock {
    private final Seat seat;

    private final User lockedBy;
    private final LocalDateTime lockTime;
    private final int lockDurationSeconds;

    public SeatLock(Seat seat, Show show, User user, int lockDurationSeconds) {
        this.seat = seat;
        this.lockedBy = user;
        this.lockDurationSeconds = lockDurationSeconds;
        this.lockTime = LocalDateTime.now(); // Lock timestamp
    }

    public boolean isLockExpired() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(lockTime, now);
        return duration.getSeconds() > lockDurationSeconds;
    }

    // Getters
    public Seat getSeat() {
        return seat;
    }


    public User getLockedBy() {
        return lockedBy;
    }

    public LocalDateTime getLockTime() {
        return lockTime;
    }

    public int getLockDurationSeconds() {
        return lockDurationSeconds;
    }

}
