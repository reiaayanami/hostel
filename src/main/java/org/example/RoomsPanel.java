package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

public class RoomsPanel extends JPanel {
    private final JFrame owner;
    private final Employee currentUser;
    private DefaultTableModel model;
    private JTable table;

    public RoomsPanel(JFrame owner, Employee user) {
        this.owner = owner;
        this.currentUser = user;
        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(new String[]{"Номер", "Тип", "Місць заг.", "Зайнято", "Ціна/добу", "Статус"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new RoomRenderer());
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addBtn = new JButton("Додати кімнату");
        JButton editBtn = new JButton("Редагувати");
        JButton deleteBtn = new JButton("Видалити");
        JButton newSettleBtn = new JButton("Нове поселення");
        JButton evictBtn = new JButton("Виселення");
        JButton debtorsBtn = new JButton("Список боржників");

        UIUtils.styleButton(addBtn); UIUtils.styleButton(editBtn); UIUtils.styleButton(deleteBtn);
        UIUtils.styleButton(newSettleBtn); UIUtils.styleButton(evictBtn); UIUtils.styleButton(debtorsBtn);

        controlPanel.add(addBtn); controlPanel.add(editBtn); controlPanel.add(deleteBtn);
        controlPanel.add(new JLabel("   Швидкі дії: "));
        controlPanel.add(newSettleBtn); controlPanel.add(evictBtn); controlPanel.add(debtorsBtn);

        add(controlPanel, BorderLayout.NORTH);

        boolean isAdmin = currentUser.getRole().toLowerCase().contains("адмін") || currentUser.getRole().toLowerCase().contains("admin");
        if (!isAdmin) {
            deleteBtn.setEnabled(false);
            addBtn.setEnabled(false); // менеджер може тільки статус змінювати
        }

        addBtn.addActionListener(e -> {
            RoomDialog dialog = new RoomDialog(owner, null);
            if (dialog.showDialog() == JOptionPane.OK_OPTION) {
                RoomService.add(dialog.getRoom());
                refresh();
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int num = (int) model.getValueAt(row, 0);
                Room r = RoomService.getByNumber(num);
                RoomDialog dialog = new RoomDialog(owner, r);
                if (dialog.showDialog() == JOptionPane.OK_OPTION) {
                    RoomService.update(dialog.getRoom());
                    refresh();
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && JOptionPane.showConfirmDialog(owner, "Видалити кімнату?", "Підтвердження", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                int num = (int) model.getValueAt(row, 0);
                RoomService.delete(num);
                refresh();
            }
        });

        newSettleBtn.addActionListener(e -> new SettlementsPanel(owner, currentUser).showNewSettlementDialog()); // або відкрити SettlementsPanel
        evictBtn.addActionListener(e -> JOptionPane.showMessageDialog(owner, "Виселення: оберіть поселення на вкладці «Поселення» та видаліть/відредагуйте запис."));
        debtorsBtn.addActionListener(e -> showDebtors());

        refresh();
    }

    private void refresh() {
        model.setRowCount(0);
        for (Room r : RoomService.getAll()) {
            model.addRow(new Object[]{
                    r.getNumber(), r.getType(), r.getTotalBeds(), r.getOccupiedBeds(),
                    r.getPricePerDay(), r.getStatus()
            });
        }
    }

    private void showDebtors() {
        StringBuilder sb = new StringBuilder("Боржники:\n");
        boolean has = false;
        for (Settlement s : SettlementService.getAll()) {
            if (s.getPayment().compareTo(s.getTotalCost()) < 0) {
                has = true;
                sb.append("Кімната ").append(s.getRoomNumber()).append(" - борг ").append(s.getTotalCost().subtract(s.getPayment())).append(" грн\n");
            }
        }
        JOptionPane.showMessageDialog(owner, has ? sb.toString() : "Боржників немає!");
    }

    // Renderer для кольорів
    class RoomRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = (String) table.getValueAt(row, 5);
            if (isSelected) return c;
            if ("Вільно".equals(status)) c.setBackground(Color.GREEN.brighter());
            else if ("Зайнято".equals(status)) c.setBackground(Color.RED.brighter());
            else if ("Обслуговування".equals(status)) c.setBackground(Color.YELLOW.brighter());
            else c.setBackground(Color.WHITE);
            return c;
        }
    }
}