package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SeatViewDialog extends JDialog {

    public SeatViewDialog(JFrame parent, Flight flight) {
        super(parent, "Flight Seat Map - " + flight.getFlightNumber(), true);
        setSize(700, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header with flight info
        JPanel headerPanel = new JPanel(new GridLayout(3, 1));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(
                "Flight: " + flight.getFlightNumber(),
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel routeLabel = new JLabel(
                flight.getOrigin() + " → " + flight.getDestination(),
                SwingConstants.CENTER
        );
        routeLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel statsLabel = new JLabel(
                "Departure: " + flight.getDepartureDate() +
                        " | Capacity: " + flight.getCapacity() +
                        " | Booked: " + flight.getPassengers().size() +
                        " | Available: " + flight.getRemainingSeats(),
                SwingConstants.CENTER
        );
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        headerPanel.add(titleLabel);
        headerPanel.add(routeLabel);
        headerPanel.add(statsLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Create seat-to-passenger map
        Map<String, String> seatPassengerMap = createSeatPassengerMap(flight);

        // Seat grid in scroll pane
        JPanel seatPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        seatPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Add column headers
        seatPanel.add(new JLabel("Row", SwingConstants.CENTER));
        for (char col : new char[]{'A', 'B', 'C', 'D', 'E', 'F'}) {
            JLabel colLabel = new JLabel(String.valueOf(col), SwingConstants.CENTER);
            colLabel.setFont(new Font("Arial", Font.BOLD, 14));
            seatPanel.add(colLabel);
        }

        // Create seat display
        displaySeatGrid(flight, seatPanel, seatPassengerMap);

        JScrollPane scrollPane = new JScrollPane(seatPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Seat Map"));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with legend and close button
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Legend panel
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        legendPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel legendLabel = new JLabel("LEGEND: ");
        legendLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel availableLegend = createLegendBox("Available", new Color(46, 204, 113));
        JPanel bookedLegend = createLegendBox("Booked", new Color(231, 76, 60));

        legendPanel.add(legendLabel);
        legendPanel.add(availableLegend);
        legendPanel.add(bookedLegend);

        bottomPanel.add(legendPanel, BorderLayout.CENTER);

        // Close button
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private Map<String, String> createSeatPassengerMap(Flight flight) {
        Map<String, String> seatMap = new HashMap<>();

        // Get all bookings for this flight
        for (Customer passenger : flight.getPassengers()) {
            for (Booking booking : passenger.getBookings()) {
                if (booking.getFlight().getId() == flight.getId()) {
                    String seatNumber = booking.getSeatNumber();
                    if (seatNumber != null && !seatNumber.isEmpty()) {
                        String passengerInfo = passenger.getName() + " (ID: " + passenger.getId() + ")";
                        seatMap.put(seatNumber.toUpperCase(), passengerInfo);
                    }
                }
            }
        }

        return seatMap;
    }

    private void displaySeatGrid(Flight flight, JPanel seatPanel, Map<String, String> seatPassengerMap) {
        int seatsPerRow = 6;
        int totalSeats = flight.getCapacity();
        int totalRows = (int) Math.ceil((double) totalSeats / seatsPerRow);

        char[] cols = {'A','B','C','D','E','F'};
        int seatIndex = 0;

        for (int row = 1; row <= totalRows; row++) {

            // Row label
            JLabel rowLabel = new JLabel(String.valueOf(row), SwingConstants.CENTER);
            rowLabel.setFont(new Font("Arial", Font.BOLD, 12));
            seatPanel.add(rowLabel);

            for (int c = 0; c < seatsPerRow; c++) {

                if (seatIndex >= totalSeats) {
                    seatPanel.add(new JLabel()); // Empty cell
                    continue;
                }

                String seat = row + String.valueOf(cols[c]);
                JPanel seatCell = new JPanel(new BorderLayout());
                seatCell.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                seatCell.setPreferredSize(new Dimension(50, 40));

                JLabel seatLabel = new JLabel(seat, SwingConstants.CENTER);
                seatLabel.setForeground(Color.WHITE);
                seatLabel.setFont(new Font("Arial", Font.BOLD, 12));
                seatCell.add(seatLabel, BorderLayout.CENTER);

                if (seatPassengerMap.containsKey(seat)) {
                    seatCell.setBackground(new Color(231, 76, 60));
                    seatCell.setToolTipText("Booked by: " + seatPassengerMap.get(seat));
                } else {
                    seatCell.setBackground(new Color(46, 204, 113));
                    seatCell.setToolTipText("Seat " + seat + " - Available");
                }

                seatPanel.add(seatCell);
                seatIndex++;
            }
        }
    }


    private JPanel createLegendBox(String text, Color color) {
        JPanel legendBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        legendBox.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        JLabel colorBox = new JLabel("   ");
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        colorBox.setPreferredSize(new Dimension(20, 15));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        legendBox.add(colorBox);
        legendBox.add(textLabel);

        return legendBox;
    }
}