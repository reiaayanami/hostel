package org.example;

import java.math.BigDecimal;
import java.util.List;

public class SettlementService {
    public static List<Settlement> getAll() {
        return DataStore.settlements;
    }

    public static void add(Settlement s) {
        s.setId(DataStore.settlements.size() + 1);
        DataStore.settlements.add(s);

        // Оновлюємо occupiedBeds в кімнаті
        Room r = RoomService.getByNumber(s.getRoomNumber());
        if (r != null) {
            r.setOccupiedBeds(r.getOccupiedBeds() + 1);
            if (r.getOccupiedBeds() >= r.getTotalBeds()) r.setStatus("Зайнято");
            RoomService.update(r);
        }
    }

    public static void delete(int id) {
        Settlement s = DataStore.settlements.stream().filter(set -> set.getId() == id).findFirst().orElse(null);
        if (s != null) {
            DataStore.settlements.remove(s);
            // Повертаємо місце в кімнаті
            Room r = RoomService.getByNumber(s.getRoomNumber());
            if (r != null) {
                r.setOccupiedBeds(Math.max(0, r.getOccupiedBeds() - 1));
                if (r.getOccupiedBeds() < r.getTotalBeds()) r.setStatus("Вільно");
                RoomService.update(r);
            }
        }
    }
}