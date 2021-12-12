package pl.ias.pas.hotelroom.pasrest.dao;


import jakarta.enterprise.context.ApplicationScoped;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HotelRoomDao {

    private List<HotelRoom> roomsRepository = Collections.synchronizedList(new ArrayList<>());

    public UUID addHotelRoom(HotelRoom room) {
        roomsRepository.add(room);
        return room.getId();
    }

    public void updateHotelRoom(HotelRoom oldRoom, HotelRoom room) {

        oldRoom.setRoomNumber(room.getRoomNumber());
        oldRoom.setPrice(room.getPrice());
        oldRoom.setCapacity(room.getCapacity());
        if(room.getDescription() != null) {
            oldRoom.setDescription(room.getDescription());
        }
    }

    public void removeRoom(HotelRoom room) {
        roomsRepository.remove(room);
    }

    public HotelRoom getRoomById(UUID id) throws ApplicationDaoException {
        for (HotelRoom room : roomsRepository) {
            if (room.getId().equals(id)) {
                return room;
            }
        }
        throw new ApplicationDaoException("500", "Room doesn't exist");
    }

    public HotelRoom getRoomByNumber(int number) throws ApplicationDaoException {
        for (HotelRoom room : roomsRepository) {
            if (room.getRoomNumber() == number) {
                return room;
            }
        }
        throw new ApplicationDaoException("500", "Room doesn't exist");
    }

    public List<HotelRoom> getAllRooms() {
        return roomsRepository;
    }
}
