package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ListCustomersView extends JPanel {
    private FlightBookingSystem fbs;

    private JTable customersTable;
    private DefaultTableModel tableModel;
    private JButton deleteCustomerBtn;
    private JButton restoreCustomerBtn;
    private JCheckBox showDeletedCheckBox;

    public ListCustomersView(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initializeUI();
        refresh();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("All Customers", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(44, 62, 80));

        // ====== NEW: Control Panel with buttons ======
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        // Show deleted customers checkbox
        showDeletedCheckBox = new JCheckBox("Show Deleted Customers");
        showDeletedCheckBox.addActionListener(e -> refresh());

        // Refresh button
        JButton refreshBtn = new JButton("Refresh");
        styleButton(refreshBtn, new Color(52, 152, 219));
        refreshBtn.addActionListener(e -> refresh());

        // Delete customer button
        deleteCustomerBtn = new JButton("Delete Customer");
        styleButton(deleteCustomerBtn, new Color(231, 76, 60)); // Red
        deleteCustomerBtn.addActionListener(e -> deleteSelectedCustomer());
        deleteCustomerBtn.setEnabled(false);

        // Restore customer button
        restoreCustomerBtn = new JButton("Restore Customer");
        styleButton(restoreCustomerBtn, new Color(46, 204, 113)); // Green
        restoreCustomerBtn.addActionListener(e -> restoreSelectedCustomer());
        restoreCustomerBtn.setEnabled(false);

        controlPanel.add(showDeletedCheckBox);
        controlPanel.add(refreshBtn);
        controlPanel.add(deleteCustomerBtn);
        controlPanel.add(restoreCustomerBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(controlPanel, BorderLayout.EAST);

        // Create customers table with additional columns
        String[] columns = {"ID", "Name", "Phone", "Email", "Age",
                "Username", "Role", "Bookings", "Deleted"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customersTable = new JTable(tableModel);
        customersTable.setRowHeight(30);
        customersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        customersTable.getTableHeader().setBackground(new Color(52, 73, 94));
        customersTable.getTableHeader().setForeground(Color.WHITE);

        // Add selection listener
        customersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });

        JScrollPane scrollPane = new JScrollPane(customersTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateButtonStates() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow == -1) {
            deleteCustomerBtn.setEnabled(false);
            restoreCustomerBtn.setEnabled(false);
            return;
        }

        boolean isDeleted = (boolean) tableModel.getValueAt(selectedRow, 8); // "Deleted" column

        deleteCustomerBtn.setEnabled(!isDeleted);
        restoreCustomerBtn.setEnabled(isDeleted);
    }

    private void deleteSelectedCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a customer to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        String customerName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete customer " + customerName + "?\n" +
                        "This will prevent them from logging in.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Check if customer has bookings
                Customer customer = fbs.getCustomerByID(customerId);
                if (!customer.getBookings().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Cannot delete customer with active bookings.\n" +
                                    "Please cancel all bookings first.",
                            "Delete Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                fbs.deleteCustomer(customerId);
                JOptionPane.showMessageDialog(this,
                        "Customer " + customerName + " has been deleted (soft delete).",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refresh();

            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting customer: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void restoreSelectedCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a customer to restore.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        String customerName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to restore customer " + customerName + "?\n" +
                        "This will allow them to log in again.",
                "Confirm Restore", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                fbs.restoreCustomer(customerId);
                JOptionPane.showMessageDialog(this,
                        "Customer " + customerName + " has been restored.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refresh();

            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error restoring customer: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refresh() {
        List<Customer> customers;
        if (showDeletedCheckBox.isSelected()) {
            // Show all customers including deleted ones
            customers = fbs.getAllCustomers();
        } else {
            // Show only non-deleted customers
            customers = fbs.getCustomers();
        }

        tableModel.setRowCount(0);

        for (Customer customer : customers) {
            Object[] row = {
                    customer.getId(),
                    customer.getName(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getAge(),
                    customer.getUsername(),
                    customer.getRole().toString(),
                    customer.getBookings().size(),
                    customer.isDeleted()
            };
            tableModel.addRow(row);
        }

        updateButtonStates();
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }
}