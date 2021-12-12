package pl.ias.pas.hotelroom.pasrest.endpoints;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.managers.ReservationManager;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Path("/reservation")
public class ReservationEndpoint {

    @Inject
    private ReservationManager resevationManager;

    // przykładowe zapytanie tworzące nowej rezerwacji, trza niestety uzupelniac
    // http POST localhost:8080/PASrest-1.0-SNAPSHOT/api/reservation uid=  rid=


    //CREATE\\
    @POST
    @Consumes("application/json")
    public Response createReservation(Reservation reservation) {
        UUID createdResevation;
        try {
            createdResevation = resevationManager.addReservation(reservation);
        } catch (ApplicationDaoException e) {
            throw new WebApplicationException(e.getMessage());
        }
        return Response.created(URI.create("/reservation/" + createdResevation)).build();
    }

    //UPDATE\\
    @POST
    @Path("/{id}")
    @Consumes("application/json")
    public void updateReservation(@PathParam("id") String id, Reservation reservation) {
        try {
            resevationManager.updateReservation(resevationManager.getReservationById(UUID.fromString(id)), reservation);
        } catch (ApplicationDaoException e) {
            throw new WebApplicationException(e.getMessage());
        }
    }

    //DELETE\\
    @DELETE
    @Path("/{id}")
//    @Consumes("application/json")
    public void archiveReservation(@PathParam("id") String id) {
        try {
            resevationManager.archiveReservation(UUID.fromString(id));
        } catch (ApplicationDaoException e) {
            throw new WebApplicationException(e.getMessage());
        }
    }

    //READ\\
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Reservation getReservationById(@PathParam("id") String id) {
        try {
            return resevationManager.getReservationById(UUID.fromString(id));
        } catch (ApplicationDaoException e) {
            throw new WebApplicationException(e.getMessage());
        }
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    public List<Reservation> getAllReservation() {
        return resevationManager.getAllReservations();
    }

    //BYID\\
    @GET
    @Path("/byID/{id}")
    @Produces("application/json")
    public List<Reservation> getActiveReservationByClient(@PathParam("id") String id, @QueryParam("client") boolean client, @QueryParam("active") boolean active) {
        try {
            return resevationManager.giveReservations(UUID.fromString(id), client, active);
        } catch (ApplicationDaoException e) {
            throw new WebApplicationException(e.getMessage());
        }
    }
//    @GET
//    @Path("/byClient/arch/{id}")
//    @Produces("application/json")
//    public List<Reservation> getArchiveReservationByClient(@PathParam("id") String id) {
//        try {
//            return resevationManager.giveReservations(UUID.fromString(id), true, true);
//        } catch (ApplicationDaoException e) {
//            throw new WebApplicationException(e.getMessage());
//        }
//    }
//
//    //ROOM\\
//    @GET
//    @Path("/byRoom/{id}")
//    @Produces("application/json")
//    public List<Reservation> getActiveReservationByRoom(@PathParam("id") String id) {
//        try {
//            return resevationManager.giveReservations(UUID.fromString(id), false, false);
//        } catch (ApplicationDaoException e) {
//            throw new WebApplicationException(e.getMessage());
//        }
//    }
//    @GET
//    @Path("/byRoom/arch/{id}")
//    @Produces("application/json")
//    public List<Reservation> getArchiveReservationByRoom(@PathParam("id") String id) {
//        try {
//            return resevationManager.giveReservations(UUID.fromString(id), false, true);
//        } catch (ApplicationDaoException e) {
//            throw new WebApplicationException(e.getMessage());
//        }
//    }


}
