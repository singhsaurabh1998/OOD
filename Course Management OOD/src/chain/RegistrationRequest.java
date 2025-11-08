package chain;


import model.CourseOffering;
import model.Student;

public class RegistrationRequest {
    private final Student student;
    private final CourseOffering offering;

    public RegistrationRequest(Student student, CourseOffering offering) {
        this.student = student;
        this.offering = offering;
    }

    public Student getStudent() {
        return student;
    }

    public CourseOffering getOffering() {
        return offering;
    }
}