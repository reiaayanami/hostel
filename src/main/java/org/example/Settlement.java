package org.example;

import java.time.LocalDate;

public class Settlement {
    private int id;
    private int roomNumber;
    private int clientId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int totalCost;
    private boolean isPaid;

    public Settlement(int id, int roomNumber, int clientId, LocalDate checkIn, LocalDate checkOut, int totalCost, boolean isPaid) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.clientId = clientId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalCost = totalCost;
        this.isPaid = isPaid;
    }

    // Гeттeрu для тaблuцi тa сeрвiсу
    public int getId() { return id; }
    public int getRoomNumber() { return roomNumber; }
    public int getClientId() { return clientId; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public int getTotalCost() { return totalCost; }
    public boolean isPaid() { return isPaid; }
}