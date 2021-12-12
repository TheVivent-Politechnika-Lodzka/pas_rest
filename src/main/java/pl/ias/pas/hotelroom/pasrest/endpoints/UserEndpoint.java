package pl.ias.pas.hotelroom.pasrest.endpoints;

import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.exceptions.PermissionsException;
import pl.ias.pas.hotelroom.pasrest.managers.UserManager;
import pl.ias.pas.hotelroom.pasrest.model.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.UUID;


@RequestScoped
@Path("/user")
public class UserEndpoint {

    @Inject
    private UserManager userManager;

    // przykładowe zapytanie tworzące nowego użytkownika
    // http POST localhost:8080/PASrest-1.0-SNAPSHOT/api/user login=test password=test name=test surname=test

    //CREATE\\
    @POST
    @Consumes("application/json")
    public Response createUser(User user) {
        UUID createdUser;
        try {
            createdUser = userManager.addUser(user);
        } catch (ApplicationDaoException | PermissionsException e) {
            //Jako basic jest 500
            throw new WebApplicationException(e.getMessage());
        }
        return Response.created(URI.create("/user/" + createdUser)).build();
    }

    //UPDATE\\
    @POST
    @Path("/{id}")
    @Consumes("application/json")
    public void updateUser(@PathParam("id") String id, User user) {
        try {
            userManager.updateUser(userManager.getUserById(UUID.fromString(id)), user);
        } catch (ApplicationDaoException | PermissionsException e) {
            throw new WebApplicationException(e.getMessage());
        }
    }

    //DELETE\\
    @DELETE
    @Path("/{id}")
    @Consumes("application/json")
    public void archiveUser(@PathParam("id") String id) {
        try {
            userManager.removeUser(UUID.fromString(id));
        } catch (ApplicationDaoException | PermissionsException e) {
            throw new WebApplicationException(e.getMessage());
        }
    }

    //READ\\
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public User getUserById(@PathParam("id") String id) {
        try {
            return userManager.getUserById(UUID.fromString(id));
        } catch (ApplicationDaoException e) {
            throw new WebApplicationException(e.getMessage());
        }
    }

    @GET
    @Path("/search")
    @Produces("application/json")
    public List<User> getUsersContainsLogin(@QueryParam("login") String login) {
        try {
            return userManager.searchUsers(login);
        } catch (ApplicationDaoException e) {
            throw new WebApplicationException(e.getMessage());
        }
    }

    @GET
    @Path("/login/{login}")
    @Produces("application/json")
    public User getUserByLogin(@PathParam("login") String login) {
        try {
            return userManager.getUserByLogin(login);
        } catch (ApplicationDaoException e) {
            throw new WebApplicationException(e.getMessage());
        }
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    public List<User> getAllUsers() {
        return userManager.giveAllUsers();
    }

    //ARCHIVE\\
    @GET
    @Path("/archive")
    @Produces("application/json")
    public List<User> getAllArchiveUsers() {
        return userManager.giveArchiveUsers();
    }
}
