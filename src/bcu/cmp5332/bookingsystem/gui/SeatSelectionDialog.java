package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Flight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SeatSelectionDialog extends JDialog {

    private String selectedSeat = null;

    public SeatSelectionDialog(JFrame parent, Flight flight) {
        super(parent, "Select Your Seat", true);
        setSize(700, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        int seatsPerRow = 6;
        int totalSeats = flight.getCapacity();
        int totalRows = (int) Math.ceil((double) totalSeats / seatsPerRow);
        char[] cols = {'A','B','C','D','E','F'};

        /* ================= HEADER ================= */
        JLabel headerLabel = new JLabel(
                "Flight " + flight.getFlightNumber() +
                        " | Capacity: " + totalSeats +
                        " | Available: " + flight.getRemainingSeats(),
                SwingConstants.CENTER
        );
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(headerLabel, BorderLayout.NORTH);

        /* ================= SEAT GRID ================= */
        JPanel seatPanel = new JPanel(new GridLayout(0, seatsPerRow + 1, 6, 6));
        seatPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Column headers
        seatPanel.add(new JLabel("Row", SwingConstants.CENTER));
        for (char col : cols) {
            JLabel colLabel = new JLabel(String.valueOf(col), SwingConstants.CENTER);
            colLabel.setFont(new Font("Arial", Font.BOLD, 14));
            seatPanel.add(colLabel);
        }

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
                JButton seatBtn = new JButton(seat);
                seatBtn.setFocusPainted(false);
                seatBtn.setFont(new Font("Arial", Font.BOLD, 12));

                if (flight.isSeatAvailable(seat)) {
                    seatBtn.setBackground(new Color(46, 204, 113));
                    seatBtn.setToolTipText("Seat " + seat + " - Available (Click to select)");
                } else {
                    seatBtn.setBackground(new Color(231, 76, 60));
                    seatBtn.setEnabled(false);
                    seatBtn.setToolTipText("Seat " + seat + " - Booked");
                }

                seatBtn.addActionListener((ActionEvent e) -> {
                    selectedSeat = seat;
                    dispose();
                });

                seatPanel.add(seatBtn);
                seatIndex++;
            }
        }

        JScrollPane scrollPane = new JScrollPane(seatPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Seat Map"));
        add(scrollPane, BorderLayout.CENTER);

        /* ================= LEGEND & BUTTON ================= */
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        legendPanel.add(createLegendBox("Available", new Color(46, 204, 113)));
        legendPanel.add(createLegendBox("Booked", new Color(231, 76, 60)));

        bottomPanel.add(legendPanel, BorderLayout.CENTER);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createLegendBox(String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));

        JLabel colorBox = new JLabel("   ");
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        colorBox.setPreferredSize(new Dimension(20, 15));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(colorBox);
        panel.add(label);
        return panel;
    }

    public String getSelectedSeat() {
        return selectedSeat;
    }
}
