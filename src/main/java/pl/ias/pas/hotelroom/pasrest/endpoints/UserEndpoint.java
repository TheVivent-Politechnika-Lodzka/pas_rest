package pl.ias.pas.hotelroom.pasrest.endpoints;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.exceptions.PermissionsException;
import pl.ias.pas.hotelroom.pasrest.managers.UserManager;
import pl.ias.pas.hotelroom.pasrest.model.User;

import java.net.URI;
import java.util.UUID;

@RequestScoped
@Path("/user")
public class UserEndpoint {

    @Inject
    private UserManager userManager;

    // przykładowe zapytanie tworzące nowego użytkownika
    // http PUT localhost:8080/api/user login=test password=test name=test surname=test
    @PUT
    @Consumes("application/json")
    public Response createUser(User user) {
        UUID createdUser = null;
        try {
            createdUser = userManager.addUser(user);
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        } catch (PermissionsException e) {
            e.printStackTrace();
        }
        return Response.created(URI.create("/user/" + createdUser)).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public User getUser(@PathParam("id") String id) {
        try {
            return userManager.getUserById(UUID.fromString(id));
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return null;
    }

}
