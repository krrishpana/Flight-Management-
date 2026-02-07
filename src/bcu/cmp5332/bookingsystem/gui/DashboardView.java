package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JPanel {

    private final FlightBookingSystem fbs;
    private final Customer currentUser;

    private JLabel totalBookingsLabel;
    private JLabel activeFlightsLabel;
    private JLabel upcomingTripsLabel;
    private JLabel totalSpentLabel;

    private Image backgroundImage;

    public DashboardView(FlightBookingSystem fbs, Customer currentUser) {
        this.fbs = fbs;
        this.currentUser = currentUser;

        try {
            backgroundImage = new ImageIcon(
                    getClass().getResource("/images/airplane_bg.png")
            ).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }

        initializeUI();
        refresh();
    }

    private void initializeUI() {

        // === Background Panel ===
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    setBackground(new Color(236, 240, 241));
                }
            }
        };

        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        // === Header ===
        JLabel titleLabel = new JLabel("Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(52, 152, 219));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        // === Stats Grid ===
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        statsPanel.setOpaque(false);

        statsPanel.add(createStatCard("Total Bookings", totalBookingsLabel = new JLabel(), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Active Flights", activeFlightsLabel = new JLabel(), new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Upcoming Trips", upcomingTripsLabel = new JLabel(), new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Total Spent", totalSpentLabel = new JLabel(), new Color(241, 196, 15)));

        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        backgroundPanel.add(statsPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    private JPanel createStatCard(String title, JLabel valueLabel, Color accent) {

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(255, 255, 255, 200)); // glass effect
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(accent);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.DARK_GRAY);

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        return card;
    }

    // ================= DATA =================

    public void refresh() {
        totalBookingsLabel.setText(String.valueOf(currentUser.getBookings().size()));
        activeFlightsLabel.setText(String.valueOf(countActiveFlights()));
        upcomingTripsLabel.setText(String.valueOf(countUpcomingFlights()));
        totalSpentLabel.setText("Rs." + String.format("%.2f", calculateTotalSpent()));
    }

    private int countActiveFlights() {
        return (int) currentUser.getBookings().stream()
                .filter(b -> !b.getFlight().getDepartureDate().isBefore(fbs.getSystemDate()))
                .count();
    }

    private int countUpcomingFlights() {
        return (int) currentUser.getBookings().stream()
                .filter(b -> b.getFlight().getDepartureDate().isAfter(fbs.getSystemDate()))
                .count();
    }

    private double calculateTotalSpent() {
        return currentUser.getBookings().stream()
                .mapToDouble(Booking::getPaidPrice)
                .sum();
    }
}
