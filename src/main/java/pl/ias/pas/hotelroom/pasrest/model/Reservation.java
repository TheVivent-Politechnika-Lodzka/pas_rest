package pl.ias.pas.hotelroom.pasrest.model;

import java.util.Date;
import java.util.UUID;

public class Reservation {

    private UUID id;
    private Date startDate;
    private Date endDate;
//    private User user;
//    private HotelRoom room;
    private String uid;
    private String rid;

    public Reservation() {
    }

    // nadawanie id to odpowiedzialność managera
//    public Reservation(UUID id, User user, HotelRoom room, Date startDate, Date endDate) {
//        this(id, user, room, startDate);
//        this.endDate = endDate;
//    }
//
//    public Reservation(UUID id, User user, HotelRoom room, Date startDate) {
//        this(id, user, room);
//        this.startDate = startDate;
//    }
//
//    public Reservation(UUID id, User user, HotelRoom room) {
//        this.id = id;
//        this.user = user;
//        this.room = room;
//        this.startDate = new Date();
//        this.endDate = null;
//    }

    public Reservation(UUID id, String uid, String rid, Date startDate, Date endDate) {
        this(id, uid, rid, startDate);
        this.endDate = endDate;
    }

    public Reservation(UUID id, String uid, String rid, Date startDate) {
        this(id, uid, rid);
        this.startDate = startDate;
    }

    public Reservation(UUID id, String uid, String rid) {
        this.id = id;
        this.startDate = new Date();
        this.endDate = null;
        this.uid = uid;
        this.rid = rid;
    }

    public UUID getUid() {
        return UUID.fromString(uid);
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UUID getRid() {
        return UUID.fromString(rid);
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public UUID getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate() {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if(this.startDate.after(endDate)) {
            throw new RuntimeException();
        }
        this.endDate = endDate;
    }

//    public User getUser() {
//        return user;
//    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public void setRoom(HotelRoom room) {
//        this.room = room;
//    }
//
//    public HotelRoom getRoom() {
//        return room;
//    }

    public boolean isActive() {
        return endDate == null;
    }
}
