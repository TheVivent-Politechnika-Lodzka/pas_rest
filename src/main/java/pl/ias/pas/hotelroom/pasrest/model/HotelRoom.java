package pl.ias.pas.hotelroom.pasrest.model;

import lombok.*;

import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class HotelRoom {

    @EqualsAndHashCode.Include
    private UUID id;
    private int roomNumber;
    private int price;
    private int capacity;
    private String description;
    private boolean isAllocated = false;

    // nadawanie id to odpowiedzialność managera
    public HotelRoom(UUID id, int roomNumber, int price, int capacity, String description) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
    }

}
