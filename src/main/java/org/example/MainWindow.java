package org.example;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private LoginPanel loginPanel;

    public MainWindow() {
        super("Хостел");

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
        tabbedPane.addTab("Працівники", new EmployeesPanel(this, employee));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(tabbedPane, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}
