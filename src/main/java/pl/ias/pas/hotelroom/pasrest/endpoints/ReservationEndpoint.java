package pl.ias.pas.hotelroom.pasrest.endpoints;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.exceptions.PermissionsException;
import pl.ias.pas.hotelroom.pasrest.managers.HotelRoomManager;
import pl.ias.pas.hotelroom.pasrest.managers.ReservationManager;
import pl.ias.pas.hotelroom.pasrest.managers.UserManager;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;
import pl.ias.pas.hotelroom.pasrest.model.User;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Path("/reservation")
public class ReservationEndpoint {

    @Inject
    private ReservationManager resevationManager;
    @Inject
    private HotelRoomManager roomManager;
    @Inject
    private UserManager userManager;

    //CREATE\\
    @POST
    @Consumes("application/json")
    public Response createReservation(Reservation reservation) {
        UUID createdResevation = null;
        try {
            createdResevation = resevationManager.addReservation(reservation);
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return Response.created(URI.create("/reservation/" + createdResevation)).build();
    }

    //UPDATE\\
    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public void updateReservation(@PathParam("id") String id, Reservation reservation) {
        try {
            resevationManager.updateReservation(resevationManager.getReservationById(UUID.fromString(id)), reservation);
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
    }

    //DELETE\\
    @DELETE
    @Path("/{id}")
    @Consumes("application/json")
    public void archiveReservation(@PathParam("id") String id) {
        try {
            resevationManager.archiveReservation(UUID.fromString(id));
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
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
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    public List<Reservation> getAllReservation() {
        return resevationManager.giveAllReservations();
    }

    //CLIENT\\
    @GET
    @Path("/byClient/{id}")
    @Produces("application/json")
    public List<Reservation> getActiveReservationByClient(@PathParam("id") String id) {
        try {
            return resevationManager.giveReservation(UUID.fromString(id), true, false);
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return null;
    }
    @GET
    @Path("/byClient/arch/{id}")
    @Produces("application/json")
    public List<Reservation> getArchiveReservationByClient(@PathParam("id") String id) {
        try {
            return resevationManager.giveReservation(UUID.fromString(id), true, true);
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return null;
    }

    //ROOM\\
    @GET
    @Path("/byRoom/{id}")
    @Produces("application/json")
    public List<Reservation> getActiveReservationByRoom(@PathParam("id") String id) {
        try {
            return resevationManager.giveReservation(UUID.fromString(id), false, false);
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return null;
    }
    @GET
    @Path("/byRoom/arch/{id}")
    @Produces("application/json")
    public List<Reservation> getArchiveReservationByRoom(@PathParam("id") String id) {
        try {
            return resevationManager.giveReservation(UUID.fromString(id), false, true);
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return null;
    }


}
