package org.example;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField loginField = new JTextField(15);
    private JPasswordField passField = new JPasswordField(15);
    private JButton btnLogin = new JButton("Увійти");
    private JButton btnExit = new JButton("Вихід");

    public LoginPanel(MainWindow mainWindow) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        add(new JLabel("Логін:"), gbc); gbc.gridy = 1;
        add(loginField, gbc); gbc.gridy = 2;
        add(new JLabel("Пароль:"), gbc); gbc.gridy = 3;
        add(passField, gbc); gbc.gridy = 4;

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnLogin);
        btnPanel.add(btnExit);
        add(btnPanel, gbc);

        UIUtils.styleAll(this);

        btnLogin.addActionListener(e -> {
            String login = loginField.getText().trim();
            String pass = new String(passField.getPassword());

            Employee emp = EmployeeService.login(login, pass);
            if (emp != null) {
                mainWindow.openMainForm(emp);
            } else {
                JOptionPane.showMessageDialog(this, "Невірний логін або пароль!", "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnExit.addActionListener(e -> System.exit(0));

        // авто-активація кнопки
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { check(); }
            public void removeUpdate(DocumentEvent e) { check(); }
            public void changedUpdate(DocumentEvent e) { check(); }
            private void check() {
                btnLogin.setEnabled(!loginField.getText().trim().isEmpty() && passField.getPassword().length > 0);
            }
        };
        loginField.getDocument().addDocumentListener(dl);
        passField.getDocument().addDocumentListener(dl);
        btnLogin.setEnabled(false);
    }
}