package pl.ias.pas.hotelroom.pasrest.endpoints;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.exceptions.PermissionsException;
import pl.ias.pas.hotelroom.pasrest.managers.UserManager;
import pl.ias.pas.hotelroom.pasrest.model.User;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RequestScoped
@Path("/user")
public class UserEndpoint {

    @Inject
    private UserManager userManager;

    // przykładowe zapytanie tworzące nowego użytkownika
    // http PUT localhost:8080/PASrest-1.0-SNAPSHOT/api/user login=test password=test name=test surname=test
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

    // TODO Update

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public User getUserById(@PathParam("id") String id) {
        try {
            return userManager.getUserById(UUID.fromString(id));
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    public List<User> getUsers(@QueryParam("logins") String logins) {
        try {
            return userManager.searchUsers(logins);
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Produces("application/json")
    public User getUser(@QueryParam("login") String login) {
        try {
            return userManager.getUserByLogin(login);
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return null;
    }
}
