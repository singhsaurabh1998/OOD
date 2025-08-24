import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SeatLockProvider {
    private final int lockDurationSeconds;
    private final Map<Seat, SeatLock> seatLocks;//contains the info of the locked seats(local DataBase)

    public SeatLockProvider(int lockDurationSeconds) {
        this.lockDurationSeconds = lockDurationSeconds;
        this.seatLocks = new ConcurrentHashMap<>();
    }

    // Lock multiple seats atomically
    public synchronized void lockSeats(Show show, List<Seat> seats, User user) {
        // First check if all requested seats can be locked
        for (Seat seat : seats) {
            if (isSeatLocked(seat)) {
                throw new RuntimeException("❌ Seat already locked: " + seat.getSeatId());
            }
        }
        for (Seat seat : seats) {
            SeatLock seatLock = new SeatLock(seat, show, user, lockDurationSeconds);
            seatLocks.put(seat, seatLock);
        }
        System.out.println("✅ Seats locked by user: " + user.getName());

    }

    public boolean isSeatLocked(Seat seat) {
        if (!seatLocks.containsKey(seat))
            return false; //map ne nahi h to lock b nahi h

        SeatLock seatLock = seatLocks.get(seat);
        if (seatLock.isLockExpired()) {
            seatLocks.remove(seat);//map se n hatado
            return false;
        } else
            return true;//mtlb time duration h abhi b unlock hone me
    }

    public void unlockSeats(List<Seat> seats, User user) {
        for (Seat seat : seats) {
            if (isSeatLockedByUser(seat, user)) {
                seatLocks.remove(seat);
            }
        }
        System.out.println("🔓 Seats unlocked by user: " + user.getName());
    }

    public boolean isSeatLockedByUser(Seat seat, User user) {
        if (!seatLocks.containsKey(seat)) return false;

        SeatLock lock = seatLocks.get(seat);
        if (lock.isLockExpired()) {
            seatLocks.remove(seat);
            return false;
        }

        return lock.getLockedBy().equals(user);
    }
}
