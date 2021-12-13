package EndpointTests;

import org.junit.jupiter.api.Test;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;
import pl.ias.pas.hotelroom.pasrest.model.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class HotelRoomIT {

    static final String BASE_URI = "http://localhost:8080/test";

    @Test
    public void insertRoomGetRoomById(){

        // stwórz zasób pokoju
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        HotelRoom toRest = new HotelRoom(UUID.randomUUID(),2, 30,22, "cosy");
        Response response =  target.path("api").path("room").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        assertEquals(201, response.getStatus());

        // pobierz utworzone id
        String id = response.getLocation().toString();
        id = id.substring(id.lastIndexOf("/")+1);

        // pobierz zasób pokoju
        HotelRoom fromRest = target.path("api").path("room").path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_JSON).get(HotelRoom.class);
        assertNotNull(fromRest);

        // sprawdź czy pobrane dane są takie same jak wysłane
        assertEquals(toRest.getRoomNumber(), fromRest.getRoomNumber());
        assertEquals(toRest.getPrice(), fromRest.getPrice());
        assertEquals(toRest.getCapacity(), fromRest.getCapacity());
        assertEquals(toRest.getDescription(), fromRest.getDescription());
    }

    @Test
    public void updateRoom(){

        // stwórz zasób pokoju
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        HotelRoom toRest = new HotelRoom(UUID.randomUUID(),3, 30,22, "cosy");
        Response response =  target.path("api").path("room").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        assertEquals(201, response.getStatus());

        // pobierz utworzone id
        String id = response.getLocation().toString();
        id = id.substring(id.lastIndexOf("/")+1);

        // pobierz zasób pokoju
        HotelRoom fromRest = target.path("api").path("room").path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_JSON).get(HotelRoom.class);

        // sprawdź czy pobrane dane są takie same jak wysłane
        assertEquals(toRest.getRoomNumber(), fromRest.getRoomNumber());
        assertEquals(toRest.getPrice(), fromRest.getPrice());
        assertEquals(toRest.getCapacity(), fromRest.getCapacity());
        assertEquals(toRest.getDescription(), fromRest.getDescription());

        // zmodyfikuj dane
        fromRest.setRoomNumber(420);
        fromRest.setPrice(21);
        fromRest.setCapacity(37);
        fromRest.setDescription("To Ty na mnie spojrzałes...");

        // wysyłaj zmodyfikowane dane
        response = target.path("api").path("room").path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_JSON).post(Entity.json(fromRest));
        assertEquals(200, response.getStatus());

        // pobierz zasób pokoju
        HotelRoom fromRestModified = target.path("api").path("room").path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_JSON).get(HotelRoom.class);

    }

}
