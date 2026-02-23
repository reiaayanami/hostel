package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    public static List<Room> rooms = new ArrayList<>();
    public static List<Client> clients = new ArrayList<>();
    public static List<Settlement> settlements = new ArrayList<>();

    static {
        // Тестові дані
        rooms.add(new Room(101, "Стандарт", 4, 0, new BigDecimal("300"), "Вільно"));
        rooms.add(new Room(102, "Люкс", 2, 2, new BigDecimal("500"), "Зайнято"));
        rooms.add(new Room(103, "Стандарт", 3, 0, new BigDecimal("250"), "Обслуговування"));

        clients.add(new Client(1, "Іванов Іван Іванович", "АА123456", "0671234567", "ivan@gmail.com"));
        clients.add(new Client(2, "Петренко Марія Петрівна", "ВВ654321", "0509876543", ""));
    }

    public static List<Room> getFreeRooms() {
        return rooms.stream().filter(r -> r.isFree()).toList();
    }
}