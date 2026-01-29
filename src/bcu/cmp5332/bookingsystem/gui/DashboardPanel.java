package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends CommandBasePanel {

    public DashboardPanel(FlightBookingSystem flightBookingSystem) {
        super(flightBookingSystem);
        initializeDashboard();
    }

    private void initializeDashboard() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Flight stats
        JPanel flightStats = createStatCard("Flights",
                String.valueOf(flightBookingSystem.getFlights().size()),
                "Manage flights, add new flights, view flight details");

        // Customer stats
        JPanel customerStats = createStatCard("Customers",
                String.valueOf(flightBookingSystem.getCustomers().size()),
                "Manage customers, add new customers");

        // Add to panel
        statsPanel.add(flightStats);
        statsPanel.add(customerStats);

        add(statsPanel, BorderLayout.CENTER);

        // Instructions
        JTextArea instructions = new JTextArea(3, 50);
        instructions.setText("Use the menu bar above to navigate to different sections:\n" +
                "• Flights: Manage flight operations\n" +
                "• Customers: Manage customer records\n" +
                "• Bookings: Manage flight bookings");
        instructions.setEditable(false);
        instructions.setBackground(getBackground());
        instructions.setFont(new Font("Arial", Font.PLAIN, 12));

        add(instructions, BorderLayout.SOUTH);
    }

    private JPanel createStatCard(String title, String count, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel countLabel = new JLabel(count);
        countLabel.setFont(new Font("Arial", Font.BOLD, 36));
        countLabel.setForeground(Color.BLUE);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea descArea = new JTextArea(description);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(card.getBackground());
        descArea.setFont(new Font("Arial", Font.PLAIN, 12));

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(countLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(descArea);

        return card;
    }
}