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

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        if(price <= 0) {
            throw new RuntimeException();
        }
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if(capacity <= 0) {
            throw new RuntimeException();
        }
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
