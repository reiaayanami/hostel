package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Settlement {
    private int id;
    private int roomNumber;
    private int clientId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BigDecimal totalCost = BigDecimal.ZERO;
    private BigDecimal payment = BigDecimal.ZERO;
    private boolean breakfast;
    private boolean laundry;
    private boolean towels;

    public Settlement() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public BigDecimal getPayment() { return payment; }
    public void setPayment(BigDecimal payment) { this.payment = payment; }
    public boolean isBreakfast() { return breakfast; }
    public void setBreakfast(boolean breakfast) { this.breakfast = breakfast; }
    public boolean isLaundry() { return laundry; }
    public void setLaundry(boolean laundry) { this.laundry = laundry; }
    public boolean isTowels() { return towels; }
    public void setTowels(boolean towels) { this.towels = towels; }

    public BigDecimal calculateCost(BigDecimal pricePerDay) {
        if (checkIn == null || checkOut == null || checkOut.isBefore(checkIn)) {
            return BigDecimal.ZERO;
        }
        long days = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        BigDecimal base = pricePerDay.multiply(BigDecimal.valueOf(days));
        if (breakfast) base = base.add(BigDecimal.valueOf(80L * days));
        if (laundry)   base = base.add(BigDecimal.valueOf(50L * days));
        if (towels)    base = base.add(BigDecimal.valueOf(30L * days));
        return base;
    }
}