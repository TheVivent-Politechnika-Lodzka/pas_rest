package pl.ias.pas.hotelroom.pasrest.endpoints;

import pl.ias.pas.hotelroom.pasrest.managers.HotelRoomManager;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.net.URI;
import java.util.UUID;

//@RequestScope
@RequestScoped
@Path("/room")
public class HotelRoomEndpoint {

    @Inject
    private HotelRoomManager roomManager;

    // przykładowe zapytanie tworzące nowego użytkownika
    // http POST localhost:8080/PASrest-1.0-SNAPSHOT/api/room roomNumber=2 price=100 capacity=300 description=cosy

    // CREATE [POST -> 201]
    @POST
    @Path("/create")
    @Consumes("application/json")
    public Response createRoom(HotelRoom room) {
        UUID createdRoom = roomManager.addRoom(room);

        return Response.created(URI.create("/room/" + createdRoom)).build();
    }

    // UPDATE [POST -> 200]
    @POST
    @Path("/update/{id}")
    @Consumes("application/json")
    public Response updateRoom(@PathParam("id") String roomToUpdate, HotelRoom update, @Context SecurityContext context) {
        if (context.isUserInRole("CLIENT")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        UUID id = UUID.fromString(roomToUpdate);
        roomManager.updateRoom(id, update);

        return Response.ok().build();
    }

    // DELETE [DELETE -> 200]
    @DELETE
    @Path("/delete/{id}")
    public Response removeRoom(@PathParam("id") String id, @Context SecurityContext context) {
        if (context.isUserInRole("CLIENT")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        roomManager.deleteRoom(UUID.fromString(id));
        return Response.ok().build();
    }

    // READ [GET -> 200]
    @GET
    @Path("/get/id/{id}")
    @Produces("application/json")
    public Response getRoomById(@PathParam("id") String id) {
        HotelRoom room = roomManager.getRoomById(UUID.fromString(id));

        return Response.ok(room).build();
    }

    // READ [GET -> 200]
    @GET
    @Path("/get/number/{number}")
    @Produces("application/json")
    public Response getRoomByNumber(@PathParam("number") int number) {
        HotelRoom room = roomManager.getRoomByNumber(number);

        return Response.ok(room).build();
    }

    // READ [GET -> 200]
    @GET
    @Path("/get/all")
    @Produces("application/json")
    public Response getAllRooms() {
        return Response.ok(roomManager.getAllRooms()).build();
    }
}
