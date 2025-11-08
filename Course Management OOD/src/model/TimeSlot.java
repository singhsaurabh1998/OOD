package model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class TimeSlot {
    private final DayOfWeek day;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimeSlot(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean overlaps(TimeSlot other) {
        return this.day == other.day &&
                this.startTime.isBefore(other.endTime) &&
                this.endTime.isAfter(other.startTime);
    }

}
