package pl.ias.pas.hotelroom.pasrest.endpoints;

import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.IDontKnowException;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ResourceAlreadyExistException;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ResourceNotFoundException;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ValidationException;
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
        }catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (ResourceAlreadyExistException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (IDontKnowException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

        return Response.created(URI.create("/user/" + createdUser)).build();
    }

    //UPDATE\\
    @POST
    @Path("/{id}")
    @Consumes("application/json")
    public Response updateUser(@PathParam("id") String id, User user) {
        try {
            UUID oldUser = UUID.fromString(id);
            userManager.updateUser(oldUser, user);
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @HEAD
    @Path("/activate/{id}")
    public Response activateUser(@PathParam("id") String id) {
        try {
            userManager.activateUser(UUID.fromString(id));
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    //DELETE\\
    @DELETE
    @Path("/{id}")
    @Consumes("application/json")
    public Response archiveUser(@PathParam("id") String id) {
        try {
            userManager.removeUser(UUID.fromString(id));
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    //READ\\
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getUserById(@PathParam("id") String id) {
        User user = userManager.getUserById(UUID.fromString(id), false);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        return Response.ok(user).build();
    }

    @GET
    @Path("/search")
    @Produces("application/json")
    public Response getUsersContainsLogin(@QueryParam("login") String login) {
        return Response.ok(userManager.searchUsers(login)).build();

    }

    @GET
    @Path("/login/{login}")
    @Produces("application/json")
    public Response getUserByLogin(@PathParam("login") String login) {
        User user = userManager.getUserByLogin(login);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        return Response.ok(user).build();

    }


    @GET
    @Path("/all")
    @Produces("application/json")
    public Response getAllUsers(@QueryParam("scope") String scope) {
        if (scope == null) scope = "active";
        List<User> toReturn;
        switch (scope) {
            case "active":
                toReturn = userManager.getAllActiveUsers();
                break;
            case "archived":
                toReturn = userManager.getAllArchivedUsers();
                break;
            case "all":
                toReturn = userManager.getAllUsers();
                break;
            default:
                return Response.status(Response.Status.BAD_REQUEST).entity("Wrong scope").build();
        }

        return Response.ok(toReturn).build();
    }

}
