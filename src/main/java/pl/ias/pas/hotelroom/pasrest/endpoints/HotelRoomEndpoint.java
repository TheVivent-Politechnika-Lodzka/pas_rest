package pl.ias.pas.hotelroom.pasrest.endpoints;

import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.exceptions.PermissionsException;
import pl.ias.pas.hotelroom.pasrest.managers.HotelRoomManager;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Path("/room")
public class HotelRoomEndpoint {

    @Inject
    private HotelRoomManager roomManager;

    // przykładowe zapytanie tworzące nowego użytkownika
    // http POST localhost:8080/PASrest-1.0-SNAPSHOT/api/room roomNumber=2 price=100 capacity=300 description=cosy

    //CREATE\\
    @POST
    @Consumes("application/json")
    public Response createRoom(HotelRoom room) {
        UUID createdRoom = null;
        try {
            createdRoom = roomManager.addRoom(room);
        } catch (ApplicationDaoException | PermissionsException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return Response.created(URI.create("/room/" + createdRoom)).build();
    }

    //UPDATE\\
    @POST
    @Path("/{id}")
    @Consumes("application/json")
    public HotelRoom updateRoom(@PathParam("id") String id, HotelRoom room) {
        try {
            roomManager.updateRoom(roomManager.getRoomById(UUID.fromString(id)), room);
            return room;
        } catch (ApplicationDaoException | PermissionsException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return room;
    }

    //DELETE\\
    @DELETE
    @Path("/{id}")
//    @Consumes("application/json")
    public void removeRoom(@PathParam("id") String id) {
        try {
            roomManager.removeRoom(UUID.fromString(id));
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
    }

    //READ\\
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
    @Path("/number/{number}")
    @Produces("application/json")
    public HotelRoom getRoomByNumber(@PathParam("number") int number) {
        try {
            return roomManager.getRoomByNumber(number);
        } catch (ApplicationDaoException e) {
            // TODO poprawna obsługa błędów
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    public List<HotelRoom> getAllRooms() {
        return roomManager.giveAllRooms();
    }
}
