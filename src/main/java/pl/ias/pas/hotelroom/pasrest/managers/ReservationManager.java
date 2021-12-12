package pl.ias.pas.hotelroom.pasrest.managers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.ias.pas.hotelroom.pasrest.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.pasrest.dao.ReservationDao;
import pl.ias.pas.hotelroom.pasrest.dao.UserDao;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;
import pl.ias.pas.hotelroom.pasrest.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestScoped
public class ReservationManager {

    @Inject
    private UserDao userDao;
    @Inject
    private ReservationDao reservationDao;
    @Inject
    private HotelRoomDao roomDao;

    public Reservation getReservationById(UUID id) throws ApplicationDaoException {
        return reservationDao.getReservationById(id);
    }

    public UUID addReservation(Reservation reservation) throws ApplicationDaoException {

        UUID id = UUID.randomUUID();
        Reservation newReservation;
        User user;
        HotelRoom room;

        //sprawdzenie czy klient i pokoj istnieja
        if(userDao.getUserById(reservation.getUserId()) != null && roomDao.getRoomById(reservation.getRoomId()) != null) {
            user = userDao.getUserById(reservation.getUserId());
            room = roomDao.getRoomById(reservation.getRoomId());
        } else {
            throw new ApplicationDaoException("500", "User or room doesn't exist");
        }

        //sprawdzenie czy nie jest juz przypadkiem wynajmowany
        if(room.isAllocated()) {
            throw new ApplicationDaoException("500", "Room is already occupied");
        }

        room.setAllocated(true);


        //sprawdzenie czy są podane daty rozpoczęcia/zakończenia
        if(reservation.getStartDate() != null) {
            if(reservation.getEndDate() != null) {
                newReservation = new Reservation(id, user.getId(), room.getId(), reservation.getStartDate(), reservation.getEndDate());
                return reservationDao.addReservationToArchive(newReservation);
            } else {
                newReservation = new Reservation(id, user.getId(), room.getId(), reservation.getStartDate());
            }
        } else {
            newReservation = new Reservation(id, user.getId(), room.getId());
        }
        return reservationDao.addReservation(newReservation);
    }

    public void archiveReservation(UUID id) throws ApplicationDaoException {
        Reservation reservation = reservationDao.getReservationById(id);

        if (!reservationDao.getActualReservations().contains(reservation)) {
            throw new ApplicationDaoException("500", "Reservation does not exist");
        }

        boolean removedReservation = reservationDao.getActualReservations().remove(reservation);

        if (!removedReservation) {
            throw new ApplicationDaoException("500", "Reservation could not be removed, please try again");
        }

        roomDao.getRoomById(reservation.getRoomId()).setAllocated(false);
        reservationDao.endReservation(reservation);
    }

    public void updateReservation(Reservation old, Reservation reservation) throws ApplicationDaoException {
        if (!getAllReservations().contains(old)) {
            throw new ApplicationDaoException("500", "Reservation does not exist");
        }

        reservationDao.updateReservation(old, reservation);
    }


    public List<Reservation> getArchiveReservation() {
        return reservationDao.getArchiveReservations();
    }

    public List<Reservation> getActualReservation() {
        return reservationDao.getActualReservations();
    }

    public List<Reservation> giveReservations(UUID id, boolean client, boolean active) throws ApplicationDaoException {
        List<Reservation> reservation = new ArrayList<>();
        User user = new User();
        HotelRoom room = new HotelRoom();
        if (client && userDao.getActiveUsers().contains(userDao.getUserById(id))) {
            user = userDao.getUserById(id);
        } else if (!client && roomDao.getAllRooms().contains(roomDao.getRoomById(id))) {
            room = roomDao.getRoomById(id);
        }
        for(Reservation res: getAllReservations()) {
            if(client) {
                if(res.getUserId().equals(user.getId())) {
                    if(active) {
                        reservation.add(res);
                    } else {
                        reservation.add(res);
                    }
                }
            } else {
                if(res.getRoomId().equals(room.getId())) {
                    if (active) {
                        reservation.add(res);
                    } else {
                        reservation.add(res);
                    }
                }
            }
        }
        return reservation;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> allReservations = new ArrayList<>(reservationDao.getActualReservations());
        allReservations.addAll(reservationDao.getArchiveReservations());
        return allReservations;
    }


}
