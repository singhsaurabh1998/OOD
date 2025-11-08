package service;

import chain.*;
import exception.RegistrationException;

public class CourseRegistrationService {
    private final RegistrationRuleHandler registrationChain;

    public CourseRegistrationService() {
        // Build the chain of responsibility
        RegistrationRuleHandler capacityHandler = new CapacityRuleHandler();
        RegistrationRuleHandler conflictHandler = new ScheduleConflictRuleHandler();
        RegistrationRuleHandler prereqHandler = new PrerequisiteRuleHandler();
        conflictHandler.setNext(capacityHandler);
        prereqHandler.setNext(conflictHandler);
        this.registrationChain = prereqHandler;
    }

    public void register(RegistrationRequest request) throws RegistrationException {
        registrationChain.handle(request);
    }
}
