package EndpointTests;

import org.junit.jupiter.api.Test;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
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
        assertEquals(fromRest.getRoomNumber(), fromRestModified.getRoomNumber());
        assertEquals(fromRest.getPrice(), fromRestModified.getPrice());
        assertEquals(fromRest.getCapacity(), fromRestModified.getCapacity());
        assertEquals(fromRest.getDescription(), fromRestModified.getDescription());
    }

    @Test
    public void deleteRoom(){

        // stwórz zasób pokoju
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        HotelRoom toRest = new HotelRoom(UUID.randomUUID(),4, 30,22, "cosy");
        Response response =  target.path("api").path("room").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        assertEquals(201, response.getStatus());

        // pobierz utworzone id
        String id = response.getLocation().toString();
        id = id.substring(id.lastIndexOf("/")+1);

        // usuń zasób
        response = target.path("api").path("room").path("{id}").resolveTemplate("id", id).request().delete();
        assertEquals(200, response.getStatus());

        // pobierz zasób pokoju
        Response isDeletedResponse = target.path("api").path("room").path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_JSON).get();
        assertEquals(404, isDeletedResponse.getStatus());
    }

    @Test
    public void getRoomByNumber(){
        // stwórz zasób pokoju
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        HotelRoom toRest = new HotelRoom(UUID.randomUUID(),5, 30,22, "cosy");
        Response response =  target.path("api").path("room").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        assertEquals(201, response.getStatus());

        // pobierz zasób pokoju
        HotelRoom fromRest = target.path("api").path("room").path("number").path("{number}").resolveTemplate("number", 5).request(MediaType.APPLICATION_JSON).get(HotelRoom.class);
        assertNotNull(fromRest);

        // sprawdź czy pobrane dane są takie same jak wysłane
        assertEquals(toRest.getRoomNumber(), fromRest.getRoomNumber());
        assertEquals(toRest.getPrice(), fromRest.getPrice());
        assertEquals(toRest.getCapacity(), fromRest.getCapacity());
        assertEquals(toRest.getDescription(), fromRest.getDescription());
    }

    @Test
    public void getAllRooms(){
        // stwórz zasób pokoju
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        HotelRoom toRest = new HotelRoom(UUID.randomUUID(),6, 30,22, "bik");
        target.path("api").path("room").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        toRest.setRoomNumber(7);
        target.path("api").path("room").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        toRest.setRoomNumber(8);

        Response response = target.path("api").path("room").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        String id = response.getLocation().toString();
        id = id.substring(id.lastIndexOf("/")+1);
        target.path("api").path("room").path("{id}").resolveTemplate("id", id).request().delete();

        List<HotelRoom> allRooms = target.path("api").path("room").path("all")
                .queryParam("scope", "all")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<HotelRoom>>(){});
        assertTrue(allRooms.size() >= 3);

        for (HotelRoom room : allRooms) {
            assertFalse(room.isAllocated());
        }
    }

    @Test
    public void negativeUpdateRoom(){
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
        fromRest.setRoomNumber(-2);
        fromRest.setPrice(-3);
        fromRest.setCapacity(0);
        fromRest.setDescription(null);

        // wysyłaj zmodyfikowane dane
        response = target.path("api").path("room").path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_JSON).post(Entity.json(fromRest));
        assertEquals(200, response.getStatus());

        // pobierz zasób pokoju
        HotelRoom fromRestModified = target.path("api").path("room").path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_JSON).get(HotelRoom.class);
        assertNotEquals(fromRest.getRoomNumber(), fromRestModified.getRoomNumber());
        assertNotEquals(fromRest.getPrice(), fromRestModified.getPrice());
        assertNotEquals(fromRest.getCapacity(), fromRestModified.getCapacity());
        assertNotEquals(fromRest.getDescription(), fromRestModified.getDescription());

        // sprawdz, czy dane pozostaly
        assertEquals(toRest.getRoomNumber(), fromRestModified.getRoomNumber());
        assertEquals(toRest.getPrice(), fromRestModified.getPrice());
        assertEquals(toRest.getCapacity(), fromRestModified.getCapacity());
        assertEquals(toRest.getDescription(), fromRestModified.getDescription());
    }
}
