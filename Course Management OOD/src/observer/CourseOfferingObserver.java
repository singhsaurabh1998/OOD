package observer;

import model.CourseOffering;

public interface CourseOfferingObserver {
    void onSpotAvailable(CourseOffering offering);
}
