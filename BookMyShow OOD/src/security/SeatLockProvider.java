package security;

import model.Seat;
import model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SeatLockProvider {
    private final int lockDurationSeconds;
    //id->seatlock
    private final Map<Integer, SeatLock> seatLocks;//contains the info of the locked seats(local DataBase)

    public SeatLockProvider(int lockDurationSeconds) {
        this.lockDurationSeconds = lockDurationSeconds;
        this.seatLocks = new ConcurrentHashMap<>();
    }

    // Lock multiple seats atomically
    public synchronized void lockSeats(List<Seat> seats, User user) {
        // First check if all requested seats can be locked
        for (Seat seat : seats) {
            if (isSeatLocked(seat)) {
                throw new RuntimeException("❌ Seat already locked: " + seat.getSeatId());
            }
        }
        for (Seat seat : seats) {
            SeatLock seatLock = new SeatLock(seat, user, lockDurationSeconds);
            seatLocks.put(seat.getSeatId(), seatLock);
        }
        System.out.println("✅ Seats locked by user: " + user.getName());

    }

    public boolean isSeatLocked(Seat seat) {
        if (!seatLocks.containsKey(seat.getSeatId()))
            return false; //map ne nahi h to lock b nahi h

        SeatLock seatLock = seatLocks.get(seat.getSeatId());
        if (seatLock.isLockExpired()) {
            seatLocks.remove(seat.getSeatId());//map se n hatado us id ko
            return false;
        } else
            return true;//mtlb time duration h abhi b unlock hone me
    }

    public void unlockSeats(List<Seat> seats, User user) {
        for (Seat seat : seats) {
            if (isSeatLockedByUser(seat, user)) {
                seatLocks.remove(seat.getSeatId());
            }
        }
        System.out.println("🔓 Seats unlocked by user: " + user.getName());
    }

    public boolean isSeatLockedByUser(Seat seat, User user) {
        if (!seatLocks.containsKey(seat.getSeatId())) return false;

        SeatLock lock = seatLocks.get(seat.getSeatId());
        if (lock.isLockExpired()) {
            seatLocks.remove(seat.getSeatId());
            return false;
        }

        return lock.getLockedBy().equals(user);
    }
}
