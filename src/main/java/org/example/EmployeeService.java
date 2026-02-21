package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    public static Employee login(String login, String password) {
        String request = "SELECT id, full_name, role, salary FROM employee WHERE login = ? AND password = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(request);
        ) {
            ps.setString(1, login);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                int id = rs.getInt("id");
                String name = rs.getString("full_name");
                String role = rs.getString("role");
                int salary = rs.getInt("salary");

                return new Employee(id, name, role, salary, login, password);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Сталася помилка під час входу! " + e.getMessage());
        }

        return null;
    }

    public static List<Employee> getEmployees(String query) {
        List<Employee> employees = new ArrayList<>();
        String request = "SELECT id, full_name, role, login, salary FROM employee " +
                "WHERE full_name LIKE ? OR role LIKE ? OR login LIKE ? ORDER BY id";

        String normalizedQuery = "%" + (query == null ? "" : query.trim()) + "%";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(request);
        ) {
            ps.setString(1, normalizedQuery);
            ps.setString(2, normalizedQuery);
            ps.setString(3, normalizedQuery);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(new Employee(
                            rs.getInt("id"),
                            rs.getString("full_name"),
                            rs.getString("role"),
                            rs.getInt("salary"),
                            rs.getString("login"),
                            ""
                    ));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Сталася помилка під час завантаження працівників! " + e.getMessage());
        }

        return employees;
    }

    public static boolean addEmployee(Employee employee) {
        String request = "INSERT INTO employee (full_name, role, login, password, salary) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(request);
        ) {
            ps.setString(1, employee.getFullName());
            ps.setString(2, employee.getRole());
            ps.setString(3, employee.getLogin());
            ps.setString(4, employee.getPassword());
            ps.setInt(5, employee.getSalary());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Сталася помилка під час додавання працівника! " + e.getMessage());
            return false;
        }
    }

    public static boolean updateEmployee(Employee employee) {
        // Дoдaнo password = ? у зaпuт
        String request = "UPDATE employee SET full_name = ?, role = ?, login = ?, salary = ?, password = ? WHERE id = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(request);
        ) {
            ps.setString(1, employee.getFullName());
            ps.setString(2, employee.getRole());
            ps.setString(3, employee.getLogin());
            ps.setInt(4, employee.getSalary());
            ps.setString(5, employee.getPassword()); // Нoвuй пaрoль
            ps.setInt(6, employee.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Пomулкa oнoвлeння: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteEmployee(int id) {
        String request = "DELETE FROM employee WHERE id = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(request);
        ) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Сталася помилка під час видалення працівника! " + e.getMessage());
            return false;
        }
    }
}
