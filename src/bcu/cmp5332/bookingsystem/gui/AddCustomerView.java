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

    private Image backgroundImage;

    public AddCustomerView(FlightBookingSystem fbs) {
        this.fbs = fbs;

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/airplane_bg.png")).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }

        setOpaque(false); // allow background to show
        initializeUI();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Add New Customer", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            public boolean isOpaque() {
                return false; // transparent panel
            }
        };
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // === Helper to create label + field ===
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(Color.WHITE);
        formPanel.add(lblName, gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        makeFieldTransparent(nameField);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setForeground(Color.WHITE);
        formPanel.add(lblPhone, gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        makeFieldTransparent(phoneField);
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblEmail = new JLabel("Email (must be @gmail.com):");
        lblEmail.setForeground(Color.WHITE);
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        makeFieldTransparent(emailField);
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblAge = new JLabel("Age (must be 18+):");
        lblAge.setForeground(Color.WHITE);
        formPanel.add(lblAge, gbc);
        gbc.gridx = 1;
        ageSpinner = new JSpinner(new SpinnerNumberModel(18, 18, 100, 1));
        ((JSpinner.DefaultEditor) ageSpinner.getEditor()).getTextField().setOpaque(false);
        ((JSpinner.DefaultEditor) ageSpinner.getEditor()).getTextField().setForeground(Color.WHITE);
        ageSpinner.setOpaque(false);
        formPanel.add(ageSpinner, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        formPanel.add(lblUsername, gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        makeFieldTransparent(usernameField);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblPassword = new JLabel("Password (min 6 chars):");
        lblPassword.setForeground(Color.WHITE);
        formPanel.add(lblPassword, gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        makeFieldTransparent(passwordField);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblRole = new JLabel("Role:");
        lblRole.setForeground(Color.WHITE);
        formPanel.add(lblRole, gbc);
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{"CUSTOMER", "ADMIN"});
        roleCombo.setOpaque(false);
        roleCombo.setForeground(Color.WHITE);
        formPanel.add(roleCombo, gbc);

        // === Add Customer Button ===
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton addBtn = new JButton("Add Customer");
        styleButton(addBtn, new Color(46, 204, 113));
        addBtn.addActionListener(new AddCustomerAction());
        formPanel.add(addBtn, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void makeFieldTransparent(JTextField field) {
        field.setOpaque(true);               // solid background
        field.setBackground(Color.WHITE);    // white background
        field.setForeground(Color.BLACK);    // black text
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // optional: gray border
        field.setCaretColor(Color.BLACK);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true); // solid button

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

                if (onCustomerAdded != null) onCustomerAdded.run();

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
