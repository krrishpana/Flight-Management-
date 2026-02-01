package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FlightManagementPanel extends CommandBasePanel {

    public FlightManagementPanel(FlightBookingSystem flightBookingSystem) {
        super(flightBookingSystem);
        initializeFlightManagement();
    }

    private void initializeFlightManagement() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Flight Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton listFlightsBtn = new JButton("List All Flights");
        listFlightsBtn.addActionListener(e -> executeListFlights());

        JButton addFlightBtn = new JButton("Add New Flight");
        addFlightBtn.addActionListener(e -> showAddFlightDialog());

        JButton showFlightBtn = new JButton("Show Flight Details");
        showFlightBtn.addActionListener(e -> showFlightDetailsDialog());

        buttonPanel.add(listFlightsBtn);
        buttonPanel.add(addFlightBtn);
        buttonPanel.add(showFlightBtn);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void executeListFlights() {
        clearOutput();
        try {
            ListFlights command = new ListFlights();
            command.execute(flightBookingSystem);
            appendOutput("List flights command executed successfully.");
        } catch (FlightBookingSystemException e) {
            showError(e.getMessage());
        }
    }

    private void showAddFlightDialog() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        JTextField flightNumberField = new JTextField();
        JTextField originField = new JTextField();
        JTextField destinationField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField capacityField = new JTextField();
        JTextField basePriceField = new JTextField();
        JTextField cancellationFeeField = new JTextField();

        panel.add(new JLabel("Flight Number:"));
        panel.add(flightNumberField);
        panel.add(new JLabel("Origin:"));
        panel.add(originField);
        panel.add(new JLabel("Destination:"));
        panel.add(destinationField);
        panel.add(new JLabel("Departure Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Capacity:"));
        panel.add(capacityField);
        panel.add(new JLabel("Base Price (£):"));
        panel.add(basePriceField);
        panel.add(new JLabel("Cancellation Fee (£):"));
        panel.add(cancellationFeeField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Flight", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String flightNumber = flightNumberField.getText().trim();
                String origin = originField.getText().trim();
                String destination = destinationField.getText().trim();
                LocalDate departureDate = null;
                try {
                    departureDate = LocalDate.parse(dateField.getText().trim());
                }
                catch (DateTimeParseException dtpe) {
                    throw new FlightBookingSystemException("Date must be in YYYY-MM-DD format");
                }

                // NEW: Parse additional fields
                int capacity = Integer.parseInt(capacityField.getText().trim());
                double basePrice = Double.parseDouble(basePriceField.getText().trim());
                double cancellationFee = Double.parseDouble(cancellationFeeField.getText().trim());

                if (flightNumber.isEmpty() || origin.isEmpty() || destination.isEmpty()) {
                    throw new IllegalArgumentException("All fields are required.");
                }

                if (capacity <= 0) {
                    throw new IllegalArgumentException("Capacity must be positive.");
                }
                if (basePrice < 0) {
                    throw new IllegalArgumentException("Base price cannot be negative.");
                }
                if (cancellationFee < 0) {
                    throw new IllegalArgumentException("Cancellation fee cannot be negative.");
                }

                // FIXED: Use new constructor with all 7 parameters
                Command addFlight = new AddFlight(flightNumber, origin, destination,
                        departureDate, basePrice, cancellationFee,capacity);
                addFlight.execute(flightBookingSystem);
                showSuccess("Flight added successfully!");

            } catch (DateTimeParseException e) {
                showError("Invalid date format. Please use YYYY-MM-DD.");
            } catch (NumberFormatException e) {
                showError("Invalid number format. Please enter valid numbers for capacity, price, and fee.");
            } catch (IllegalArgumentException | FlightBookingSystemException e) {
                showError(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showFlightDetailsDialog() {
        String flightIdStr = JOptionPane.showInputDialog(this,
                "Enter Flight ID:", "Show Flight Details", JOptionPane.QUESTION_MESSAGE);

        if (flightIdStr != null && !flightIdStr.trim().isEmpty()) {
            try {
                int flightId = Integer.parseInt(flightIdStr.trim());
                ShowFlight command = new ShowFlight(flightId);
                command.execute(flightBookingSystem);
                appendOutput("Flight details displayed above.");
            } catch (NumberFormatException e) {
                showError("Invalid Flight ID. Please enter a number.");
            } catch (FlightBookingSystemException e) {
                showError(e.getMessage());
            }
        }
    }
}