package org.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class EmployeeDialog extends JDialog {

    private final JTextField fullNameInput;
    private final JTextField roleInput;
    private final JTextField loginInput;
    private final JPasswordField passwordInput;
    private final JTextField salaryInput;

    private final JButton saveButton;

    private Employee result;

    public EmployeeDialog(JFrame owner, Employee employee) {
        super(owner, employee == null ? "Додати працівника" : "Редагувати працівника", true);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 8));

        fullNameInput = new JTextField();
        roleInput = new JTextField();
        loginInput = new JTextField();
        passwordInput = new JPasswordField();
        salaryInput = new JTextField();

        formPanel.add(new JLabel("ПІБ:"));
        formPanel.add(fullNameInput);
        formPanel.add(new JLabel("Посада:"));
        formPanel.add(roleInput);
        formPanel.add(new JLabel("Логін:"));
        formPanel.add(loginInput);
        formPanel.add(new JLabel("Пароль:"));
        formPanel.add(passwordInput);
        formPanel.add(new JLabel("Зарплата:"));
        formPanel.add(salaryInput);

        saveButton = new JButton("Зберегти");
        JButton cancelButton = new JButton("Скасувати");

        saveButton.setEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        if (employee != null) {
            fullNameInput.setText(employee.getFullName());
            roleInput.setText(employee.getRole());
            loginInput.setText(employee.getLogin());
            passwordInput.setText("");
            passwordInput.setEnabled(false);
            salaryInput.setText(String.valueOf(employee.getSalary()));
        }


        addValidationListener(fullNameInput);
        addValidationListener(roleInput);
        addValidationListener(loginInput);
        addValidationListener(passwordInput);
        addValidationListener(salaryInput);

        saveButton.addActionListener(e -> onSave(employee));
        cancelButton.addActionListener(e -> {
            result = null;
            dispose();
        });

        setLayout(new BorderLayout(10, 10));
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(420, 260);
        setLocationRelativeTo(owner);
    }

    private void addValidationListener(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { checkButtonState(); }
            public void removeUpdate(DocumentEvent e) { checkButtonState(); }
            public void changedUpdate(DocumentEvent e) { checkButtonState(); }
        });
    }

    private void checkButtonState() {
        String fullName = fullNameInput.getText().trim();
        String role = roleInput.getText().trim();
        String login = loginInput.getText().trim();
        String password = new String(passwordInput.getPassword());
        String salary = salaryInput.getText().trim();

        saveButton.setEnabled(
                !fullName.isEmpty() &&
                        !role.isEmpty() &&
                        !login.isEmpty() &&
                        !salary.isEmpty() &&
                        (!passwordInput.isEnabled() || !password.isEmpty())
        );
    }

    private void onSave(Employee sourceEmployee) {
        String fullName = fullNameInput.getText().trim();
        String role = roleInput.getText().trim();
        String login = loginInput.getText().trim();
        String password = new String(passwordInput.getPassword());
        int salary = Integer.parseInt(salaryInput.getText().trim());

        if (sourceEmployee == null) {
            result = new Employee(0, fullName, role, salary, login, password);
        } else {
            result = new Employee(
                    sourceEmployee.getId(),
                    fullName,
                    role,
                    salary,
                    login,
                    sourceEmployee.getPassword()
            );
        }

        dispose();
    }

    public Employee getResult() {
        return result;
    }
}
