package pl.ias.pas.hotelroom.pasrest.managers;

import pl.ias.pas.hotelroom.pasrest.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.pasrest.dao.ReservationDao;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.exceptions.PermissionsException;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@RequestScoped
public class HotelRoomManager {

    @Inject
    private HotelRoomDao roomDao;
    @Inject
    private ReservationDao reservationDao;

    public HotelRoom getRoomByNumber(int number) throws ApplicationDaoException {
        return roomDao.getRoomByNumber(number);
    }

    public HotelRoom getRoomById(UUID id) throws ApplicationDaoException {
        return roomDao.getRoomById(id);
    }

    public UUID addRoom(HotelRoom room) throws ApplicationDaoException {
        UUID id = UUID.randomUUID();

        // sprawdzanie unikalności numeru
        if (roomDao.getRoomByNumber(room.getRoomNumber()) != null) {
            throw new ApplicationDaoException("500", "Room already exists");
        }

        // walidacja danych
        if(room.getRoomNumber() <= 0 || room.getPrice() <= 0 || room.getCapacity() <= 0) {
            throw new ApplicationDaoException("500", "Room can't be created, invalid data");
        }

        // wstawienie nowego pokoju
        HotelRoom newRoom = new HotelRoom(id, room.getRoomNumber(), room.getPrice(), room.getCapacity(), room.getDescription());


        return roomDao.addHotelRoom(newRoom);
    }

    public void removeRoom(UUID id) throws ApplicationDaoException {
        HotelRoom room = roomDao.getRoomById(id);
        for(Reservation reservation: reservationDao.getActualReservations()) {
            if(roomDao.getRoomById(reservation.getRoomId()) == room) {
                throw new ApplicationDaoException("500", "Room is already allocated");
            }
        }

        //sprawdzenie czy pokoj jest w bazie
        if (!roomDao.getAllRooms().contains(room)) {
            throw new ApplicationDaoException("500", "Room couldn't exist");
        }

        roomDao.removeRoom(room);
    }

    public void updateRoom(HotelRoom old, HotelRoom room) throws ApplicationDaoException, PermissionsException {
        if (!roomDao.getAllRooms().contains(old)) {
            throw new ApplicationDaoException("500", "Room doesn't exist");
        }

        if (roomDao.getAllRooms().contains(roomDao.getRoomByNumber(room.getRoomNumber()))) {
            throw new ApplicationDaoException("500", "Room with this number already exist");
        }

        if(!old.isAllocated()) {
            roomDao.updateHotelRoom(old, room);
        }
    }

    public List<HotelRoom> giveAllRooms() {
        return  roomDao.getAllRooms();
    }

}
