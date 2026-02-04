package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCustomerView extends JPanel {
    private FlightBookingSystem fbs;

    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JSpinner ageSpinner;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    private Runnable onCustomerAdded;

    public AddCustomerView(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Add New Customer", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        formPanel.add(phoneField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email (must be @gmail.com):"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);

        // Age
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Age (must be 18+):"), gbc);
        gbc.gridx = 1;
        ageSpinner = new JSpinner(new SpinnerNumberModel(18, 18, 100, 1));
        formPanel.add(ageSpinner, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Password (min 6 chars):"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{"CUSTOMER", "ADMIN"});
        formPanel.add(roleCombo, gbc);

        // Add Button
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addBtn = new JButton("Add Customer");
        styleButton(addBtn, new Color(46, 204, 113));
        addBtn.addActionListener(new AddCustomerAction());
        formPanel.add(addBtn, gbc);

        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
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

    private class AddCustomerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Create AddCustomer command
                AddCustomer addCustomerCommand = new AddCustomer(
                        nameField.getText(),
                        phoneField.getText(),
                        emailField.getText(),
                        (Integer) ageSpinner.getValue(),
                        usernameField.getText(),
                        new String(passwordField.getPassword())
                );

                addCustomerCommand.execute(fbs);

                JOptionPane.showMessageDialog(AddCustomerView.this,
                        "Customer added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear form
                nameField.setText("");
                phoneField.setText("");
                emailField.setText("");
                ageSpinner.setValue(18);
                usernameField.setText("");
                passwordField.setText("");
                roleCombo.setSelectedIndex(0);

                // Notify parent
                if (onCustomerAdded != null) {
                    onCustomerAdded.run();
                }

            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(AddCustomerView.this,
                        "Failed to add customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setOnCustomerAdded(Runnable callback) {
        this.onCustomerAdded = callback;
    }
}