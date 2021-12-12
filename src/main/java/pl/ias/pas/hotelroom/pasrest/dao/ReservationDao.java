package pl.ias.pas.hotelroom.pasrest.dao;

import jakarta.enterprise.context.ApplicationScoped;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;

import java.util.*;


@ApplicationScoped
public class ReservationDao {

    private List<Reservation> reservationsRepository = Collections.synchronizedList(new ArrayList<>());
    private List<Reservation> archiveRepository = Collections.synchronizedList(new ArrayList<>());

    public UUID addReservation(Reservation reservation) {
        reservationsRepository.add(reservation);
        return reservation.getId();
    }

    public UUID addReservationToArchive(Reservation reservation) {
        archiveRepository.add(reservation);
        return reservation.getId();
    }

    public Reservation getReservationById(UUID id) throws ApplicationDaoException {
        for (Reservation reservation : reservationsRepository) {
            if (reservation.getId().equals(id)) {
                return reservation;
            }
        }
        throw new ApplicationDaoException("500", "Reservation does not exist");
    }

    public void updateReservation(Reservation oldReservation, Reservation reservation) {
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
        if(reservation.getEndDate() == null) {
            reservation.setEndDate(new Date());
        }
        archiveRepository.add(reservation);
        reservationsRepository.remove(reservation);
    }

    public List<Reservation> getActualReservations() {
        return reservationsRepository;
    }
    public List<Reservation> getArchiveReservations() {
        return archiveRepository;
    }

}