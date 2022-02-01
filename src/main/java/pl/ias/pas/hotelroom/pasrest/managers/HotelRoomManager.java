package pl.ias.pas.hotelroom.pasrest.managers;

import pl.ias.pas.hotelroom.pasrest.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.pasrest.exceptions.ResourceAllocatedException;
import pl.ias.pas.hotelroom.pasrest.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;


@RequestScoped
public class HotelRoomManager {

    @Inject
    private HotelRoomDao roomDao;

    public HotelRoom getRoomByNumber(int number) {
        return roomDao.getRoomByNumber(number);
    }

    public HotelRoom getRoomById(UUID id) {
        HotelRoom room = roomDao.getRoomById(id);
        if (room.isActive() == false) {
            throw new ResourceNotFoundException("Room with id " + id + " not found");
        }
        return room;
    }

    public UUID addRoom(HotelRoom room) {
        return roomDao.addHotelRoom(room);
    }

    public void deleteRoom(UUID roomId) {
        HotelRoom room = roomDao.getRoomById(roomId);

        if (room.isAllocated()) {
            throw new ResourceAllocatedException("Room is already allocated");
        }

        roomDao.deleteRoom(roomId);
    }

    public void updateRoom(UUID roomToUpdate, HotelRoom update) {

        if (getRoomById(roomToUpdate).isAllocated()) {
            throw new ResourceAllocatedException("Room is already allocated");
        }

        // TODO jakaś walidacja danych

        roomDao.updateHotelRoom(roomToUpdate, update);

    }

    public List<HotelRoom> getAllRooms() {
        return roomDao.getAllRooms();
    }

}
