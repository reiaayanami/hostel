package org.example;

import javax.swing.*;
import java.awt.*;

public class ClientDialog extends JDialog {
    private JTextField txtFullName = new JTextField(20);
    private JTextField txtPassport = new JTextField(15);
    private JTextField txtPhone = new JTextField(12);
    private JTextField txtEmail = new JTextField(20);

    private Client result;

    public ClientDialog(JFrame owner, Client client) {
        super(owner, client == null ? "Новий клієнт" : "Редагувати клієнта", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("ПІБ:"), gbc);
        gbc.gridx = 1; add(txtFullName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Паспорт:"), gbc);
        gbc.gridx = 1; add(txtPassport, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Телефон (10 цифр):"), gbc);
        gbc.gridx = 1; add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Email (необов'язково):"), gbc);
        gbc.gridx = 1; add(txtEmail, gbc);

        JButton save = new JButton("Зберегти");
        UIUtils.styleButton(save);
        gbc.gridy = 4; gbc.gridwidth = 2; add(save, gbc);

        if (client != null) {
            txtFullName.setText(client.getFullName());
            txtPassport.setText(client.getPassport());
            txtPhone.setText(client.getPhone());
            txtEmail.setText(client.getEmail());
        }

        txtPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') e.consume();
                if (txtPhone.getText().length() >= 10 && c != '\b') e.consume();
            }
        });

        save.addActionListener(e -> {
            String phone = txtPhone.getText().trim();
            if (phone.length() != 10 || !phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Телефон — рівно 10 цифр!");
                return;
            }

            result = new Client(0, txtFullName.getText().trim(), txtPassport.getText().trim(), phone, txtEmail.getText().trim());
            dispose();
        });

        pack();
        setLocationRelativeTo(owner);
    }

    public int showDialog() {
        setVisible(true);
        return result != null ? JOptionPane.OK_OPTION : JOptionPane.CANCEL_OPTION;
    }

    public Client getClient() { return result; }
}