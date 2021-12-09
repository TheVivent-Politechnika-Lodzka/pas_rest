package pl.ias.pas.hotelroom.pasrest.managers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.ias.pas.hotelroom.pasrest.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.pasrest.dao.ReservationDao;
import pl.ias.pas.hotelroom.pasrest.dao.UserDao;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.exceptions.PermissionsException;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;
import pl.ias.pas.hotelroom.pasrest.model.User;

import java.util.UUID;

@RequestScoped
public class ReservationManager {

    @Inject
    private UserDao userDao;
    @Inject
    private ReservationDao reservDao;
    @Inject
    private HotelRoomDao roomDao;

    public UUID addReservation(Reservation reservation) throws ApplicationDaoException {

        //sprawdzenie czy klient istnieje
        if(!userDao.getAllUsers().contains(reservation.getUser())) {
            throw new ApplicationDaoException("500", "User doesn't exist");
            //FIXME Czy my w sumie chcemy go ewentualnie dodawać?
        }

        //sprawdzenie czy pokoj istnieje
        if(!roomDao.getAllRooms().contains(reservation.getRoom())) {
            throw new ApplicationDaoException("500", "Room doesn't exist");
        }

        //sprawdzenie czy nie jest juz przypadkiem wynajmowany
        for(Reservation reserv: reservDao.getAllReservations()) {
            if(reserv.getRoom() == reservation.getRoom()) {
                throw new ApplicationDaoException("500", "Room is already occupied");
            }
        }

        return reservDao.addReservation(reservation);
    }
}
