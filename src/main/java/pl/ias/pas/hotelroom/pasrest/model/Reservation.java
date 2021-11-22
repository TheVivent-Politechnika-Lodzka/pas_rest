package pl.ias.pas.hotelroom.pasrest.model;

import java.util.Date;
import java.util.UUID;

public class Reservation {

    private UUID id;
    private Date startDate;
    private Date endDate;
    private User user;
    private HotelRoom room;

    public Reservation() {
    }

    // nadawanie id to odpowiedzialność managera
    public Reservation(UUID id, User user, HotelRoom room, Date startDate, Date endDate) {
        this(id, user, room, startDate);
        this.endDate = endDate;
    }

    public Reservation(UUID id, User user, HotelRoom room, Date startDate) {
        this(id, user, room);
        this.startDate = startDate;
    }

    public Reservation(UUID id, User user, HotelRoom room) {
        this.id = id;
        this.user = user;
        this.room = room;
        this.startDate = new Date();
        this.endDate = null;
    }

    public UUID getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        // TODO walidacja czy endDate jest po startDate
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public HotelRoom getRoom() {
        return room;
    }

    public boolean isActive() {
        return endDate == null;
    }
}
