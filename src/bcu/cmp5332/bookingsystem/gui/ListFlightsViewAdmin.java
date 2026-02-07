package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Flight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ListFlightsViewAdmin extends JPanel {
    private FlightBookingSystem fbs;
    private JTable flightsTable;
    private DefaultTableModel tableModel;
    private Image backgroundImage;
    private JCheckBox showDeletedCheckBox;
    private JButton deleteFlightBtn;
    private JButton restoreFlightBtn;
    private JButton viewSeatMapBtn;



    public ListFlightsViewAdmin(FlightBookingSystem fbs) {
        this.fbs = fbs;

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/airplane_bg.png")).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }

        setOpaque(false);
        initializeUI();
        refresh();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("All Flights", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(44, 62, 80));

        // ====== NEW: Control Panel with buttons ======
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        // Show deleted flights checkbox
        showDeletedCheckBox = new JCheckBox("Show Deleted Flights");
        showDeletedCheckBox.addActionListener(e -> refresh());

        // Refresh button
        JButton refreshBtn = new JButton("Refresh");
        styleButton(refreshBtn, new Color(52, 152, 219));
        refreshBtn.addActionListener(e -> refresh());

        // View Seat Map Button
        JButton viewSeatMapBtn = new JButton("View Seat Map");
        styleButton(viewSeatMapBtn, new Color(155, 89, 182)); // Purple color
        viewSeatMapBtn.addActionListener(e -> openSeatViewDialog());
        viewSeatMapBtn.setEnabled(false); // Initially disabled
        this.viewSeatMapBtn = viewSeatMapBtn;
        // Delete flight button
        deleteFlightBtn = new JButton("Delete Flight");
        styleButton(deleteFlightBtn, new Color(231, 76, 60)); // Red
        deleteFlightBtn.addActionListener(e -> deleteSelectedFlight());
        deleteFlightBtn.setEnabled(false);

        // Restore flight button
        restoreFlightBtn = new JButton("Restore Flight");
        styleButton(restoreFlightBtn, new Color(46, 204, 113)); // Green
        restoreFlightBtn.addActionListener(e -> restoreSelectedFlight());
        restoreFlightBtn.setEnabled(false);

        controlPanel.add(showDeletedCheckBox);
        controlPanel.add(refreshBtn);
        controlPanel.add(viewSeatMapBtn);
        controlPanel.add(deleteFlightBtn);
        controlPanel.add(restoreFlightBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(controlPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Create flights table with additional columns
        String[] columns = {"ID", "Flight Number", "Origin", "Destination",
                "Departure Date", "Status", "Capacity", "Passengers",
                "Remaining Seats", "Base Price", "Cancellation Fee", "Deleted"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        flightsTable = new JTable(tableModel);
        flightsTable.setRowHeight(30);
        flightsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        flightsTable.getTableHeader().setBackground(new Color(52, 73, 94));
        flightsTable.getTableHeader().setForeground(Color.WHITE);

        // Add selection listener to enable/disable buttons
        flightsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });

        JScrollPane scrollPane = new JScrollPane(flightsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(scrollPane, BorderLayout.CENTER);
    }


    // ADD THIS METHOD to open the seat view dialog
    private void openSeatViewDialog() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a flight first!",
                    "No Flight Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Get flight ID from the selected row (first column)
            int flightId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Flight flight = fbs.getFlightByID(flightId);

            // Open the SeatViewDialog
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            SeatViewDialog dialog = new SeatViewDialog(parentFrame, flight);
            dialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading seat map: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateButtonStates() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow == -1) {
            deleteFlightBtn.setEnabled(false);
            restoreFlightBtn.setEnabled(false);
            viewSeatMapBtn.setEnabled(false); // Disable if no selection
            return;
        }

        boolean isDeleted = (boolean) tableModel.getValueAt(selectedRow, 11); // "Deleted" column

        deleteFlightBtn.setEnabled(!isDeleted);
        restoreFlightBtn.setEnabled(isDeleted);
        viewSeatMapBtn.setEnabled(true); // Enable when a flight is selected
    }

    private void deleteSelectedFlight() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a flight to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int flightId = (int) tableModel.getValueAt(selectedRow, 0);
        String flightNumber = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete flight " + flightNumber + "?\n" +
                        "This will hide the flight from customer view.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Check if flight has bookings
                Flight flight = fbs.getFlightByID(flightId);
                if (!flight.getPassengers().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Cannot delete flight with active bookings.\n" +
                                    "Please cancel all bookings first.",
                            "Delete Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                fbs.deleteFlight(flightId);
                JOptionPane.showMessageDialog(this,
                        "Flight " + flightNumber + " has been deleted (soft delete).",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refresh();

            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting flight: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void restoreSelectedFlight() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a flight to restore.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int flightId = (int) tableModel.getValueAt(selectedRow, 0);
        String flightNumber = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to restore flight " + flightNumber + "?\n" +
                        "This will make the flight visible to customers again.",
                "Confirm Restore", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                fbs.restoreFlight(flightId);
                JOptionPane.showMessageDialog(this,
                        "Flight " + flightNumber + " has been restored.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refresh();

            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error restoring flight: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refresh() {
        List<Flight> flights;
        if (showDeletedCheckBox.isSelected()) {
            // Show all flights including deleted ones
            flights = fbs.getAllFlights();
        } else {
            // Show only non-deleted flights (for admin)
            flights = fbs.getFlightsForAdmin();
        }

        tableModel.setRowCount(0);

        for (Flight flight : flights) {
            String status;
            if (flight.isDeleted()) {
                status = "Deleted";
            } else if (flight.hasDeparted(fbs.getSystemDate())) {
                status = "Departed";
            } else {
                status = "Active";
            }

            Object[] row = {
                    flight.getId(),
                    flight.getFlightNumber(),
                    flight.getOrigin(),
                    flight.getDestination(),
                    flight.getDepartureDate(),
                    status,
                    flight.getCapacity(),
                    flight.getPassengers().size(),
                    flight.getRemainingSeats(),
                    String.format("Rs.%.2f", flight.getBasePrice()),
                    String.format("Rs.%.2f", flight.getCancellationFee()),
                    flight.isDeleted()
            };
            tableModel.addRow(row);
        }

        // Update button states after refresh
        updateButtonStates();
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }
}