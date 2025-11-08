package chain;

import exception.RegistrationException;
import model.Course;

import java.util.Set;

public class PrerequisiteRuleHandler extends RegistrationRuleHandler {
    @Override
    public void handle(RegistrationRequest request) throws RegistrationException {
        Set<Course> completed = request.getStudent().getCompletedCourses();
        Set<Course> prereqs = request.getOffering().getCourse().getPrerequisites();
        if (!completed.containsAll(prereqs)) {
            throw new RegistrationException("Prerequisite not met for course " + request.getOffering().getCourse().getCourseCode());
        }
        handleNext(request);
    }
}
