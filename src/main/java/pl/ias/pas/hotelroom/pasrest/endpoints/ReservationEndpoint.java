package pl.ias.pas.hotelroom.pasrest.endpoints;

import pl.ias.pas.hotelroom.pasrest.managers.ReservationManager;
import pl.ias.pas.hotelroom.pasrest.managers.UserManager;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;
import pl.ias.pas.hotelroom.pasrest.model.User;

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

//@RequestScope
@RequestScoped
@Path("/reservation")
public class ReservationEndpoint {

    @Inject
    private ReservationManager reservationManager;

    @Inject
    private UserManager userManager;

    // przykładowe zapytanie tworzące nowej rezerwacji, trza niestety uzupelniac
    // http POST localhost:8080/PASrest-1.0-SNAPSHOT/api/reservation uid=  rid=


    private Response createReservation(Reservation reservation, String userId, String roomId) {
        UUID userUUID = UUID.fromString(userId);
        UUID roomUUID = UUID.fromString(roomId);
        UUID createdReservation = reservationManager.addReservation(reservation, userUUID, roomUUID);

        return Response.created(URI.create("/reservation/" + createdReservation)).build();
    }

    // CREATE [POST -> 201]
    @POST
    @Path("/create/for/{userId}/{roomId}")
    @Consumes("application/json")
    public Response createReservationFor(
            @Valid Reservation reservation,
            @PathParam("userId") String userId,
            @PathParam("roomId") String roomId
    ) {
        if (reservation == null) {
            reservation = new Reservation();
        }
        return createReservation(reservation, userId, roomId);
    }

    @POST
    @Path("/create/logged/{roomId}")
    @Consumes("application/json")
    public Response createReservationLogged(
            @Valid Reservation reservation,
            @PathParam("roomId") String roomId,
            @Context SecurityContext securityContext
    ) {
        if (reservation == null) {
            reservation = new Reservation();
        }
        String login = securityContext.getUserPrincipal().getName();
        User user = userManager.getUserByLogin(login);

        return createReservation(reservation, user.getId().toString(), roomId);
    }

    // DELETE [HEAD -> 200]
    @HEAD
    @Path("/end/{id}")
    public Response archiveReservation(@PathParam("id") String id) {
        reservationManager.endReseravation(UUID.fromString(id));

        return Response.ok().build();
    }

    // READ [GET -> 200]
    @GET
    @Path("/get/id/{id}")
    @Produces("application/json")
    public Response getReservationById(@PathParam("id") String id) {
        Reservation reservation = reservationManager.getReservationById(UUID.fromString(id));

        return Response.ok(reservation).build();
    }

    // READ ALL [GET -> 200]
    @GET
    @Path("/get/all")
    @Produces("application/json")
    public Response getAllReservations() {
        return Response.ok(reservationManager.getAllReservations()).build();
    }

    // SEARCH [GET -> 200]
    @GET
    @Path("/search/admin")
    @Produces("application/json")
    public Response searchReservations(
            @QueryParam(value = "clientId") @DefaultValue("") String clientId,
            @QueryParam(value = "roomId") @DefaultValue("") String roomId,
            @QueryParam(value = "archived") @DefaultValue("true") boolean archived
    ) {

        List<Reservation> reservations = reservationManager
                .searchReservations(clientId, roomId, archived);
        return Response.ok(reservations).build();
    }

    @GET
    @Path("/search/logged")
    @Produces("application/json")
    public Response searchReservationsLogged(
            @QueryParam(value = "roomId") @DefaultValue("") String roomId,
            @QueryParam(value = "archived") @DefaultValue("true") boolean archived,
            @Context SecurityContext context
    ) {
        User user = userManager.getUserByLogin(context.getUserPrincipal().getName());
        String userId = user.getId().toString();

        List<Reservation> reservations = reservationManager
                .searchReservations(userId, roomId, archived);
        return Response.ok(reservations).build();
    }
}
