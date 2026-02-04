package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.main.AuthenticationService;
import bcu.cmp5332.bookingsystem.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Dialog window for new customer registration.
 * Collects and validates all required customer information.
 */
public class RegisterWindow extends JDialog {
    private FlightBookingSystem fbs;
    private AuthenticationService authService;
    private JFrame parent;

    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JSpinner ageSpinner;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    /**
     * Creates a registration dialog window.
     * @param fbs the flight booking system to add customer to
     * @param authService authentication service for registration
     * @param parent the parent window (for modal behavior)
     */
    public RegisterWindow(FlightBookingSystem fbs, AuthenticationService authService, JFrame parent) {
        super(parent, "Register New Customer", true);
        this.fbs = fbs;
        this.authService = authService;
        this.parent = parent;

        initializeUI();
        setSize(500, 500);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Don't close automatically

        // Add window listener for closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose(); // Just close the dialog, don't exit the application
            }
        });

        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("Register New Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Name Field
        addFormField(formPanel, gbc, 0, "Name:", nameField = new JTextField(20));

        // Phone Field
        addFormField(formPanel, gbc, 1, "Phone:", phoneField = new JTextField(20));

        // Email Field
        addFormField(formPanel, gbc, 2, "Email (must be @gmail.com):", emailField = new JTextField(20));

        // Age Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Age (must be 18+):"), gbc);
        gbc.gridx = 1;
        SpinnerModel ageModel = new SpinnerNumberModel(18, 18, 100, 1);
        ageSpinner = new JSpinner(ageModel);
        formPanel.add(ageSpinner, gbc);

        // Username Field
        addFormField(formPanel, gbc, 4, "Username:", usernameField = new JTextField(20));

        // Password Field
        addFormField(formPanel, gbc, 5, "Password (min 6 chars):", passwordField = new JPasswordField(20));

        // Confirm Password Field
        addFormField(formPanel, gbc, 6, "Confirm Password:", confirmPasswordField = new JPasswordField(20));

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        JButton registerBtn = new JButton("Register");
        styleButton(registerBtn, new Color(46, 204, 113));
        registerBtn.addActionListener(new RegisterAction());

        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, new Color(231, 76, 60));
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(registerBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
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

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                int age = (Integer) ageSpinner.getValue();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                // Validation
                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() ||
                        username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterWindow.this,
                            "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterWindow.this,
                            "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!email.endsWith("@gmail.com")) {
                    JOptionPane.showMessageDialog(RegisterWindow.this,
                            "Email must be a Gmail address (@gmail.com)", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Register customer
                Customer newCustomer = authService.registerCustomer(name, phone, email, age, username, password);

                JOptionPane.showMessageDialog(RegisterWindow.this,
                        "Registration successful!\nCustomer ID: " + newCustomer.getId() +
                                "\nUsername: " + newCustomer.getUsername(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                dispose();

            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(RegisterWindow.this,
                        "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}