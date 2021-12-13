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
        UUID createdRoom;
        try {
            createdRoom = roomManager.addRoom(room);
        } catch (ApplicationDaoException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.created(URI.create("/room/" + createdRoom)).build();
    }

    //UPDATE\\
    @POST
    @Path("/{id}")
    @Consumes("application/json")
    public Response updateRoom(@PathParam("id") String id, HotelRoom room) {
        try {
            roomManager.updateRoom(roomManager.getRoomById(UUID.fromString(id)), room);
        } catch (ApplicationDaoException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    //DELETE\\
    @DELETE
    @Path("/{id}")
    public Response removeRoom(@PathParam("id") String id) {
        try {
            roomManager.removeRoom(UUID.fromString(id));
        } catch (ApplicationDaoException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    //READ\\
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getRoomById(@PathParam("id") String id) {
        HotelRoom room;
        try {
            room = roomManager.getRoomById(UUID.fromString(id));
        } catch (ApplicationDaoException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Room not found").build();
        }
        return Response.ok(room).build();
    }

    @GET
    @Path("/number/{number}")
    @Produces("application/json")
    public Response getRoomByNumber(@PathParam("number") int number) {
        HotelRoom room;
        try {
            room = roomManager.getRoomByNumber(number);
        } catch (ApplicationDaoException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Room not found").build();
        }
        return Response.ok(room).build();
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    public Response getAllRooms() {
        return Response.ok(roomManager.giveAllRooms()).build();
    }
}
