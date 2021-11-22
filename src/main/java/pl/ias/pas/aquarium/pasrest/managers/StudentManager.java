package pl.ias.pas.aquarium.pasrest.managers;

import jakarta.enterprise.context.RequestScoped;
import pl.ias.pas.aquarium.pasrest.exceptions.InvalidIDException;
import pl.ias.pas.aquarium.pasrest.model.Student;

@RequestScoped
public class StudentManager {

    public void addStudent(Student newStudent) throws InvalidIDException {
        System.out.println("addStudent: " + newStudent);

        if("13".equals(newStudent.getId())) {
            throw new InvalidIDException();
        }

        // if student in studentRepo: throw

        // studentRepo.addStudent
        // zastanowić się, czy chcemy tu wyłapać wyjątek repo czy dalej
    }

}
