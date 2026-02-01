package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BookingManagementPanel extends CommandBasePanel {

    public BookingManagementPanel(FlightBookingSystem flightBookingSystem) {
        super(flightBookingSystem);
        initializeBookingManagement();
    }

    private void initializeBookingManagement() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Booking Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton addBookingBtn = new JButton("Add Booking");
        addBookingBtn.addActionListener(e -> showAddBookingDialog());

        JButton cancelBookingBtn = new JButton("Cancel Booking");
        cancelBookingBtn.addActionListener(e -> showCancelBookingDialog());

        JButton editBookingBtn = new JButton("Edit Booking");
        editBookingBtn.addActionListener(e -> showEditBookingDialog());

        buttonPanel.add(addBookingBtn);
        buttonPanel.add(cancelBookingBtn);
        buttonPanel.add(editBookingBtn);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void showAddBookingDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        JTextField customerIdField = new JTextField();
        JTextField flightIdField = new JTextField();

        panel.add(new JLabel("Customer ID:"));
        panel.add(customerIdField);
        panel.add(new JLabel("Flight ID:"));
        panel.add(flightIdField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add Booking", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int customerId = Integer.parseInt(customerIdField.getText().trim());
                int flightId = Integer.parseInt(flightIdField.getText().trim());

                AddBooking command = new AddBooking(customerId, flightId);
                command.execute(flightBookingSystem);
                showSuccess("Booking added successfully!");

            } catch (NumberFormatException e) {
                showError("Invalid IDs. Please enter numbers.");
            } catch (FlightBookingSystemException e) {
                showError(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showCancelBookingDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        JTextField customerIdField = new JTextField();
        JTextField flightIdField = new JTextField();

        panel.add(new JLabel("Customer ID:"));
        panel.add(customerIdField);
        panel.add(new JLabel("Flight ID:"));
        panel.add(flightIdField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Cancel Booking", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int customerId = Integer.parseInt(customerIdField.getText().trim());
                int flightId = Integer.parseInt(flightIdField.getText().trim());

                CancelBooking command = new CancelBooking(customerId, flightId);
                command.execute(flightBookingSystem);
                showSuccess("Booking cancelled successfully!");

            } catch (NumberFormatException e) {
                showError("Invalid IDs. Please enter numbers.");
            } catch (FlightBookingSystemException e) {
                showError(e.getMessage());
            }
        }
    }

    private void showEditBookingDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField customerIdField = new JTextField();
        JTextField newFlightIdField = new JTextField();

        panel.add(new JLabel("Customer ID:"));
        panel.add(customerIdField);
        panel.add(new JLabel("New Flight ID:"));
        panel.add(newFlightIdField);
        panel.add(new JLabel("Note: Prompts for date in console"));
        panel.add(new JLabel(""));

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Edit Booking", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int customerId = Integer.parseInt(customerIdField.getText().trim());
                int newFlightId = Integer.parseInt(newFlightIdField.getText().trim());

                EditBooking command = new EditBooking(customerId, newFlightId);
                command.execute(flightBookingSystem);
                showSuccess("Booking edited successfully!");

            } catch (NumberFormatException e) {
                showError("Invalid IDs. Please enter numbers.");
            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }
}