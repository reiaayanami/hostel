package org.example;

public class Room {
    private int roomNumber;
    private String type;
    private int totalBeds;
    private int occupiedBeds;
    private int price;
    private String status;

    public Room(int roomNumber, String type, int totalBeds, int occupiedBeds, int price, String status) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.totalBeds = totalBeds;
        this.occupiedBeds = occupiedBeds;
        this.price = price;
        this.status = status;
    }

    @Override
    public String toString() {
        return "№" + roomNumber + " (" + price + " грн)";
    }

    public int getRoomNumber() { return roomNumber; }
    public int getPrice() { return price; }
    public String getStatus() { return status; }
    public String getType() { return type; }
    public int getTotalBeds() { return totalBeds; }
    public int getOccupiedBeds() { return occupiedBeds; }
}