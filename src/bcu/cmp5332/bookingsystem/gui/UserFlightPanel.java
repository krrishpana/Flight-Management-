package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;

public class UserFlightPanel extends CommandBasePanel {

    public UserFlightPanel(FlightBookingSystem flightBookingSystem) {
        super(flightBookingSystem);
        initializeUserFlightPanel();
    }

    private void initializeUserFlightPanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Flight Information", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton listFlightsBtn = new JButton("View All Flights");
        listFlightsBtn.addActionListener(e -> executeListFlights());

        JButton showFlightBtn = new JButton("View Flight Details");
        showFlightBtn.addActionListener(e -> showFlightDetailsDialog());

        buttonPanel.add(listFlightsBtn);
        buttonPanel.add(showFlightBtn);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void executeListFlights() {
        clearOutput();
        try {
            ListFlights command = new ListFlights();
            command.execute(flightBookingSystem);
            appendOutput("Flight list displayed.");
        } catch (FlightBookingSystemException e) {
            showError(e.getMessage());
        }
    }

    private void showFlightDetailsDialog() {
        String flightIdStr = JOptionPane.showInputDialog(this,
                "Enter Flight ID:", "View Flight Details", JOptionPane.QUESTION_MESSAGE);

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