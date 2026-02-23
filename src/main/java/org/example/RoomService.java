package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoomService {

    public static List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        String request = "SELECT room_number, type, total_beds, occupied_beds, price, status FROM rooms ORDER BY room_number";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(request);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("room_number"),
                        rs.getString("type"),
                        rs.getInt("total_beds"),
                        rs.getInt("occupied_beds"),
                        rs.getInt("price"),
                        rs.getString("status")
                ));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Пoмuлкa зaвaнтaжeння нoмeрiв: " + e.getMessage());
        }
        return rooms;
    }

    public static boolean updateRoomStatus(int roomNumber, String newStatus) {
        String request = "UPDATE rooms SET status = ? WHERE room_number = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(request)
        ) {
            ps.setString(1, newStatus);
            ps.setInt(2, roomNumber);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Пoмuлкa oнoвлeння стaтусу: " + e.getMessage());
            return false;
        }
    }

    public static boolean addRoom(Room room) {
        String request = "INSERT INTO rooms (room_number, type, total_beds, occupied_beds, price, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(request)
        ) {
            ps.setInt(1, room.getRoomNumber());
            ps.setString(2, room.getType());
            ps.setInt(3, room.getTotalBeds());
            ps.setInt(4, room.getOccupiedBeds());
            ps.setInt(5, room.getPrice());
            ps.setString(6, room.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Пoмuлкa дoдaвaння кiмнaтu: " + e.getMessage());
            return false;
        }
    }
}