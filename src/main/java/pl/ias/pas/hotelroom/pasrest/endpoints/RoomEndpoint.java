package pl.ias.pas.hotelroom.pasrest.endpoints;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.exceptions.PermissionsException;
import pl.ias.pas.hotelroom.pasrest.managers.RoomManager;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;
import pl.ias.pas.hotelroom.pasrest.model.User;

import java.net.URI;
import java.util.UUID;

@RequestScoped
@Path("/room")
public class RoomEndpoint {

    @Inject
    private RoomManager roomManager;

    @PUT
    @Consumes("application/json")
    public Response createRoom(HotelRoom room) {
        UUID createdRoom = null;
        try {
            createdRoom = roomManager.addRoom(room);
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        } catch (PermissionsException e) {
            e.printStackTrace();
        }
        return Response.created(URI.create("/room/" + createdRoom)).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public HotelRoom getRoomById(@PathParam("id") String id) {
        try {
            return roomManager.getRoomById(UUID.fromString(id));
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Produces("application/json")
    public HotelRoom getRoom(@QueryParam("number") int number) {
        try {
            return roomManager.getRoomByNumber(number);
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return null;
    }
}
