package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Flight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MyBookingsView extends JPanel {
    private FlightBookingSystem fbs;
    private Customer currentUser;

    private JTable bookingsTable;
    private DefaultTableModel tableModel;

    public MyBookingsView(FlightBookingSystem fbs, Customer currentUser) {
        this.fbs = fbs;
        this.currentUser = currentUser;
        initializeUI();
        refresh();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("My Bookings", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(44, 62, 80));

        // Create bookings table
        String[] columns = {"Booking ID", "Flight Number", "Origin", "Destination",
                "Departure Date", "Booking Date", "Price Paid",
                "Cancellation Fee", "Infant", "Vegetarian"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookingsTable = new JTable(tableModel);
        bookingsTable.setRowHeight(30);
        bookingsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        bookingsTable.getTableHeader().setBackground(new Color(52, 73, 94));
        bookingsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh() {
        tableModel.setRowCount(0);

        for (Booking booking : currentUser.getBookings()) {
            Flight flight = booking.getFlight();
            Object[] row = {
                    flight.getId() + "-" + currentUser.getId(), // Simple booking ID
                    flight.getFlightNumber(),
                    flight.getOrigin(),
                    flight.getDestination(),
                    flight.getDepartureDate(),
                    booking.getBookingDate(),
                    String.format("£%.2f", booking.getPaidPrice()),
                    String.format("£%.2f", booking.getCancellationFee()),
                    booking.hasInfant() ? "Yes" : "No",
                    booking.isVegetarian() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }
}