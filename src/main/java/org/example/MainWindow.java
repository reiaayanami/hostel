package org.example;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private LoginPanel loginPanel;

    public MainWindow() {
        super("Хостел. Адміністрування"); // [cite: 3]

        loginPanel = new LoginPanel(this::openMainForm);
        add(loginPanel);

        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openMainForm(Employee employee) {
        getContentPane().removeAll();

        JTabbedPane tabbedPane = new JTabbedPane();

        // Створюємо панелі
        RoomsPanel roomsPanel = new RoomsPanel(this, employee);
        ClientsPanel clientsPanel = new ClientsPanel(this);
        SettlementsPanel settlementsPanel = new SettlementsPanel(this);

        // Додаємо вкладки згідно з ролями
        tabbedPane.addTab("Кімнати", roomsPanel);
        tabbedPane.addTab("Клієнти", clientsPanel);
        tabbedPane.addTab("Поселення", settlementsPanel);

        // Вкладка Працівники доступна лише Адміністратору [cite: 23, 103]
        String role = employee.getRole().toLowerCase();
        if (role.contains("адмін") || role.contains("admin")) {
            tabbedPane.addTab("Працівники", new EmployeesPanel(this, employee));
        }

        // Автоматичне оновлення даних при зміні вкладок
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            String title = tabbedPane.getTitleAt(selectedIndex);

            if (title.equals("Поселення")) {
                settlementsPanel.refreshData(); // Оновлює випадаючі списки
            } else if (title.equals("Кімнати")) {
                roomsPanel.refreshTable(); // Оновлює кольори та статуси
            }
        });

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(tabbedPane, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}