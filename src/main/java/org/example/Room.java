package org.example;

import java.math.BigDecimal;

public class Room {
    private int number;
    private String type;
    private int totalBeds;
    private int occupiedBeds;
    private BigDecimal pricePerDay;
    private String status;

    public Room() {
    }

    public Room(int number, String type, int totalBeds, int occupiedBeds, BigDecimal pricePerDay, String status) {
        this.number = number;
        this.type = type;
        this.totalBeds = totalBeds;
        this.occupiedBeds = occupiedBeds;
        this.pricePerDay = pricePerDay;
        this.status = status;
    }

    public boolean isFree() {
        return "Вільно".equals(status) && occupiedBeds < totalBeds;
    }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getTotalBeds() { return totalBeds; }
    public void setTotalBeds(int totalBeds) { this.totalBeds = totalBeds; }
    public int getOccupiedBeds() { return occupiedBeds; }
    public void setOccupiedBeds(int occupiedBeds) { this.occupiedBeds = occupiedBeds; }
    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}