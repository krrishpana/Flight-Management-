package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;

public class AdminMainWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    private FlightBookingSystem flightBookingSystem;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public AdminMainWindow(FlightBookingSystem flightBookingSystem) {
        this.flightBookingSystem = flightBookingSystem;
        initialize();
    }

    private void initialize() {
        setTitle("Flight Booking System - Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        // Create main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create different panels
        FlightManagementPanel flightPanel = new FlightManagementPanel(flightBookingSystem);
        CustomerManagementPanel customerPanel = new CustomerManagementPanel(flightBookingSystem);
        BookingManagementPanel bookingPanel = new BookingManagementPanel(flightBookingSystem);
        DashboardPanel dashboardPanel = new DashboardPanel(flightBookingSystem);

        // Add panels to card layout
        mainPanel.add(dashboardPanel, "DASHBOARD");
        mainPanel.add(flightPanel, "FLIGHTS");
        mainPanel.add(customerPanel, "CUSTOMERS");
        mainPanel.add(bookingPanel, "BOOKINGS");

        add(mainPanel, BorderLayout.CENTER);

        // Start with dashboard
        cardLayout.show(mainPanel, "DASHBOARD");

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Dashboard menu
        JButton dashboardButton = new JButton("Dashboard");
        dashboardButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));
        menuBar.add(dashboardButton);

        // Flights menu
        JButton flightsButton = new JButton("Flights");
        flightsButton.addActionListener(e -> cardLayout.show(mainPanel, "FLIGHTS"));
        menuBar.add(flightsButton);

        // Customers menu
        JButton customersButton = new JButton("Customers");
        customersButton.addActionListener(e -> cardLayout.show(mainPanel, "CUSTOMERS"));
        menuBar.add(customersButton);

        // Bookings menu
        JButton bookingsButton = new JButton("Bookings");
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