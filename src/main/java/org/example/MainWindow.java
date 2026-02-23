package org.example;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        super("Хостел. Адміністрування та обслуговування");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);

        add(new LoginPanel(this));
        setVisible(true);
    }

    public void openMainForm(Employee employee) {
        getContentPane().removeAll();
        JTabbedPane tabs = new JTabbedPane();

        boolean isAdmin = employee.getRole().toLowerCase().contains("адмін") ||
                employee.getRole().toLowerCase().contains("admin");

        tabs.addTab("Кімнати", new RoomsPanel(this, employee));
        tabs.addTab("Клієнти", new ClientsPanel(this, employee));
        tabs.addTab("Поселення", new SettlementsPanel(this, employee));
        if (isAdmin) tabs.addTab("Працівники", new EmployeesPanel(this, employee));

        add(tabs);
        revalidate();
        repaint();
    }
}