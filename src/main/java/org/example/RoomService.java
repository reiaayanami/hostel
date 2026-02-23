package org.example;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class RoomService {
    public static List<Room> getAll() {
        return DataStore.rooms;
    }

    public static void add(Room room) {
        DataStore.rooms.add(room);
    }

    public static void update(Room room) {
        // in-memory — просто заміна за номером
        DataStore.rooms.removeIf(r -> r.getNumber() == room.getNumber());
        DataStore.rooms.add(room);
    }

    public static void delete(int number) {
        DataStore.rooms.removeIf(r -> r.getNumber() == number);
    }

    public static Room getByNumber(int number) {
        return DataStore.rooms.stream().filter(r -> r.getNumber() == number).findFirst().orElse(null);
    }
}