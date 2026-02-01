//package bcu.cmp5332.bookingsystem.gui;
//
//import bcu.cmp5332.bookingsystem.commands.*;
//import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
//import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class UserBookingPanel extends CommandBasePanel {
//
//    public UserBookingPanel(FlightBookingSystem flightBookingSystem) {
//        super(flightBookingSystem);
//        initializeUserBookingPanel();
//    }
//
//    private void initializeUserBookingPanel() {
//        setLayout(new BorderLayout());
//
//        // Title
//        JLabel titleLabel = new JLabel("My Bookings", SwingConstants.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
//        add(titleLabel, BorderLayout.NORTH);
//
//        // Instructions
//        JTextArea instructions = new JTextArea(2, 50);
//        instructions.setText("Note: You need to know your Customer ID to make or cancel bookings.\n" +
//                "Contact an admin if you don't know your Customer ID.");
//        instructions.setEditable(false);
//        instructions.setBackground(getBackground());
//        instructions.setFont(new Font("Arial", Font.PLAIN, 12));
//
//        add(instructions, BorderLayout.NORTH);
//
//        // Button panel
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
//
//        JButton addBookingBtn = new JButton("Make a Booking");
//        addBookingBtn.addActionListener(e -> showAddBookingDialog());
//
//        JButton cancelBookingBtn = new JButton("Cancel a Booking");
//        cancelBookingBtn.addActionListener(e -> showCancelBookingDialog());
//
//        buttonPanel.add(addBookingBtn);
//        buttonPanel.add(cancelBookingBtn);
//
//        add(buttonPanel, BorderLayout.CENTER);
//    }
//
//    private void showAddBookingDialog() {
//        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
//
//        JTextField customerIdField = new JTextField();
//        JTextField flightIdField = new JTextField();
//
//        panel.add(new JLabel("Your Customer ID:"));
//        panel.add(customerIdField);
//        panel.add(new JLabel("Flight ID:"));
//        panel.add(flightIdField);
//
//        int result = JOptionPane.showConfirmDialog(this, panel,
//                "Make Booking", JOptionPane.OK_CANCEL_OPTION);
//
//        if (result == JOptionPane.OK_OPTION) {
//            try {
//                int customerId = Integer.parseInt(customerIdField.getText().trim());
//                int flightId = Integer.parseInt(flightIdField.getText().trim());
//
//                AddBooking command = new AddBooking(customerId, flightId);
//                command.execute(flightBookingSystem);
//                showSuccess("Booking made successfully!");
//
//            } catch (NumberFormatException e) {
//                showError("Invalid IDs. Please enter numbers.");
//            } catch (FlightBookingSystemException e) {
//                showError(e.getMessage());
//            }
//        }
//    }
//
//    private void showCancelBookingDialog() {
//        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
//
//        JTextField customerIdField = new JTextField();
//        JTextField flightIdField = new JTextField();
//
//        panel.add(new JLabel("Your Customer ID:"));
//        panel.add(customerIdField);
//        panel.add(new JLabel("Flight ID to Cancel:"));
//        panel.add(flightIdField);
//
//        int result = JOptionPane.showConfirmDialog(this, panel,
//                "Cancel Booking", JOptionPane.OK_CANCEL_OPTION);
//
//        if (result == JOptionPane.OK_OPTION) {
//            try {
//                int customerId = Integer.parseInt(customerIdField.getText().trim());
//                int flightId = Integer.parseInt(flightIdField.getText().trim());
//
//                CancelBooking command = new CancelBooking(customerId, flightId);
//                command.execute(flightBookingSystem);
//                showSuccess("Booking cancelled successfully!");
//
//            } catch (NumberFormatException e) {
//                showError("Invalid IDs. Please enter numbers.");
//            } catch (FlightBookingSystemException e) {
//                showError(e.getMessage());
//            }
//        }
//    }
//}