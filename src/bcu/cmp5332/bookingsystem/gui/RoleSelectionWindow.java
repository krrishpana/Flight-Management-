package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;

public class RoleSelectionWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    private FlightBookingSystem flightBookingSystem;

    public RoleSelectionWindow(FlightBookingSystem flightBookingSystem) {
        this.flightBookingSystem = flightBookingSystem;
        initialize();
    }

    private void initialize() {
        setTitle("Flight Booking System - Role Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Title
        JLabel titleLabel = new JLabel("Flight Booking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Instruction
        JLabel instructionLabel = new JLabel("Please select your role:");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(instructionLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 0));

        JButton adminButton = new JButton("Admin");
        adminButton.setFont(new Font("Arial", Font.BOLD, 14));
        adminButton.setPreferredSize(new Dimension(150, 60));
        adminButton.addActionListener(e -> openAdminPanel());

        JButton userButton = new JButton("User");
        userButton.setFont(new Font("Arial", Font.BOLD, 14));
        userButton.setPreferredSize(new Dimension(150, 60));
        userButton.addActionListener(e -> openUserPanel());

        buttonPanel.add(adminButton);
        buttonPanel.add(userButton);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(buttonPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Status label
        JLabel statusLabel = new JLabel("System loaded successfully");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(statusLabel);

        add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openAdminPanel() {
        this.dispose(); // Close role selection window
        new AdminMainWindow(flightBookingSystem);
    }

    private void openUserPanel() {
        this.dispose(); // Close role selection window
        new UserMainWindow(flightBookingSystem);
    }
}