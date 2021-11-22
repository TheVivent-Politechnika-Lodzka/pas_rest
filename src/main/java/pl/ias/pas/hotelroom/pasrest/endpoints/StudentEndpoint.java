package pl.ias.pas.hotelroom.pasrest.endpoints;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pl.ias.pas.hotelroom.pasrest.exceptions.InvalidIDException;
import pl.ias.pas.hotelroom.pasrest.managers.StudentManager;
import pl.ias.pas.hotelroom.pasrest.model.Student;

import java.net.URI;
import java.util.ArrayList;

@RequestScoped
@Path("/student")
public class StudentEndpoint {

    @Inject
    private StudentManager studentManager;

    @GET
    @Produces({"application/xml", "application/json"})
    public ArrayList<Student> getAllStudents(){
        return new ArrayList<>();
    }

    @GET
    @Path("/{id}")
    @Produces({"application/xml", "application/json"})
    public Student getStudentById(@PathParam("id") String id){
        return new Student(id, "name"+id);
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    public Response addStudent(Student newStudent) {
        try {
            studentManager.addStudent(newStudent);
        } catch (InvalidIDException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.created(URI.create("student/" + newStudent.getId().toString())).build();
    }
}
