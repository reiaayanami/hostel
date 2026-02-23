package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SettlementService {
    public static boolean createSettlement(Settlement s) {
        String query = "INSERT INTO settlements (room_number, client_id, check_in_date, check_out_date, total_cost, is_paid) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, s.getRoomNumber());
            ps.setInt(2, s.getClientId());
            ps.setDate(3, Date.valueOf(s.getCheckIn()));
            ps.setDate(4, Date.valueOf(s.getCheckOut()));
            ps.setInt(5, s.getTotalCost());
            ps.setBoolean(6, s.isPaid());

            int result = ps.executeUpdate();
            if (result > 0) {
                // Пiсля успiшнoгo зaпuсу oнoвлюємo кiмнaту нa "Зaйнятo"
                RoomService.updateRoomStatus(s.getRoomNumber(), "Зайнято");
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static List<Settlement> getAllSettlements() {
        List<Settlement> list = new ArrayList<>();
        String query = "SELECT * FROM settlements ORDER BY id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Settlement(
                        rs.getInt("id"), rs.getInt("room_number"), rs.getInt("client_id"),
                        rs.getDate("check_in_date").toLocalDate(), rs.getDate("check_out_date").toLocalDate(),
                        rs.getInt("total_cost"), rs.getBoolean("is_paid")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}