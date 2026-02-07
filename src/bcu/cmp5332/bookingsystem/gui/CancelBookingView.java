package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.data.CancellationDataManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;

public class CancelBookingView extends JPanel {

    private FlightBookingSystem fbs;
    private Customer currentUser;
    private CancellationDataManager cancellationDM;

    private JTextField flightIdField;
    private Runnable onCancelSuccess;
    private Image backgroundImage;

    public CancelBookingView(FlightBookingSystem fbs, Customer currentUser) {
        this.fbs = fbs;
        this.currentUser = currentUser;
        this.cancellationDM = new CancellationDataManager();

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/airplane_bg.png")).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setOpaque(false); // important for background to show
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Title ---
        JLabel titleLabel = new JLabel("Cancel Booking", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- Form Panel (transparent) ---
        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Flight ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblFlight = new JLabel("Flight ID to Cancel:");
        lblFlight.setForeground(Color.WHITE);
        formPanel.add(lblFlight, gbc);

        gbc.gridx = 1;
        flightIdField = new JTextField(20);
        makeFieldWhite(flightIdField);
        formPanel.add(flightIdField, gbc);

        // Cancel Button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton cancelBtn = new JButton("Cancel Booking");
        styleButton(cancelBtn, new Color(231, 76, 60)); // Bright red
        cancelBtn.addActionListener(new CancelAction());
        formPanel.add(cancelBtn, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void makeFieldWhite(JTextField field) {
        field.setOpaque(true);
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        field.setCaretColor(Color.BLACK);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);              // <<< Make button solid
        button.setContentAreaFilled(true);   // <<< Fill with background color

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
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
                    Booking bookingToCancel = null;
                    for (Booking booking : currentUser.getBookings()) {
                        if (booking.getFlight().getId() == flightId) {
                            bookingToCancel = booking;
                            break;
                        }
                    }

                    if (bookingToCancel != null) {
                        Flight flight = bookingToCancel.getFlight();

                        try {
                            cancellationDM.logCancellation(bookingToCancel, LocalDateTime.now());
                        } catch (IOException ioEx) {
                            JOptionPane.showMessageDialog(CancelBookingView.this,
                                    "Failed to log cancellation: \nCancellation aborted.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        currentUser.cancelBookingForFlight(flight);
                        flight.removePassenger(currentUser);

                        JOptionPane.showMessageDialog(CancelBookingView.this,
                                "✓ Booking cancelled successfully!\n" +
                                        "Flight: #" + flightId + " (" + flight.getFlightNumber() + ")\n" +
                                        "Cancellation Fee: Rs." + String.format("%.2f", bookingToCancel.getCancellationFee()),
                                "Cancellation Complete", JOptionPane.INFORMATION_MESSAGE);

                        flightIdField.setText("");

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
