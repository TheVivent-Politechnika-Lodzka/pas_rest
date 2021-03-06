package pl.ias.pas.hotelroom.pasrest.managers;

import pl.ias.pas.hotelroom.pasrest.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.pasrest.dao.ReservationDao;
import pl.ias.pas.hotelroom.pasrest.dao.UserDao;
import pl.ias.pas.hotelroom.pasrest.exceptions.ResourceAllocatedException;
import pl.ias.pas.hotelroom.pasrest.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.pasrest.exceptions.ValidationException;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;
import pl.ias.pas.hotelroom.pasrest.model.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.Instant;
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

    public Reservation getReservationById(UUID id) {
        return reservationDao.getReservationById(id);
    }

    public UUID addReservation(Reservation reservation, UUID userId, UUID roomId) {

        User user = userDao.getUserById(userId);
        HotelRoom room = roomDao.getRoomById(roomId);

        if (room.isActive() == false) {
            throw new ResourceNotFoundException("Room is not active");
        }

        Instant endDate = reservation.getEndDate();
        Instant startDate = reservation.getStartDate();
        if (startDate == null) startDate = Instant.now();
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new ValidationException("Start date is after end date");
        }

        final Instant finalStartDate = startDate;
        final Instant finalEndDate = endDate;

        //sprawdzenie czy nie jest juz przypadkiem wynajmowany
        List<Reservation> tmpReservations = reservationDao.customSearch((res) -> {
            // jeżeli to inny pokój, to nie ma znaczenia
            if (!res.getHotelRoom().getId().equals(roomId)) return false;

            if (res.getEndDate() == null) {
                // jeżeli nie ma konca to jest w trakcie
                // jeżeli start nowej rezerwacji jest po starej to jest konflikt
                if (finalStartDate.isAfter(res.getStartDate())) return true;
            } else {
                // jeżeli początek nowej rezerwacji jest w granicach dat starej to jest konflikt
                if (finalStartDate.isAfter(res.getStartDate()) && finalStartDate.isBefore(res.getEndDate()))
                    return true;
                // jeżeli koniec nowej rezerwacji jest w granicach dat starej to jest konflikt
                if (finalEndDate != null) {
                    if (finalEndDate.isAfter(res.getStartDate()) && finalEndDate.isBefore(res.getStartDate()))
                        return true;
                }
            }
            return false;
        });

        if (!tmpReservations.isEmpty()) {
            throw new ResourceAllocatedException("Room is already occupied");
        }

        Reservation newReservation = new Reservation(UUID.randomUUID(), startDate, endDate);

        return reservationDao.addReservation(newReservation, user.getId(), room.getId());
    }

    public void endReseravation(UUID id) {
        reservationDao.endReservation(id);
    }

    // za dużo myślenia nad implementacją
//    public void updateReservation(UUID reservationToUpdate, Reservation update) {
//        // sprawdzi czy user/room istnieje
//        if (update.getUserId() != null) {
//            userDao.getUserById(update.getUserId());
//        }
//        if(update.getRoomId() != null) {
//            roomDao.getRoomById(update.getRoomId());
//        }
//
//
//        reservationDao.updateReservation(reservationToUpdate, update);
//    }


    public List<Reservation> getEndedReservations() {
        return reservationDao.customSearch((reservation) -> !reservation.isActive());
    }

    public List<Reservation> getActiveReservations() {
        return reservationDao.customSearch((reservation) -> reservation.isActive());
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.getAllReservations();
    }

    public List<Reservation> searchReservations(String cliendId, String roomId, boolean includeArchived) {
        // sprawdź czy user istnieje
//        userDao.getUserById(cliendId);

        List<Reservation> result;
        result = reservationDao.customSearch((reservation) ->
                reservation.getUser().getId().toString().contains(cliendId) &&
                        reservation.getHotelRoom().getId().toString().contains(roomId) &&
                        (includeArchived || reservation.isActive())
        );

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("No reservations found");
        }

        return result;
    }

}
