package org.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class EmployeesPanel extends JPanel {
    private final JFrame owner;
    private final Employee currentUser;

    private final DefaultTableModel model;
    private final JTable employeesTable;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton deleteButton;
    private final JTextField searchInput;

    public EmployeesPanel(JFrame owner, Employee currentUser) {
        this.owner = owner;
        this.currentUser = currentUser;

        setLayout(new BorderLayout(8, 8));

        model = new DefaultTableModel(new Object[]{"ID", "ПІБ", "Посада", "Логін", "Зарплата"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeesTable = new JTable(model);
        employeesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(employeesTable);

        addButton = new JButton("Додати");
        editButton = new JButton("Редагувати");
        deleteButton = new JButton("Видалити");
        searchInput = new JTextField(18);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlsPanel.add(addButton);
        controlsPanel.add(editButton);
        controlsPanel.add(deleteButton);


        add(controlsPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        initListeners();
        refreshTable();
        updateButtonState();

        if (!isAdmin()) {
            addButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
    }

    private void initListeners() {
        employeesTable.getSelectionModel().addListSelectionListener(e -> updateButtonState());

        addButton.addActionListener(e -> {
            EmployeeDialog dialog = new EmployeeDialog(owner, null);
            dialog.setVisible(true);

            Employee newEmployee = dialog.getResult();
            if (newEmployee != null && EmployeeService.addEmployee(newEmployee)) {
                refreshTable();
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = employeesTable.getSelectedRow();
            if (selectedRow < 0) {
                return;
            }

            int modelRow = employeesTable.convertRowIndexToModel(selectedRow);
            Employee selectedEmployee = mapEmployeeFromModel(modelRow);

            EmployeeDialog dialog = new EmployeeDialog(owner, selectedEmployee);
            dialog.setVisible(true);

            Employee updated = dialog.getResult();
            if (updated != null && EmployeeService.updateEmployee(updated)) {
                refreshTable();
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = employeesTable.getSelectedRow();
            if (selectedRow < 0) {
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    owner,
                    "Ви впевнені?",
                    "Підтвердження видалення",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            int modelRow = employeesTable.convertRowIndexToModel(selectedRow);
            int id = (int) model.getValueAt(modelRow, 0);

            if (EmployeeService.deleteEmployee(id)) {
                refreshTable();
            }
        });

    }

    private void refreshTable() {
        List<Employee> employees = EmployeeService.getEmployees(searchInput.getText());

        model.setRowCount(0);
        for (Employee employee : employees) {
            model.addRow(new Object[]{
                    employee.getId(),
                    employee.getFullName(),
                    employee.getRole(),
                    employee.getLogin(),
                    employee.getSalary()
            });
        }

        employeesTable.setRowSorter(new TableRowSorter<>(model));
        updateButtonState();
    }

    private void updateButtonState() {
        boolean hasSelection = employeesTable.getSelectedRow() >= 0;
        boolean canManage = isAdmin();

        editButton.setEnabled(hasSelection && canManage);
        deleteButton.setEnabled(hasSelection && canManage);
    }

    private Employee mapEmployeeFromModel(int modelRow) {
        int id = (int) model.getValueAt(modelRow, 0);
        // Зaпuтуємo пoвнi дaнuх (з пaрoлem) з БД
        return EmployeeService.getEmployees("").stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private boolean isAdmin() {
        if (currentUser == null || currentUser.getRole() == null) {
            return false;
        }

        String role = currentUser.getRole().trim().toLowerCase();
        return role.equals("адміністратор") || role.equals("admin");
    }
}
