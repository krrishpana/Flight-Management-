package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;

public class CustomerManagementPanel extends CommandBasePanel {

    public CustomerManagementPanel(FlightBookingSystem flightBookingSystem) {
        super(flightBookingSystem);
        initializeCustomerManagement();
    }

    private void initializeCustomerManagement() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Customer Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton listCustomersBtn = new JButton("List All Customers");
        listCustomersBtn.addActionListener(e -> executeListCustomers());

        JButton addCustomerBtn = new JButton("Add New Customer");
        addCustomerBtn.addActionListener(e -> showAddCustomerDialog());

        JButton showCustomerBtn = new JButton("Show Customer Details");
        showCustomerBtn.addActionListener(e -> showCustomerDetailsDialog());

        buttonPanel.add(listCustomersBtn);
        buttonPanel.add(addCustomerBtn);
        buttonPanel.add(showCustomerBtn);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void executeListCustomers() {
        clearOutput();
        try {
            ListCustomers command = new ListCustomers();
            command.execute(flightBookingSystem);
            appendOutput("List customers command executed successfully.");
        } catch (FlightBookingSystemException e) {
            showError(e.getMessage());
        }
    }

    private void showAddCustomerDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Customer", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();

                if (name.isEmpty() || phone.isEmpty()) {
                    throw new IllegalArgumentException("Name and phone are required.");
                }

                AddCustomer command = new AddCustomer(name, phone);
                command.execute(flightBookingSystem);
                showSuccess("Customer added successfully!");

            } catch (IllegalArgumentException | FlightBookingSystemException e) {
                showError(e.getMessage());
            }
        }
    }

    private void showCustomerDetailsDialog() {
        String customerIdStr = JOptionPane.showInputDialog(this,
                "Enter Customer ID:", "Show Customer Details", JOptionPane.QUESTION_MESSAGE);

        if (customerIdStr != null && !customerIdStr.trim().isEmpty()) {
            try {
                int customerId = Integer.parseInt(customerIdStr.trim());
                ShowCustomer command = new ShowCustomer(customerId);
                command.execute(flightBookingSystem);
                appendOutput("Customer details displayed above.");
            } catch (NumberFormatException e) {
                showError("Invalid Customer ID. Please enter a number.");
            } catch (FlightBookingSystemException e) {
                showError(e.getMessage());
            }
        }
    }
}