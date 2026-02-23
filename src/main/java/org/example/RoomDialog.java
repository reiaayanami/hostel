package org.example;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class RoomDialog extends JDialog {
    private JTextField txtNumber = new JTextField(10);
    private JComboBox<String> cmbType = new JComboBox<>(new String[]{"Стандарт", "Люкс"});
    private JSpinner spnTotal = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
    private JSpinner spnPrice = new JSpinner(new SpinnerNumberModel(300, 100, 2000, 50));
    private JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Вільно", "Зайнято", "Обслуговування"});

    private Room result;

    public RoomDialog(JFrame owner, Room room) {
        super(owner, room == null ? "Нова кімната" : "Редагувати кімнату", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Номер:"), gbc);
        gbc.gridx = 1; add(txtNumber, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Тип:"), gbc);
        gbc.gridx = 1; add(cmbType, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Місць загалом:"), gbc);
        gbc.gridx = 1; add(spnTotal, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Ціна/добу:"), gbc);
        gbc.gridx = 1; add(spnPrice, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Статус:"), gbc);
        gbc.gridx = 1; add(cmbStatus, gbc);

        JButton save = new JButton("Зберегти");
        UIUtils.styleButton(save);
        gbc.gridy = 5; gbc.gridwidth = 2; add(save, gbc);

        if (room != null) {
            txtNumber.setText(String.valueOf(room.getNumber()));
            cmbType.setSelectedItem(room.getType());
            spnTotal.setValue(room.getTotalBeds());
            spnPrice.setValue(room.getPricePerDay().intValue());
            cmbStatus.setSelectedItem(room.getStatus());
        }

        save.addActionListener(e -> {
            try {
                int num = Integer.parseInt(txtNumber.getText().trim());
                String type = (String) cmbType.getSelectedItem();
                int total = (int) spnTotal.getValue();
                BigDecimal price = BigDecimal.valueOf((int) spnPrice.getValue());
                String status = (String) cmbStatus.getSelectedItem();

                result = new Room(num, type, total, room != null ? room.getOccupiedBeds() : 0, price, status);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Невірні дані!");
            }
        });

        pack();
        setLocationRelativeTo(owner);
    }

    public int showDialog() {
        setVisible(true);
        return result != null ? JOptionPane.OK_OPTION : JOptionPane.CANCEL_OPTION;
    }

    public Room getRoom() { return result; }
}