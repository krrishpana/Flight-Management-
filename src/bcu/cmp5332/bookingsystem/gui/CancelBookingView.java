package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.data.CancellationDataManager;  // ADD THIS IMPORT

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;  // ADD THIS IMPORT
import java.time.LocalDateTime;  // ADD THIS IMPORT

public class CancelBookingView extends JPanel {
    private FlightBookingSystem fbs;
    private Customer currentUser;
    private CancellationDataManager cancellationDM;  // ADD THIS FIELD

    private JTextField flightIdField;

    private Runnable onCancelSuccess;

    public CancelBookingView(FlightBookingSystem fbs, Customer currentUser) {
        this.fbs = fbs;
        this.currentUser = currentUser;
        this.cancellationDM = new CancellationDataManager();  // INITIALIZE IT
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Cancel Booking", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Flight ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Flight ID to Cancel:"), gbc);
        gbc.gridx = 1;
        flightIdField = new JTextField(20);
        formPanel.add(flightIdField, gbc);

        // Cancel Button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton cancelBtn = new JButton("Cancel Booking");
        styleButton(cancelBtn, new Color(231, 76, 60));
        cancelBtn.addActionListener(new CancelAction());
        formPanel.add(cancelBtn, gbc);

        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
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

    private class CancelAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String flightIdStr = flightIdField.getText().trim();
                if (flightIdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(CancelBookingView.this,
                            "Please enter a Flight ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int flightId = Integer.parseInt(flightIdStr);

                int response = JOptionPane.showConfirmDialog(CancelBookingView.this,
                        "Are you sure you want to cancel booking for flight #" + flightId + "?\n" +
                                "Cancellation fee will be charged.\n" +
                                "You will have 24 hours to undo this cancellation.",
                        "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    // Find the booking to cancel
                    Booking bookingToCancel = null;
                    for (Booking booking : currentUser.getBookings()) {
                        if (booking.getFlight().getId() == flightId) {
                            bookingToCancel = booking;
                            break;
                        }
                    }

                    if (bookingToCancel != null) {
                        Flight flight = bookingToCancel.getFlight();

                        // LOG THE CANCELLATION FIRST - THIS IS THE CRITICAL MISSING STEP!
                        try {
                            cancellationDM.logCancellation(bookingToCancel, LocalDateTime.now());
                            System.out.println("Cancellation logged for flight #" + flightId +
                                    ", customer #" + currentUser.getId());
                        } catch (IOException ioEx) {
                            JOptionPane.showMessageDialog(CancelBookingView.this,
                                    "Failed to log cancellation: " +
                                            "\nCancellation aborted.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Now remove the booking
                        currentUser.cancelBookingForFlight(flight);
                        flight.removePassenger(currentUser);

                        JOptionPane.showMessageDialog(CancelBookingView.this,
                                "✓ Booking cancelled successfully!\n\n" +
                                        "Cancellation Details:\n" +
                                        "• Flight: #" + flightId + " (" + flight.getFlightNumber() + ")\n" +
                                        "• Cancellation Fee: £" + String.format("%.2f", bookingToCancel.getCancellationFee()) + "\n" +
                                        "• Cancellation Time: " + LocalDateTime.now() + "\n" +
                                        "• Undo Available For: 24 hours\n\n" +
                                        "To undo cancellation:\n" +
                                        "1. Go to 'Undo Cancellation' tab\n" +
                                        "2. Enter Flight ID: " + flightId + "\n" +
                                        "3. Click 'Undo Cancellation'",
                                "Cancellation Complete", JOptionPane.INFORMATION_MESSAGE);

                        // Clear form
                        flightIdField.setText("");

                        // Notify parent of success
                        if (onCancelSuccess != null) {
                            onCancelSuccess.run();
                        }
                    } else {
                        JOptionPane.showMessageDialog(CancelBookingView.this,
                                "No booking found for flight #" + flightId, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(CancelBookingView.this,
                        "Invalid Flight ID format. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(CancelBookingView.this,
                        "Cancellation failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CancelBookingView.this,
                        "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public void setOnCancelSuccess(Runnable callback) {
        this.onCancelSuccess = callback;
    }
}