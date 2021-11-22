package pl.ias.pas.hotelroom.pasrest.model;

import java.util.UUID;

public class HotelRoom {

    private UUID id;
    private int roomNumber;
    private int price;
    private int capacity;
    private String description;

    public HotelRoom() {
    }

    // nadawanie id to odpowiedzialność managera
    public HotelRoom(UUID id, int roomNumber, int price, int capacity, String description) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        // TODO czy > 0 ?
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        // TODO czy > 0 ?
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
