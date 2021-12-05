package pl.ias.pas.hotelroom.pasrest.managers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.ias.pas.hotelroom.pasrest.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.exceptions.PermissionsException;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;


import java.util.UUID;

@RequestScoped
public class RoomManager {

    @Inject
    private HotelRoomDao roomDao;

    public HotelRoom getRoomByNumber(int number) throws ApplicationDaoException {
        return roomDao.getRoomByNumber(number);
    }

    public HotelRoom getRoomById(UUID id) throws ApplicationDaoException {
        return roomDao.getRoomById(id);
    }

    public UUID addRoom(HotelRoom room) throws ApplicationDaoException, PermissionsException {
        return roomDao.addHotelRoom(room);
    }

    public void removeRoom(UUID id) throws ApplicationDaoException, PermissionsException {
        HotelRoom room = roomDao.getRoomById(id);
        roomDao.removeRoom(room);
    }

    public void updateRoom(HotelRoom room) throws ApplicationDaoException, PermissionsException {
        roomDao.updateHotelRoom(room);
    }

}
