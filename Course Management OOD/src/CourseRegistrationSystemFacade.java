import chain.RegistrationRequest;
import exception.RegistrationException;
import model.CourseOffering;
import model.Student;
import repository.CourseRepository;
import repository.StudentRepository;
import service.CourseRegistrationService;

import java.util.Optional;

public class CourseRegistrationSystemFacade {
    private final CourseRegistrationService courseRegistrationService = new CourseRegistrationService();
    private final StudentRepository studentRepo = StudentRepository.getInstance();
    private final CourseRepository courseRepo = CourseRepository.getInstance();

    public void registerStudentForCourse(String studentId, String offeringId) {
        Optional<Student> studentOpt = studentRepo.findById(studentId);
        if (studentOpt.isEmpty()) {
            System.err.println("Error: Student " + studentId + " not found.");
            return;
        }
         Optional<CourseOffering> offeringOpt = courseRepo.findOfferingById(offeringId);
        if (offeringOpt.isEmpty()) {
            System.err.println("Error: Course offering " + offeringId + " not found.");
            return;
        }
        try {
            courseRegistrationService.register(new RegistrationRequest(studentOpt.get(), offeringOpt.get()));
        } catch (RegistrationException e) {
            System.err.println("Registration Failed for " + studentOpt.get().getName() + ": " + e.getMessage());
        }
    }

    public void dropStudentFromCourse(String studentId, String offeringId) {
        Optional<Student> studentOpt = studentRepo.findById(studentId);
        if (studentOpt.isEmpty()) {
            System.err.println("Error: Student " + studentId + " not found.");
            return;
        }
        Optional<CourseOffering> offeringOpt = courseRepo.findOfferingById(offeringId);
        if (offeringOpt.isEmpty()) {
            System.err.println("Error: Course offering " + offeringId + " not found.");
            return;
        }
        offeringOpt.get().dropStudent(studentOpt.get());
        System.out.println(studentOpt.get().getName() + " dropped from " + offeringOpt.get().getCourse().getCourseCode());
    }
}
