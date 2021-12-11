package pl.ias.pas.hotelroom.pasrest.managers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.ias.pas.hotelroom.pasrest.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.pasrest.dao.ReservationDao;
import pl.ias.pas.hotelroom.pasrest.dao.UserDao;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.exceptions.PermissionsException;
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

        User user;
        HotelRoom room;

        if(userDao.getUserById(reservation.getUid()) != null && roomDao.getRoomById(reservation.getRid()) != null) {
            user = userDao.getUserById(reservation.getUid());
            room = roomDao.getRoomById(reservation.getRid());
        } else {
            throw new ApplicationDaoException("500", "User or room doesn't exist");
        }

        //sprawdzenie czy klient istnieje (jako aktywny)
        if(!userDao.getActiveUsers().contains(user)) {
            throw new ApplicationDaoException("500", "User doesn't exist");
        }

        //sprawdzenie czy pokoj istnieje
        if(!roomDao.getAllRooms().contains(room)) {
            throw new ApplicationDaoException("500", "Room doesn't exist");
        }

        //sprawdzenie czy nie jest juz przypadkiem wynajmowany
        if(room.isAllocated()) {
            throw new ApplicationDaoException("500", "Room is already occupied");
        }

        room.setAllocation(true);
        return reservationDao.addReservation(reservation, user, room);
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

        roomDao.getRoomById(reservation.getRid()).setAllocation(false);
        reservationDao.endReservation(reservation);
    }

    public void updateReservation(Reservation old, Reservation reservation) throws ApplicationDaoException {
        reservationDao.updateReservation(old, reservation);
    }

    public User getUserFromId(UUID id) {
        for(User user: userDao.getAllUsers()) {
            if(user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public HotelRoom getRoomFromId(UUID id) {
        for(HotelRoom room: roomDao.getAllRooms()) {
            if(room.getId().equals(id)) {
                return room;
            }
        }
        return null;
    }

    public List<Reservation> giveArchiveReservation() {
        return reservationDao.getArchiveReservations();
    }

    public List<Reservation> giveActualReservation() {
        return reservationDao.getActualReservations();
    }

    public List<Reservation> giveReservation(UUID id, boolean client, boolean active) throws ApplicationDaoException {
        List<Reservation> reservation = new ArrayList<Reservation>();
        User user = new User();
        HotelRoom room = new HotelRoom();
        if (client && userDao.getActiveUsers().contains(userDao.getUserById(id))) {
            user = userDao.getUserById(id);
        } else if (!client && roomDao.getAllRooms().contains(roomDao.getRoomById(id))) {
            room = roomDao.getRoomById(id);
        }
        for(Reservation res: giveAllReservations()) {
            if(client) {
                if(res.getUid().equals(user.getId())) {
                    if(active) {
                        reservation.add(res);
                    } else {
                        reservation.add(res);
                    }
                }
            } else {
                if(res.getRid().equals(room.getId())) {
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

    public List<Reservation> giveAllReservations() {
        List<Reservation> allReservations = new ArrayList<Reservation>(reservationDao.getActualReservations());
        allReservations.addAll(reservationDao.getArchiveReservations());
        return allReservations;
    }


}
