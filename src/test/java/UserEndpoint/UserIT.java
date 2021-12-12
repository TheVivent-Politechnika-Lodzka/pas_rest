package UserEndpoint;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pl.ias.pas.hotelroom.pasrest.model.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class UserIT {

    @Test
//    @Disabled
    public void getUserTest(){

        // stwórz zasób klienta
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/test/");
        User toRest = new User(UUID.randomUUID(),"login", "123","Jan", "Kowalski");
        Response response =  target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        assertEquals(response.getStatus(), 201);

        // pobierz utworzone id
        String id = response.getLocation().toString();
        id = id.substring(id.lastIndexOf("/")+1);

        // pobierz zasób klienta
        User fromRest = target.path("api").path("user").path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_JSON).get(User.class);

        // sprawdź czy pobrane dane są takie same jak wysłane
        assertEquals(toRest.getLogin(), fromRest.getLogin());
        assertEquals(toRest.getPassword(), fromRest.getPassword());
        assertEquals(toRest.getName(), fromRest.getName());
        assertEquals(toRest.getSurname(), fromRest.getSurname());
    }

}