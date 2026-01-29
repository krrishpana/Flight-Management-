package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;

public class UserMainWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    private FlightBookingSystem flightBookingSystem;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public UserMainWindow(FlightBookingSystem flightBookingSystem) {
        this.flightBookingSystem = flightBookingSystem;
        initialize();
    }

    private void initialize() {
        setTitle("Flight Booking System - User Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        // Create main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create user panels
        UserFlightPanel flightPanel = new UserFlightPanel(flightBookingSystem);
        UserBookingPanel bookingPanel = new UserBookingPanel(flightBookingSystem);

        // Add panels to card layout
        mainPanel.add(flightPanel, "FLIGHTS");
        mainPanel.add(bookingPanel, "BOOKINGS");

        add(mainPanel, BorderLayout.CENTER);

        // Start with flights panel
        cardLayout.show(mainPanel, "FLIGHTS");

        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Flights menu
        JButton flightsButton = new JButton("View Flights");
        flightsButton.addActionListener(e -> cardLayout.show(mainPanel, "FLIGHTS"));
        menuBar.add(flightsButton);

        // My Bookings menu
        JButton bookingsButton = new JButton("My Bookings");
        bookingsButton.addActionListener(e -> cardLayout.show(mainPanel, "BOOKINGS"));
        menuBar.add(bookingsButton);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(logoutButton);

        return menuBar;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new RoleSelectionWindow(flightBookingSystem);
        }
    }
}