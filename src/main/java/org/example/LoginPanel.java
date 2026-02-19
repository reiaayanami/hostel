package org.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;



public class LoginPanel extends JLabel {
    public static interface IOnLoginCallBack {
        void onLogin(Employee employee);
    }


    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JTextField loginInput;
    private JTextField passwordInput;
    private JButton loginButton;
    private IOnLoginCallBack onLoginCallBack;

    public LoginPanel(IOnLoginCallBack onLoginCallBack) {
        loginLabel = new JLabel("Логін");
        passwordLabel = new JLabel("Пароль");
        loginInput = new JTextField(15);
        passwordInput = new JPasswordField(15);
        loginButton = new JButton("Увійти");
        this.onLoginCallBack = onLoginCallBack;

        setLayout(new FlowLayout());

        add(loginLabel);
        add(loginInput);
        add(passwordLabel);
        add(passwordInput);
        add(loginButton);

        setInputListeners();
        setButtonListeners();
    }

    private void setButtonListeners() {
        loginButton.addActionListener((ActionEvent e) -> {
            String login = loginInput.getText();
            String password = passwordInput.getText();
            Employee employee = EmployeeService.login(login, password);

            if (employee == null) {
                JOptionPane.showMessageDialog(this, "Користувача не знайдено. Спробуйте ще раз");
            } else {
                this.onLoginCallBack.onLogin(employee);
            }
        });
    }

    private void checkButtonState() {
        String login = loginInput.getText();
        String password = passwordInput.getText();

        loginButton.setEnabled(
                !login.isEmpty() &&
                        !password.isEmpty()
        );
    }

    private void setInputListeners() {
        loginButton.setEnabled(false);

        loginInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { checkButtonState(); }

            @Override
            public void removeUpdate(DocumentEvent e) { checkButtonState(); }

            @Override
            public void changedUpdate(DocumentEvent e) { checkButtonState(); }
        });

        passwordInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { checkButtonState(); }

            @Override
            public void removeUpdate(DocumentEvent e) { checkButtonState(); }

            @Override
            public void changedUpdate(DocumentEvent e) { checkButtonState(); }
        });


    }
}
