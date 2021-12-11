package pl.ias.pas.hotelroom.pasrest.dao;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HotelRoomDao {

    private List<HotelRoom> roomsRepository = Collections.synchronizedList(new ArrayList<HotelRoom>());

    public UUID addHotelRoom(HotelRoom room) {
        roomsRepository.add(room);
        return room.getId();
    }

    public void updateHotelRoom(HotelRoom oldRoom, HotelRoom room) throws ApplicationDaoException {
        if (!roomsRepository.contains(oldRoom)) {
            throw new ApplicationDaoException("500", "Room doesn't exist");
        }

        oldRoom.setRoomNumber(room.getRoomNumber());
        oldRoom.setPrice(room.getPrice());
        oldRoom.setCapacity(room.getCapacity());
        if(room.getDescription() != null) {
            oldRoom.setDescription(room.getDescription());
        }
    }

    public void removeRoom(HotelRoom room) throws ApplicationDaoException {
        if (!roomsRepository.contains(room)) {
            throw new ApplicationDaoException("500", "Room couldn't exist");
        }

        boolean removedRoom = roomsRepository.remove(room);

        if (!removedRoom) {
            throw new ApplicationDaoException("500", "Room couldn't be removed, please try again");
        }
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
