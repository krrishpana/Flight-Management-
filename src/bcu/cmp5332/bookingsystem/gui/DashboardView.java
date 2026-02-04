package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Flight;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JPanel {
    private FlightBookingSystem fbs;
    private Customer currentUser;

    private JLabel totalBookingsLabel;
    private JLabel activeFlightsLabel;
    private JLabel upcomingTripsLabel;
    private JLabel totalSpentLabel;

    public DashboardView(FlightBookingSystem fbs, Customer currentUser) {
        this.fbs = fbs;
        this.currentUser = currentUser;
        initializeUI();

        refresh();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(titleLabel);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Create stat cards
        statsPanel.add(createStatCard("Total Bookings", totalBookingsLabel = new JLabel(), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Active Flights", activeFlightsLabel = new JLabel(), new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Upcoming Trips", upcomingTripsLabel = new JLabel(), new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Total Spent", totalSpentLabel = new JLabel(), new Color(241, 196, 15)));

        add(titleLabel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(Color.DARK_GRAY);

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        return card;
    }

    public void refresh() {
        totalBookingsLabel.setText(String.valueOf(currentUser.getBookings().size()));
        activeFlightsLabel.setText(String.valueOf(countActiveFlights()));
        upcomingTripsLabel.setText(String.valueOf(countUpcomingFlights()));
        totalSpentLabel.setText("£" + String.format("%.2f", calculateTotalSpent()));
    }

    private int countActiveFlights() {
        int count = 0;
        for (Booking booking : currentUser.getBookings()) {
            if (!booking.getFlight().getDepartureDate().isBefore(fbs.getSystemDate())) {
                count++;
            }
        }
        return count;
    }

    private int countUpcomingFlights() {
        int count = 0;
        for (Booking booking : currentUser.getBookings()) {
            if (booking.getFlight().getDepartureDate().isAfter(fbs.getSystemDate())) {
                count++;
            }
        }
        return count;
    }

    private double calculateTotalSpent() {
        double total = 0;
        for (Booking booking : currentUser.getBookings()) {
            total += booking.getPaidPrice();
        }
        return total;
    }
}