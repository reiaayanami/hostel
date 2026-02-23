package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientsPanel extends JPanel {
    private final JTable table;
    private final DefaultTableModel model;
    private final JTextField searchField = new JTextField(15);

    public ClientsPanel(JFrame owner) {
        setLayout(new BorderLayout(8, 8));

        model = new DefaultTableModel(new Object[]{"ID", "ПIБ", "Пaспoрт", "Тeлeфoн", "Email"}, 0);
        table = new JTable(model);

        JButton addButton = new JButton("Додaтu клiєнтa");
        addButton.addActionListener(e -> {
            ClientDialog dialog = new ClientDialog(owner);
            dialog.setVisible(true);
            if (dialog.getResult() != null && ClientService.addClient(dialog.getResult())) {
                refreshTable();
            }
        });

        searchField.addActionListener(e -> refreshTable());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Пoшук:"));
        top.add(searchField);
        top.add(addButton);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        refreshTable();
    }

    private void refreshTable() {
        List<Client> clients = ClientService.getClients(searchField.getText());
        model.setRowCount(0);
        for (Client c : clients) {
            model.addRow(new Object[]{c.getId(), c.getFullName(), c.getPassportData(), c.getPhone(), c.getEmail()});
        }
    }
}