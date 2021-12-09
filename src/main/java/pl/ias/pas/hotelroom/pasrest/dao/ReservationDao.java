package pl.ias.pas.hotelroom.pasrest.dao;

import jakarta.enterprise.context.ApplicationScoped;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ReservationDao {

    private List<Reservation> reservationsRepository = Collections.synchronizedList(new ArrayList<Reservation>());

    public UUID addReservation(Reservation reserv) throws ApplicationDaoException {
        UUID id = UUID.randomUUID();
        Reservation newReservation;

        //sprawdzenie czy są podane daty rozpoczęcia/zakończenia
        if(reserv.getStartDate() != null) {
            if(reserv.getEndDate() != null) {
                newReservation = new Reservation(id, reserv.getUser(), reserv.getRoom(), reserv.getStartDate(), reserv.getEndDate());
            } else {
                newReservation = new Reservation(id, reserv.getUser(), reserv.getRoom(), reserv.getStartDate(), reserv.getEndDate());
            }
        } else {
            newReservation = new Reservation(id, reserv.getUser(), reserv.getRoom());
        }

        reservationsRepository.add(newReservation);
        return id;
    }

    public List<Reservation> getAllReservations() {
        return reservationsRepository;
    }

}
