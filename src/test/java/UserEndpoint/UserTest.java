package UserEndpoint;

import org.junit.jupiter.api.Test;
import pl.ias.pas.hotelroom.pasrest.model.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class UserTest {

    @Test
    public void getUserTest(){
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/PASrest-1.0-SNAPSHOT/api/user");
//        User user = target.request(MediaType.APPLICATION_JSON).get(User.class);
        User toRest = new User(UUID.randomUUID(),"login", "123","Jan", "Kowalski");
        target.request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));

        WebTarget target2 = client.target("http://localhost:8080/PASrest-1.0-SNAPSHOT/api/user/" + toRest.getId());
        User fromRest = target2.request(MediaType.APPLICATION_JSON).get(User.class);
        assertEquals(toRest.getId(), fromRest.getId());
        System.out.println(fromRest);
    }

}