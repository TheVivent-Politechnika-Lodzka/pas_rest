package pl.ias.pas.hotelroom.pasrest.endpoints;

import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ResourceAllocated;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ResourceAlreadyExistException;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ResourceNotFoundException;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ValidationException;
import pl.ias.pas.hotelroom.pasrest.managers.HotelRoomManager;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
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
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (ResourceAlreadyExistException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
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
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ResourceAllocated resourceAllocated) {
            return Response.status(Response.Status.CONFLICT).entity(resourceAllocated.getMessage()).build();
        }
        return Response.ok().build();
    }

    //DELETE\\
    @DELETE
    @Path("/{id}")
    public Response removeRoom(@PathParam("id") String id) {
        try {
            roomManager.removeRoom(UUID.fromString(id));
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ResourceAllocated resourceAllocated) {
            return Response.status(Response.Status.CONFLICT).entity(resourceAllocated.getMessage()).build();
        }
        return Response.ok().build();
    }

    //READ\\
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getRoomById(@PathParam("id") String id) {
        HotelRoom room = roomManager.getRoomById(UUID.fromString(id));

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Room not found").build();
        }

        return Response.ok(room).build();
    }

    @GET
    @Path("/number/{number}")
    @Produces("application/json")
    public Response getRoomByNumber(@PathParam("number") int number) {
        HotelRoom room = roomManager.getRoomByNumber(number);

        if (room == null) {
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
