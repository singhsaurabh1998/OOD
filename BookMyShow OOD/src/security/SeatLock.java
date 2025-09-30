package security;

import lombok.Getter;
import model.Seat;
import model.User;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class SeatLock {
    // Getters
    private final Seat seat;

    private final User lockedBy;
    private final LocalDateTime lockTime;
    private final int lockDurationSeconds;

    public SeatLock(Seat seat, User user, int lockDurationSeconds) {
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
}
