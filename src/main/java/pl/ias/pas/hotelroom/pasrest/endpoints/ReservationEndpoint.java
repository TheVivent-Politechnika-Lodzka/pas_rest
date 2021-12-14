package pl.ias.pas.hotelroom.pasrest.endpoints;

import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.IDontKnowException;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ResourceAllocated;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ResourceNotFoundException;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ValidationException;
import pl.ias.pas.hotelroom.pasrest.managers.ReservationManager;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.UUID;

@RequestScoped
@Path("/reservation")
public class ReservationEndpoint {

    @Inject
    private ReservationManager reservationManager;

    // przykładowe zapytanie tworzące nowej rezerwacji, trza niestety uzupelniac
    // http POST localhost:8080/PASrest-1.0-SNAPSHOT/api/reservation uid=  rid=


    //CREATE\\
    @POST
    @Consumes("application/json")
    public Response createReservation(Reservation reservation) {
//        return Response.ok().build();

        UUID createdReservation;
        try {
            createdReservation = reservationManager.addReservation(reservation);
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ResourceAllocated resourceAllocated) {
            return Response.status(Response.Status.CONFLICT).entity(resourceAllocated.getMessage()).build();
        }

        return Response.created(URI.create("/reservation/" + createdReservation)).build();
    }

    //UPDATE\\
    @POST
    @Path("/{id}")
    @Consumes("application/json")
    public Response updateReservation(@PathParam("id") String id, Reservation reservation) {
        try {
            Reservation oldReservation = reservationManager.getReservationById(UUID.fromString(id));
            reservationManager.updateReservation(oldReservation, reservation);
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    //DELETE\\
    @DELETE
    @Path("/{id}")
    public Response archiveReservation(@PathParam("id") String id) {
        try {
            reservationManager.archiveReservation(UUID.fromString(id));
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IDontKnowException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    //READ\\
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getReservationById(@PathParam("id") String id) {
        Reservation reservation = reservationManager.getReservationById(UUID.fromString(id));

        if(reservation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(reservation).build();

    }

    @GET
    @Path("/all")
    @Produces("application/json")
    public Response getAllReservation() {
        return Response.ok(reservationManager.getAllReservations()).build();
    }

    //BYID\\
    @GET
    @Path("/search")
    @Produces("application/json")
    public Response getActiveReservationByClient(@QueryParam("clientId") String clientId, @QueryParam("archived") boolean archived) {
        UUID clienUUID = UUID.fromString(clientId);
        return Response.ok(reservationManager.searchReservations(clienUUID, archived)).build();
    }
}
