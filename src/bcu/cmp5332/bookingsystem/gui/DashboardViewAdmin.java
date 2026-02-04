package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.Booking;

import javax.swing.*;
import java.awt.*;

public class DashboardViewAdmin extends JPanel {
    private FlightBookingSystem fbs;

    private JLabel totalFlightsLabel;
    private JLabel totalCustomersLabel;
    private JLabel totalBookingsLabel;
    private JLabel availableSeatsLabel;
    private JLabel todayRevenueLabel;
    private JLabel systemDateLabel;

    public DashboardViewAdmin(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initializeUI();
        refresh();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Create stat cards
        statsPanel.add(createStatCard("Total Flights", totalFlightsLabel = new JLabel(), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Total Customers", totalCustomersLabel = new JLabel(), new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Total Bookings", totalBookingsLabel = new JLabel(), new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Revenue Today", todayRevenueLabel = new JLabel(), new Color(230, 126, 34)));

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
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(Color.DARK_GRAY);

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        return card;
    }

    public void refresh() {
        totalFlightsLabel.setText(String.valueOf(fbs.getFlights().size()));
        totalCustomersLabel.setText(String.valueOf(fbs.getCustomers().size()));
        totalBookingsLabel.setText(String.valueOf(countAllBookings()));
        todayRevenueLabel.setText("£" + String.format("%.2f", calculateTodayRevenue()));
    }

    private int countAllBookings() {
        int total = 0;
        for (Customer customer : fbs.getCustomers()) {
            total += customer.getBookings().size();
        }
        return total;
    }


    private double calculateTodayRevenue() {
        // Simplified - in reality would check booking dates
        return calculateTotalRevenue() * 0.1; // 10% of total for demo
    }

    private double calculateTotalRevenue() {
        double total = 0;
        for (Customer customer : fbs.getCustomers()) {
            for (Booking booking : customer.getBookings()) {
                total += booking.getPaidPrice();
            }
        }
        return total;
    }
}