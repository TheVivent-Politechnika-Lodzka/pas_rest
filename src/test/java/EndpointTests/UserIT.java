package EndpointTests;

import org.junit.jupiter.api.Test;
import pl.ias.pas.hotelroom.pasrest.model.User;

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


class UserIT {

    // TODO: zrobić tak, żeby context był pobierany z property/env
    //static final String BASE_URI = "http://localhost:8080/" + System.getenv("testContext");

    static final String BASE_URI = "http://localhost:8080/test";

    @Test
    public void insertUserGetUserById(){

        // stwórz zasób klienta
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        User toRest = new User(UUID.randomUUID(),"login", "123","Jan", "Kowalski");
        Response response =  target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        assertEquals(201, response.getStatus());

        // pobierz utworzone id
        String id = response.getLocation().toString();
        id = id.substring(id.lastIndexOf("/")+1);

        // pobierz zasób klienta
        User fromRest = target.path("api").path("user").path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_JSON).get(User.class);
        assertNotNull(fromRest);

        // sprawdź czy pobrane dane są takie same jak wysłane
        assertEquals(toRest.getLogin(), fromRest.getLogin());
        assertEquals(toRest.getPassword(), fromRest.getPassword());
        assertEquals(toRest.getName(), fromRest.getName());
        assertEquals(toRest.getSurname(), fromRest.getSurname());
    }

    @Test
    public void updateUser(){

        // stwórz zasób klienta
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        User toRest = new User(UUID.randomUUID(),"login2", "123","Jan", "Kowalski");
        Response response =  target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        assertEquals(201, response.getStatus());

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

        // zmodyfikuj dane
        fromRest.setLogin("login2");
        fromRest.setPassword("123");
        fromRest.setName("Janusz");
        fromRest.setSurname("Kowalski");

        // wysyłaj zmodyfikowane dane
        response = target.path("api").path("user").path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_JSON).post(Entity.json(fromRest));
        assertEquals(200, response.getStatus());

        // pobierz zasób klienta
        User fromRestModified = target.path("api").path("user").path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_JSON).get(User.class);
        assertEquals(fromRest.getLogin(), fromRestModified.getLogin());
        assertEquals(fromRest.getPassword(), fromRestModified.getPassword());
        assertEquals(fromRest.getName(), fromRestModified.getName());
        assertEquals(fromRest.getSurname(), fromRestModified.getSurname());
    }

    @Test
    public void deleteUser(){

        // stwórz zasób klienta
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        User toRest = new User(UUID.randomUUID(),"login3", "123","Jan", "Kowalski");
        Response response =  target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        assertEquals(201, response.getStatus());

        // pobierz utworzone id
        String id = response.getLocation().toString();
        id = id.substring(id.lastIndexOf("/")+1);

        // usuń zasób
        response = target.path("api").path("user").path("{id}").resolveTemplate("id", id).request().delete();
        assertEquals(200, response.getStatus());

        // pobierz zasób klienta
        Response isDeletedResponse = target.path("api").path("user").path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_JSON).get();
        assertEquals(404, isDeletedResponse.getStatus());
    }

    @Test
    public void getUserByLogin(){
        // stwórz zasób klienta
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        User toRest = new User(UUID.randomUUID(),"login4", "123","Jan", "Kowalski");
        Response response =  target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        assertEquals(201, response.getStatus());

        // pobierz zasób klienta
        User fromRest = target.path("api").path("user").path("login").path("{login}").resolveTemplate("login", "login4").request(MediaType.APPLICATION_JSON).get(User.class);
        assertNotNull(fromRest);

        // sprawdź czy pobrane dane są takie same jak wysłane
        assertEquals(toRest.getLogin(), fromRest.getLogin());
        assertEquals(toRest.getPassword(), fromRest.getPassword());
        assertEquals(toRest.getName(), fromRest.getName());
        assertEquals(toRest.getSurname(), fromRest.getSurname());
    }

    @Test
    public void getActiveUsers(){
        // stwórz zasób klienta
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        User toRest = new User(UUID.randomUUID(),"active1", "123","Jan", "Kowalski");
        target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        toRest.setLogin("active2");
        target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        toRest.setLogin("active3");
        target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));

        List<User> defaultReturn = target.path("api").path("user").path("all")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<User>>(){});
        assertTrue(defaultReturn.size() >= 3);

        for (User user : defaultReturn) {
            assertTrue(user.isActive());
        }

        List<User> scopedReturn = target.path("api").path("user").path("all")
                .queryParam("scope", "active")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<User>>(){});
        assertTrue(scopedReturn.size() >= 3);

        // zwrócone listy powinny być identyczne
        assertEquals(defaultReturn, scopedReturn);
    }

    @Test
    public void getArchivedUsers(){
        // stwórz zasób klienta
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        User toRest = new User(UUID.randomUUID(),"arhived1", "123","Jan", "Kowalski");

        Response response = target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        String id = response.getLocation().toString();
        id = id.substring(id.lastIndexOf("/")+1);
        target.path("api").path("user").path("{id}").resolveTemplate("id", id).request().delete();

        toRest.setLogin("arhived2");
        response = target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        id = response.getLocation().toString();
        id = id.substring(id.lastIndexOf("/")+1);
        target.path("api").path("user").path("{id}").resolveTemplate("id", id).request().delete();

        toRest.setLogin("arhived3");
        response = target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(toRest));
        id = response.getLocation().toString();
        id = id.substring(id.lastIndexOf("/")+1);
        target.path("api").path("user").path("{id}").resolveTemplate("id", id).request().delete();

        List<User> deletedUsers = target.path("api").path("user").path("all")
                .queryParam("scope", "archived")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<User>>(){});
        assertTrue(deletedUsers.size() >= 3);

        for (User user : deletedUsers) {
            assertFalse(user.isActive());
        }

    }


}