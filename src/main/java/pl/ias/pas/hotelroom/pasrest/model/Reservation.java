package pl.ias.pas.hotelroom.pasrest.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Reservation {

    @EqualsAndHashCode.Include
    private UUID id;
    private Date startDate;
    private Date endDate;
    private UUID userId;
    private UUID roomId;

    public Reservation(UUID id, UUID userId, UUID roomId, Date startDate, Date endDate) {
        this(id, userId, roomId, startDate);
        this.endDate = endDate;
    }

    public Reservation(UUID id, UUID userId, UUID roomId, Date startDate) {
        this(id, userId, roomId);
        this.startDate = startDate;
    }

    public Reservation(UUID id, UUID userId, UUID roomId) {
        this.id = id;
        this.startDate = new Date();
        this.endDate = null;
        this.userId = userId;
        this.roomId = roomId;
    }

    public void setUserId(String userId) {
        this.userId = UUID.fromString(userId);
    }

    public void setRoomId(String roomId) {
        this.roomId = UUID.fromString(roomId);
    }


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public void setEndDate(Date endDate) {
        if(this.startDate.after(endDate)) {
            throw new RuntimeException();
        }
        this.endDate = endDate;
    }

    public boolean isActive() {
        return endDate == null;
    }
}
