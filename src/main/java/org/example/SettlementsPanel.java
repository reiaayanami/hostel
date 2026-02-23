package org.example;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

public class SettlementsPanel extends JPanel {

    private final JFrame owner;
    private final Employee currentUser;
    private DefaultTableModel model;
    private JTable table;

    public SettlementsPanel(JFrame owner, Employee user) {
        this.owner = owner;
        this.currentUser = user;
        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(new String[]{
                "ID", "Кімната", "Клієнт ID", "Заїзд", "Виїзд", "Вартість", "Оплата"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton addButton = new JButton("Нове поселення");
        JButton editButton = new JButton("Редагувати / Виселити");
        JButton deleteButton = new JButton("Видалити");

        UIUtils.styleButton(addButton);
        UIUtils.styleButton(editButton);
        UIUtils.styleButton(deleteButton);

        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);

        add(controlPanel, BorderLayout.NORTH);

        // Слухачі кнопок
        addButton.addActionListener(e -> showSettlementDialog(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (Integer) model.getValueAt(selectedRow, 0);
                Settlement settlement = SettlementService.getAll().stream()
                        .filter(s -> s.getId() == id)
                        .findFirst()
                        .orElse(null);
                if (settlement != null) {
                    showSettlementDialog(settlement);
                }
            } else {
                JOptionPane.showMessageDialog(owner, "Оберіть поселення для редагування", "Попередження", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int choice = JOptionPane.showConfirmDialog(
                        owner,
                        "Ви дійсно хочете видалити це поселення?",
                        "Підтвердження видалення",
                        JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) {
                    int id = (Integer) model.getValueAt(selectedRow, 0);
                    SettlementService.delete(id);
                    refresh();
                }
            } else {
                JOptionPane.showMessageDialog(owner, "Оберіть поселення для видалення", "Попередження", JOptionPane.WARNING_MESSAGE);
            }
        });

        refresh();
    }

    // Публічний метод, який викликається з RoomsPanel
    public void showNewSettlementDialog() {
        showSettlementDialog(null);
    }

    private void showSettlementDialog(Settlement existing) {
        JDialog dialog = new JDialog(owner, existing == null ? "Нове поселення" : "Редагувати поселення", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Вибір кімнати (тільки вільні)
        JComboBox<Room> roomCombo = new JComboBox<>(DataStore.getFreeRooms().toArray(new Room[0]));
        roomCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Room r) {
                    setText(r.getNumber() + " (" + r.getType() + ", " + r.getPricePerDay() + " грн/добу)");
                }
                return c;
            }
        });

        // Вибір клієнта
        JComboBox<Client> clientCombo = new JComboBox<>(DataStore.clients.toArray(new Client[0]));
        clientCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Client cl) {
                    setText(cl.getId() + " - " + cl.getFullName());
                }
                return c;
            }
        });

        // Дати
        Properties p = new Properties();
        p.put("text.today", "Сьогодні");
        p.put("text.month", "Місяць");
        p.put("text.year", "Рік");

        UtilDateModel modelIn = new UtilDateModel();
        JDatePanelImpl datePanelIn = new JDatePanelImpl(modelIn, p);
        JDatePickerImpl dateIn = new JDatePickerImpl(datePanelIn, new DateLabelFormatter());

        UtilDateModel modelOut = new UtilDateModel();
        JDatePanelImpl datePanelOut = new JDatePanelImpl(modelOut, p);
        JDatePickerImpl dateOut = new JDatePickerImpl(datePanelOut, new DateLabelFormatter());

        JCheckBox breakfastCheck = new JCheckBox("Сніданок (+80 грн/день)");
        JCheckBox laundryCheck   = new JCheckBox("Прання (+50 грн/день)");
        JCheckBox towelsCheck    = new JCheckBox("Рушники (+30 грн/день)");

        JTextField paymentField = new JTextField("0", 10);
        JLabel costLabel = new JLabel("Вартість: 0 грн");

        // Розміщення елементів
        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Кімната:"), gbc);
        gbc.gridx = 1; dialog.add(roomCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Клієнт:"), gbc);
        gbc.gridx = 1; dialog.add(clientCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; dialog.add(new JLabel("Дата заїзду:"), gbc);
        gbc.gridx = 1; dialog.add(dateIn, gbc);

        gbc.gridx = 0; gbc.gridy = 3; dialog.add(new JLabel("Дата виїзду:"), gbc);
        gbc.gridx = 1; dialog.add(dateOut, gbc);

        gbc.gridx = 0; gbc.gridwidth = 2; gbc.gridy = 4; dialog.add(breakfastCheck, gbc);
        gbc.gridy = 5; dialog.add(laundryCheck, gbc);
        gbc.gridy = 6; dialog.add(towelsCheck, gbc);

        gbc.gridy = 7; gbc.gridwidth = 1; dialog.add(new JLabel("Оплата:"), gbc);
        gbc.gridx = 1; dialog.add(paymentField, gbc);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; dialog.add(costLabel, gbc);

        JButton saveButton = new JButton("Зберегти");
        UIUtils.styleButton(saveButton);
        gbc.gridy = 9; dialog.add(saveButton, gbc);

        // Розрахунок вартості в реальному часі
        Runnable updateCost = () -> {
            Room room = (Room) roomCombo.getSelectedItem();
            Date inDate = (Date) dateIn.getModel().getValue();
            Date outDate = (Date) dateOut.getModel().getValue();

            if (room != null && inDate != null && outDate != null) {
                LocalDate checkIn = inDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate checkOut = outDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Settlement temp = new Settlement();
                temp.setCheckIn(checkIn);
                temp.setCheckOut(checkOut);
                temp.setBreakfast(breakfastCheck.isSelected());
                temp.setLaundry(laundryCheck.isSelected());
                temp.setTowels(towelsCheck.isSelected());

                BigDecimal cost = temp.calculateCost(room.getPricePerDay());
                costLabel.setText("Вартість: " + cost + " грн");
            } else {
                costLabel.setText("Вартість: —");
            }
        };

        roomCombo.addActionListener(e -> updateCost.run());
        dateIn.addActionListener(e -> updateCost.run());
        dateOut.addActionListener(e -> updateCost.run());
        breakfastCheck.addActionListener(e -> updateCost.run());
        laundryCheck.addActionListener(e -> updateCost.run());
        towelsCheck.addActionListener(e -> updateCost.run());

        // Збереження
        saveButton.addActionListener(e -> {
            Room room = (Room) roomCombo.getSelectedItem();
            Client client = (Client) clientCombo.getSelectedItem();
            Date inDate = (Date) dateIn.getModel().getValue();
            Date outDate = (Date) dateOut.getModel().getValue();

            if (room == null || client == null || inDate == null || outDate == null) {
                JOptionPane.showMessageDialog(dialog, "Заповніть усі обов'язкові поля!", "Помилка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate checkIn = inDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOut = outDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                JOptionPane.showMessageDialog(dialog, "Дата виїзду повинна бути пізніше дати заїзду!", "Помилка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Settlement settlement = new Settlement();
            settlement.setRoomNumber(room.getNumber());
            settlement.setClientId(client.getId());
            settlement.setCheckIn(checkIn);
            settlement.setCheckOut(checkOut);
            settlement.setBreakfast(breakfastCheck.isSelected());
            settlement.setLaundry(laundryCheck.isSelected());
            settlement.setTowels(towelsCheck.isSelected());

            BigDecimal calculatedCost = settlement.calculateCost(room.getPricePerDay());
            settlement.setTotalCost(calculatedCost);

            try {
                settlement.setPayment(new BigDecimal(paymentField.getText().trim()));
            } catch (NumberFormatException ex) {
                settlement.setPayment(BigDecimal.ZERO);
            }

            if (existing == null) {
                SettlementService.add(settlement);
            } else {
                SettlementService.delete(existing.getId());
                SettlementService.add(settlement);
            }

            refresh();
            dialog.dispose();
        });

        dialog.pack();
        dialog.setSize(520, 520);
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }

    private void refresh() {
        model.setRowCount(0);
        for (Settlement s : SettlementService.getAll()) {
            model.addRow(new Object[]{
                    s.getId(),
                    s.getRoomNumber(),
                    s.getClientId(),
                    s.getCheckIn(),
                    s.getCheckOut(),
                    s.getTotalCost(),
                    s.getPayment()
            });
        }
    }

    // Форматтер дати (обов'язковий для JDatePickerImpl 1.3.4)
    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                java.util.Calendar cal = (java.util.Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}