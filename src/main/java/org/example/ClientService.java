package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientService {

    public static List<Client> getClients(String searchText) {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM clients WHERE full_name LIKE ? OR phone LIKE ?";
        String normalizedQuery = "%" + (searchText == null ? "" : searchText.trim()) + "%";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)
        ) {
            ps.setString(1, normalizedQuery);
            ps.setString(2, normalizedQuery);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clients.add(new Client(
                            rs.getInt("id"),
                            rs.getString("full_name"),
                            rs.getString("passport_data"),
                            rs.getString("phone"),
                            rs.getString("email")
                    ));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Пoмuлкa зaвaнтaжeння клiєнтiв: " + e.getMessage());
        }
        return clients;
    }

    public static boolean addClient(Client client) {
        String query = "INSERT INTO clients (full_name, passport_data, phone, email) VALUES (?, ?, ?, ?)";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)
        ) {
            ps.setString(1, client.getFullName());
            ps.setString(2, client.getPassportData());
            ps.setString(3, client.getPhone());
            ps.setString(4, client.getEmail());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Пoмuлкa рeєстрaцiї клiєнтa: " + e.getMessage());
            return false;
        }
    }
}