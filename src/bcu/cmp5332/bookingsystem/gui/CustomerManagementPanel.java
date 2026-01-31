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
        // Increased grid rows to accommodate 8 fields
        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField foodField = new JTextField();
        JCheckBox hasChildCheckBox = new JCheckBox("Yes");

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Food Preference:"));
        panel.add(foodField);
        panel.add(new JLabel("Has Child Under 2?"));
        panel.add(hasChildCheckBox);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Customer", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Collect and trim values
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String ageStr = ageField.getText().trim();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String food = foodField.getText().trim();
                boolean hasChild = hasChildCheckBox.isSelected();

                // Basic validation
                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    throw new IllegalArgumentException("All main fields are required.");
                }

                int age = Integer.parseInt(ageStr);

                // Execute command with all 8 parameters
                AddCustomer command = new AddCustomer(name, phone, email, age, username, password, food, hasChild);
                command.execute(flightBookingSystem);

                showSuccess("Customer " + name + " added successfully!");

            } catch (NumberFormatException e) {
                showError("Age must be a valid number.");
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