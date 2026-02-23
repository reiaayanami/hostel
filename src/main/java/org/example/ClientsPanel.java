package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientsPanel extends JPanel {
    private final JFrame owner;
    private final Employee currentUser;
    private DefaultTableModel model;
    private JTable table;

    public ClientsPanel(JFrame owner, Employee user) {
        this.owner = owner;
        this.currentUser = user;
        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(new String[]{"ID", "ПІБ", "Паспорт", "Телефон", "Email"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel control = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Додати клієнта");
        JButton edit = new JButton("Редагувати");
        JButton delete = new JButton("Видалити");

        UIUtils.styleButton(add); UIUtils.styleButton(edit); UIUtils.styleButton(delete);

        control.add(add); control.add(edit); control.add(delete);
        add(control, BorderLayout.NORTH);

        add.addActionListener(e -> {
            ClientDialog d = new ClientDialog(owner, null);
            if (d.showDialog() == JOptionPane.OK_OPTION) {
                ClientService.add(d.getClient());
                refresh();
            }
        });

        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                Client c = ClientService.getById(id);
                ClientDialog d = new ClientDialog(owner, c);
                if (d.showDialog() == JOptionPane.OK_OPTION) {
                    ClientService.update(d.getClient());
                    refresh();
                }
            }
        });

        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && JOptionPane.showConfirmDialog(owner, "Видалити клієнта?", "Підтвердження", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                int id = (int) model.getValueAt(row, 0);
                ClientService.delete(id);
                refresh();
            }
        });

        refresh();
    }

    private void refresh() {
        model.setRowCount(0);
        for (Client c : ClientService.getAll()) {
            model.addRow(new Object[]{c.getId(), c.getFullName(), c.getPassport(), c.getPhone(), c.getEmail()});
        }
    }
}