package pl.ias.pas.hotelroom.pasrest.dao;

import jakarta.enterprise.context.ApplicationScoped;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;
import pl.ias.pas.hotelroom.pasrest.model.User;

import java.util.*;

@ApplicationScoped
public class ReservationDao {

    private List<Reservation> reservationsRepository = Collections.synchronizedList(new ArrayList<Reservation>());
    private List<Reservation> archiveRepository = Collections.synchronizedList(new ArrayList<Reservation>());

    public UUID addReservation(Reservation reserv, User user, HotelRoom room) {
        UUID id = UUID.randomUUID();
        Reservation newReservation;

        //sprawdzenie czy są podane daty rozpoczęcia/zakończenia
        if(reserv.getStartDate() != null) {
            if(reserv.getEndDate() != null) {
                newReservation = new Reservation(id, user.getId().toString(), room.getId().toString(), reserv.getStartDate(), reserv.getEndDate());
            } else {
                newReservation = new Reservation(id, user.getId().toString(), room.getId().toString(), reserv.getStartDate());
            }
        } else {
            newReservation = new Reservation(id, user.getId().toString(), room.getId().toString());
        }

        reservationsRepository.add(newReservation);
        return id;
    }

    public Reservation getReservationById(UUID id) throws ApplicationDaoException {
        for (Reservation reservation : reservationsRepository) {
            if (reservation.getId().equals(id)) {
                return reservation;
            }
        }
        throw new ApplicationDaoException("500", "Reservation does not exist");
    }

    public void updateReservation(Reservation oldReservation, Reservation reservation) throws ApplicationDaoException {
        if (!reservationsRepository.contains(oldReservation)) {
            throw new ApplicationDaoException("500", "Reservation does not exist");
        }
        if(reservation.getRid() != null) {
            oldReservation.setRid(reservation.getRid().toString());
        }
        if(reservation.getUid() != null) {
            oldReservation.setUid(reservation.getUid().toString());
        }
        if(reservation.getStartDate() != null) {
            oldReservation.setStartDate(reservation.getStartDate());
        }
    }

    public void endReservation(Reservation reservation) {
        reservationsRepository.remove(reservation);
        archiveRepository.add(reservation);

        if(reservation.getEndDate() == null) {
            reservation.setEndDate(new Date());
        }
    }

    public List<Reservation> getActualReservations() {
        return reservationsRepository;
    }
    public List<Reservation> getArchiveReservations() {
        return archiveRepository;
    }

}
