package pl.ias.pas.hotelroom.pasrest.endpoints;

import pl.ias.pas.hotelroom.pasrest.managers.UserManager;
import pl.ias.pas.hotelroom.pasrest.model.Client;
import pl.ias.pas.hotelroom.pasrest.model.ResourceAdmin;
import pl.ias.pas.hotelroom.pasrest.model.User;
import pl.ias.pas.hotelroom.pasrest.model.UserAdmin;
import pl.ias.pas.hotelroom.pasrest.security.JwtUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
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

    @GET
    @Path("/hello")
    public String test() {
        System.out.println("########################");
        System.out.println("hello");
        System.out.println("########################");
        return "Hello";
    }

    // CREATE [POST -> 201]
    @POST
    @Path("/create")
    @Consumes("application/json")
    public Response createUser(@Valid Client user) {
        UUID createdUser = userManager.addUser(user);

        return Response.created(URI.create("/user/" + createdUser)).build();
    }

    // CREATE [POST -> 201]
    @POST
    @Path("/createUserAdmin")
    @Consumes("application/json")
    public Response createUser(@Valid UserAdmin user) {
        UUID createdUser = userManager.addUser(user);

        return Response.created(URI.create("/user/" + createdUser)).build();
    }

    // CREATE [POST -> 201]
    @POST
    @Path("/createResourceAdmin")
    @Consumes("application/json")
    public Response createUser(@Valid ResourceAdmin user) {
        UUID createdUser = userManager.addUser(user);

        return Response.created(URI.create("/user/" + createdUser)).build();
    }


    // UPDATE [POST -> 200]
    @POST
    @Path("/{id}")
    @Consumes("application/json")
    public Response updateUser(
            @PathParam("id") String userToUpdate,
            Client update,
            @Context SecurityContext context,
            @HeaderParam("Authorization") String token
    ) {
        if (context.isUserInRole("CLIENT")) {
            String userId = JwtUtils.getUserId(token);
            if (!userId.equals(userToUpdate)) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }

        UUID id = UUID.fromString(userToUpdate);
        userManager.updateUser(id, update);

        return Response.ok().build();
    }

    // ACTIVATE [HEAD -> 200]
    @HEAD
    @Path("/activate/{id}")
    public Response activateUser(@PathParam("id") String id) {
        userManager.activateUser(UUID.fromString(id));

        return Response.ok().build();
    }

    // DEACTIVATE [HEAD -> 200]
    @HEAD
    @Path("/deactivate/{id}")
    public Response archiveUser(@PathParam("id") String id) {
        userManager.deactivateUser(UUID.fromString(id));

        return Response.ok().build();
    }

    // READ [GET -> 200]
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getUserById(
            @PathParam("id") String id,
            @Context SecurityContext context,
            @HeaderParam("Authorization") String token
    ) {
        if (context.isUserInRole("CLIENT")) {
            String userId =
                    JwtUtils.getUserId(token);
            if (!userId.equals(id)) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        User user = userManager.getUserById(UUID.fromString(id), true);

        return Response.ok(user).build();
    }


    // SEARCH [GET -> 200]
    @GET
    @Path("/search/{login}")
    @Produces("application/json")
    public Response getUsersContainsLogin(@PathParam("login") String login) {
        return Response.ok(userManager.searchUsers(login)).build();
    }

    // READ [GET -> 200]
    @GET
    @Path("/login/{login}")
    @Produces("application/json")
    public Response getUserByLogin(@PathParam("login") String login) {
        User user = userManager.getUserByLogin(login);

        return Response.ok(user).build();
    }

    // READ [GET -> 200]
    @GET
    @Path("/all")
    @Produces("application/json")
    public Response getAllUsers(
            @QueryParam(value = "scope") @DefaultValue("") String scope
    ) {
        if (scope.isEmpty()) scope = "all";
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
                return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok(toReturn).build();
    }

}
