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
 * Dialog window for user authentication.
 * Handles login with username and password, then redirects based on user role.
 */
public class LoginWindow extends JDialog {
    private FlightBookingSystem fbs;
    private AuthenticationService authService;
    private JFrame parent;

    private JTextField usernameField;
    private JPasswordField passwordField;

    /**
     * Creates a login dialog window.
     * @param fbs the flight booking system to authenticate against
     * @param authService authentication service to use
     * @param parent the parent window (for modal behavior)
     */
    public LoginWindow(FlightBookingSystem fbs, AuthenticationService authService, JFrame parent) {
        super(parent, "Login", true);
        this.fbs = fbs;
        this.authService = authService;
        this.parent = parent;

        initializeUI();
        setSize(400, 300);
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
        JLabel titleLabel = new JLabel("Login to Your Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        JButton loginBtn = new JButton("Login");
        styleButton(loginBtn, new Color(52, 152, 219));
        loginBtn.addActionListener(new LoginAction());

        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, new Color(231, 76, 60));
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(loginBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add Enter key listener
        getRootPane().setDefaultButton(loginBtn);
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

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginWindow.this,
                        "Please enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (authService.login(username, password)) {
                Customer currentUser = authService.getCurrentUser();
                fbs.setCurrentUser(currentUser);

                JOptionPane.showMessageDialog(LoginWindow.this,
                        "Login successful!\nWelcome, " + currentUser.getName(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                dispose();
                parent.dispose(); // Close role selection window

                // Open appropriate panel based on role
                if (currentUser.isAdmin()) {
                    new AdminFrame(fbs, authService);
                } else {
                    new CustomerFrame(fbs, authService);
                }

            } else {
                JOptionPane.showMessageDialog(LoginWindow.this,
                        "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}