package pl.ias.pas.hotelroom.pasrest.endpoints;

import pl.ias.pas.hotelroom.pasrest.managers.ReservationManager;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.UUID;

//@RequestScope
@RequestScoped
@Path("/reservation")
public class ReservationEndpoint {

    @Inject
    private ReservationManager reservationManager;

    // przykładowe zapytanie tworzące nowej rezerwacji, trza niestety uzupelniac
    // http POST localhost:8080/PASrest-1.0-SNAPSHOT/api/reservation uid=  rid=


    // CREATE [POST -> 201]
    @POST
    @Path("/{userId}/{roomId}")
    @Consumes("application/json")
    public Response createReservation(
            @Valid Reservation reservation,
            @PathParam("userId") String userId,
            @PathParam("roomId") String roomId
    ) {
        if (reservation == null) {
            reservation = new Reservation();
        }
        UUID userUUID = UUID.fromString(userId);
        UUID roomUUID = UUID.fromString(roomId);
        UUID createdReservation = reservationManager.addReservation(reservation, userUUID, roomUUID);

        return Response.created(URI.create("/reservation/" + createdReservation)).build();
    }

    //UPDATE\\
//    @PostMapping(value = "/{id}", consumes = "application/json")
//    public ResponseEntity updateReservation(@PathVariable("id") String reservationToUpdate, @RequestBody Reservation update) {
//        UUID id = UUID.fromString(reservationToUpdate);
//        reservationManager.updateReservation(id, update);
//
//        return ResponseEntity.ok().build();
//    }

    // DELETE [HEAD -> 200]
    @HEAD
    @Path("/end/{id}")
    public Response archiveReservation(@PathParam("id") String id) {
        reservationManager.endReseravation(UUID.fromString(id));

        return Response.ok().build();
    }

    // READ [GET -> 200]
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getReservationById(@PathParam("id") String id) {
        Reservation reservation = reservationManager.getReservationById(UUID.fromString(id));

        return Response.ok(reservation).build();
    }

    // READ ALL [GET -> 200]
    @GET
    @Path("/all")
    @Produces("application/json")
    public Response getAllReservation() {
        return Response.ok(reservationManager.getAllReservations()).build();
    }

    // SEARCH [GET -> 200]
    @GET
    @Path("/search")
    @Produces("application/json")
    public Response getActiveReservationByClient(
            @QueryParam(value = "clientId") @DefaultValue("") String clientId,
            @QueryParam(value = "roomId") @DefaultValue("") String roomId,
            @QueryParam(value = "archived") @DefaultValue("true") boolean archived
    ) {


        List<Reservation> reservations = reservationManager
                .searchReservations(clientId, roomId, archived);
        return Response.ok(reservations).build();
    }
}
