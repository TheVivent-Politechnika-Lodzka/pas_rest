package EndpointTests;

import org.junit.jupiter.api.Test;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;
import pl.ias.pas.hotelroom.pasrest.model.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReservationIT {

    static final String BASE_URL = "http://localhost:8080/test/";

    @Test
    public void insertReservationGetReservationById() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URL);
        User user = new User(UUID.randomUUID(), "inget1", "password", "name", "surname");
        Response response = target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(user));
        String userId = response.getLocation().toString();
        userId = userId.substring(userId.lastIndexOf("/") + 1);

        HotelRoom room = new HotelRoom(UUID.randomUUID(), 10, 20, 30, "");
        response = target.path("api").path("room").request(MediaType.APPLICATION_JSON).post(Entity.json(room));
        String roomId = response.getLocation().toString();
        roomId = roomId.substring(roomId.lastIndexOf("/") + 1);

        Long yesterday = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        Long tomorrow = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
        Reservation reservation = new Reservation(UUID.randomUUID(), UUID.fromString(userId), UUID.fromString(roomId), yesterday, tomorrow);

        response = target.path("api").path("reservation").request(MediaType.APPLICATION_JSON).post(Entity.json(reservation));
        String reservationId = response.getLocation().toString();
        reservationId = reservationId.substring(reservationId.lastIndexOf("/") + 1);

        Reservation reservationFromRest = target.path("api").path("reservation").path(reservationId).request().get(Reservation.class);
        assertNotNull(reservationFromRest);

        assertEquals(reservation.getStartDate(), reservationFromRest.getStartDate());
        assertEquals(reservation.getEndDate(), reservationFromRest.getEndDate());
        assertEquals(reservation.getRoomId(), reservationFromRest.getRoomId());
        assertEquals(reservation.getUserId(), reservationFromRest.getUserId());

        user = new User(UUID.randomUUID(), "inget2", "password", "name", "surname");
        response = target.path("api").path("user").request(MediaType.APPLICATION_JSON).post(Entity.json(user));
        userId = response.getLocation().toString();
        userId = userId.substring(userId.lastIndexOf("/") + 1);

        reservation = new Reservation(UUID.randomUUID(), UUID.fromString(userId), UUID.fromString(roomId), yesterday, tomorrow);

        response = target.path("api").path("reservation").request(MediaType.APPLICATION_JSON).post(Entity.json(reservation));
        assertEquals(400, response.getStatus());

    }

}
