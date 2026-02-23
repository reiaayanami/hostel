package org.example;

import javax.swing.*;
import java.awt.*;

public class UIUtils {
    public static void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(240, 240, 240));
    }

    public static void styleAll(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton) {
                styleButton((JButton) c);
            } else if (c instanceof Container) {
                styleAll((Container) c);
            }
        }
    }
}