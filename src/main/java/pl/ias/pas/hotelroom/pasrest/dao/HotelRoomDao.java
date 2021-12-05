package pl.ias.pas.hotelroom.pasrest.dao;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HotelRoomDao {

    private ArrayList<HotelRoom> roomsRepository = new ArrayList<>();

    synchronized public UUID addHotelRoom(HotelRoom room) throws ApplicationDaoException {

        UUID id = UUID.randomUUID();

        // sprawdzanie unikalności numeru
        for (HotelRoom currentHotelRoom : roomsRepository) {
            if (currentHotelRoom.getRoomNumber() == room.getRoomNumber()) {
                throw new ApplicationDaoException("500", "Room already exists");
            }
        }

        // sprawdzanie nieujemnej ceny i pojemności
//        if (room.getCapacity() < 0 || room.getPrice() < 0) {
//            throw new ApplicationDaoException("500", "Room can't exist");
//        }

        // wstawienie nowego pokoju
        HotelRoom newRoom = new HotelRoom(id, room.getRoomNumber(), room.getPrice(), room.getCapacity(), room.getDescription());
        roomsRepository.add(newRoom);
        return id;
    }

    public void updateHotelRoom(HotelRoom room) throws ApplicationDaoException {
        if (!roomsRepository.contains(room)) {
            throw new ApplicationDaoException("500", "Room doesn't exist");
        }

        HotelRoom oldRoom = getRoomById(room.getId());
        oldRoom.setPrice(room.getPrice());
        oldRoom.setCapacity(room.getCapacity());
        oldRoom.setDescription(room.getDescription());
    }

    synchronized public void removeRoom(HotelRoom room) throws ApplicationDaoException {
        if (!roomsRepository.contains(room)) {
            throw new ApplicationDaoException("500", "Room couldn't exist");
        }

        boolean removedRoom = roomsRepository.remove(room);

        if (!removedRoom) {
            throw new ApplicationDaoException("500", "Room couldn't be removed, please try again");
        }
    }

    synchronized public HotelRoom getRoomById(UUID id) throws ApplicationDaoException {
        for (HotelRoom room : roomsRepository) {
            if (room.getId().equals(id)) {
                return room;
            }
        }
        throw new ApplicationDaoException("500", "Room doesn't exist");
    }

    synchronized public HotelRoom getRoomByNumber(int number) throws ApplicationDaoException {
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
