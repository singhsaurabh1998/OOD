package chain;
import exception.RegistrationException;

public class ScheduleConflictRuleHandler extends RegistrationRuleHandler {
    @Override
    public void handle(RegistrationRequest request) throws RegistrationException {
        boolean conflict = request.getStudent().getRegisteredOfferings().stream()
                .anyMatch(offering -> offering.getTimeSlot().overlaps(request.getOffering().getTimeSlot()));
        if (conflict) {
            throw new RegistrationException("Schedule conflict detected for course " + request.getOffering().getCourse().getCourseCode());
        }
        handleNext(request);
    }
}
