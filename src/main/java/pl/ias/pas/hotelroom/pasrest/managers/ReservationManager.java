package pl.ias.pas.hotelroom.pasrest.managers;

import pl.ias.pas.hotelroom.pasrest.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.pasrest.dao.ReservationDao;
import pl.ias.pas.hotelroom.pasrest.dao.UserDao;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;
import pl.ias.pas.hotelroom.pasrest.model.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.naming.spi.ResolveResult;
import java.util.ArrayList;
import java.sql.Date;
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
        User user = userDao.getUserById(reservation.getUserId());
        HotelRoom room = roomDao.getRoomById(reservation.getRoomId());

        //sprawdzenie czy klient i pokoj istnieja
        if(user == null || room == null) {
            throw new ApplicationDaoException("500", "User or room doesn't exist");
        }

        //sprawdzenie czy nie jest juz przypadkiem wynajmowany
        if(room.isAllocated()) {
            throw new ApplicationDaoException("500", "Room is already occupied");
        }

        room.setAllocated(true);
        newReservation = new Reservation(id, user.getId(), room.getId());

        if (reservation.getStartDate() != null) {
            newReservation.setStartDate(reservation.getStartDate());
        } else {
            newReservation.setStartDate(System.currentTimeMillis());
        }

        if (reservation.getEndDate() != null) {
            newReservation.setEndDate(reservation.getEndDate());
        }

        return reservationDao.addReservation(newReservation);
    }

    public void archiveReservation(UUID id) throws ApplicationDaoException {

        if (reservationDao.getReservationById(id) == null) {
            throw new ApplicationDaoException("500", "Reservation does not exist");
        }

        reservationDao.endReservation(id);
//        if (!removedReservation) {
//            throw new ApplicationDaoException("500", "Reservation could not be removed, please try again");
//        }
//
//        roomDao.getRoomById(reservation.getRoomId()).setAllocated(false);
//        reservationDao.endReservation(reservation);
    }

    public void updateReservation(Reservation old, Reservation reservation) throws ApplicationDaoException {
        if (!getAllReservations().contains(old)) {
            throw new ApplicationDaoException("500", "Reservation does not exist");
        }

        reservationDao.updateReservation(old, reservation);
    }


    public List<Reservation> getArchivedReservation() {
        return reservationDao.getArchivedReservations();
    }

    public List<Reservation> getActiveReservation() {
        return reservationDao.getActiveReservations();
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.getAllReservations();
    }

    private List<Reservation> searchingReservation(boolean active) {
        List<Reservation> searching = new ArrayList<>();
        if(active) {
//            searching = getActualReservation();
        } else {
//            searching = getArchiveReservation();
        }
        return searching;
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
        for(Reservation res: searchingReservation(active)) {
            if(client) {
                if(res.getUserId().equals(user.getId())) {
                    reservation.add(res);
                }
            } else {
                if(res.getRoomId().equals(room.getId())) {
                    reservation.add(res);
                }
            }
        }
        return reservation;
    }



}
