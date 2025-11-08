package chain;

import exception.RegistrationException;

public class CapacityRuleHandler extends RegistrationRuleHandler {
    @Override
    public void handle(RegistrationRequest request) throws RegistrationException {
        if (request.getOffering().isFull()) {
            request.getOffering().addToWaitlist(request.getStudent());
            System.out.println("Course " + request.getOffering().getCourse().getCourseCode() + " is full. " + request.getStudent().getName() + " added to waitlist.");
        } else {
            request.getOffering().addStudent(request.getStudent());
            System.out.println(request.getStudent().getName() + " successfully registered for " + request.getOffering().getCourse().getCourseCode());
        }
        handleNext(request);
    }
}
