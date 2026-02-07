package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.data.CancellationDataManager;
import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UndoCancellationView extends JPanel {
    private FlightBookingSystem fbs;
    private Customer currentUser;
    private CancellationDataManager cancellationDM;

    private JTextField flightIdField;
    private JTextArea resultArea;
    private Runnable onUndoSuccess;
    private Image backgroundImage;

    public UndoCancellationView(FlightBookingSystem fbs, Customer currentUser) {
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
        setOpaque(false); // Important for background to show
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Title ---
        JLabel titleLabel = new JLabel("Undo Cancellation", SwingConstants.CENTER);
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
        JLabel lblFlight = new JLabel("Flight ID to Undo Cancellation:");
        lblFlight.setForeground(Color.WHITE);
        formPanel.add(lblFlight, gbc);

        gbc.gridx = 1;
        flightIdField = new JTextField(20);
        makeFieldTransparent(flightIdField);
        formPanel.add(flightIdField, gbc);

        // Check Button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton checkBtn = new JButton("Check Cancellation");
        styleButton(checkBtn, new Color(52, 152, 219)); // Blue
        checkBtn.addActionListener(new CheckCancellationAction());
        formPanel.add(checkBtn, gbc);

        // Undo Button
        gbc.gridy = 2;
        JButton undoBtn = new JButton("Undo Cancellation");
        styleButton(undoBtn, new Color(46, 204, 113)); // Green
        undoBtn.addActionListener(new UndoAction());
        undoBtn.setEnabled(false); // Disabled until check
        formPanel.add(undoBtn, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Result Area
        resultArea = new JTextArea(8, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setBorder(BorderFactory.createTitledBorder("Cancellation Details"));
        resultArea.setBackground(new Color(0,0,0,50)); // Slightly transparent over bg
        resultArea.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void makeFieldTransparent(JTextField field) {
        field.setOpaque(false);
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        field.setCaretColor(Color.WHITE);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

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

    // --- Action Listeners ---
    private class CheckCancellationAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String flightIdStr = flightIdField.getText().trim();
                if (flightIdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(UndoCancellationView.this,
                            "Please enter a Flight ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int flightId = Integer.parseInt(flightIdStr);
                CancelledBooking cancelled = cancellationDM.findCancellation(currentUser.getId(), flightId);

                resultArea.setText("");

                if (cancelled == null) {
                    resultArea.append("No cancellation found for:\n");
                    resultArea.append("  Customer ID: " + currentUser.getId() + "\n");
                    resultArea.append("  Flight ID: " + flightId + "\n\n");
                    resultArea.append("Please check the IDs and try again.");
                    return;
                }

                // Display cancellation details
                resultArea.append("CANCELLATION FOUND:\n");
                resultArea.append("=".repeat(40) + "\n");
                resultArea.append("Customer ID: " + cancelled.getCustomerId() + "\n");
                resultArea.append("Flight ID: " + cancelled.getFlightId() + "\n");
                resultArea.append("Booking Date: " + cancelled.getBookingDate() + "\n");
                resultArea.append("Paid Price: Rs." + String.format("%.2f", cancelled.getPaidPrice()) + "\n");
                resultArea.append("Cancellation Fee: Rs." + String.format("%.2f", cancelled.getCancellationFee()) + "\n");
                resultArea.append("Cancellation Time: " + cancelled.getCancellationTime() + "\n");
                resultArea.append("Undo Deadline: " + cancelled.getUndoDeadline() + "\n");
                resultArea.append("Infant: " + (cancelled.hadInfant() ? "Yes" : "No") + "\n");
                resultArea.append("Vegetarian: " + (cancelled.wasVegetarian() ? "Yes" : "No") + "\n");
                resultArea.append("\n");

                if (cancelled.canUndo()) {
                    resultArea.append("✓ UNDO AVAILABLE!\n");
                    resultArea.append("Time Remaining: " + cancelled.getTimeRemaining() + "\n");
                    resultArea.append("\nYou can now undo this cancellation.");

                    // Enable undo button
                    for (Component comp : getComponents()) {
                        if (comp instanceof JPanel) {
                            for (Component btn : ((JPanel) comp).getComponents()) {
                                if (btn instanceof JButton && ((JButton) btn).getText().equals("Undo Cancellation")) {
                                    ((JButton) btn).setEnabled(true);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    resultArea.append("✗ UNDO NOT AVAILABLE\n");
                    resultArea.append("Reason: " + cancelled.getTimeRemaining() + "\n");
                    resultArea.append("The 24-hour undo period has expired.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(UndoCancellationView.this,
                        "Invalid Flight ID format", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(UndoCancellationView.this,
                        "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class UndoAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String flightIdStr = flightIdField.getText().trim();
                int flightId = Integer.parseInt(flightIdStr);

                CancelledBooking cancelled = cancellationDM.findCancellation(currentUser.getId(), flightId);

                if (cancelled == null || !cancelled.canUndo()) {
                    JOptionPane.showMessageDialog(UndoCancellationView.this,
                            "Cannot undo this cancellation.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Flight flight = fbs.getFlightByID(flightId);
                if (flight.isFull()) {
                    JOptionPane.showMessageDialog(UndoCancellationView.this,
                            "Cannot undo: Flight is fully booked.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Booking restoredBooking = new Booking(
                        currentUser, flight, cancelled.getBookingDate(),
                        cancelled.getPaidPrice(), cancelled.getCancellationFee(),
                        cancelled.hadInfant(), cancelled.wasVegetarian(),
                        cancelled.getSeatNumber()
                );

                currentUser.addBooking(restoredBooking);
                flight.addPassenger(currentUser);

                cancellationDM.removeCancellation(currentUser.getId(), flightId);

                JOptionPane.showMessageDialog(UndoCancellationView.this,
                        "Cancellation successfully undone!\n" +
                                "Booking restored for flight #" + flightId + ".",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                flightIdField.setText("");
                resultArea.setText("");

                for (Component comp : getComponents()) {
                    if (comp instanceof JPanel) {
                        for (Component btn : ((JPanel) comp).getComponents()) {
                            if (btn instanceof JButton && ((JButton) btn).getText().equals("Undo Cancellation")) {
                                ((JButton) btn).setEnabled(false);
                                break;
                            }
                        }
                    }
                }

                if (onUndoSuccess != null) {
                    onUndoSuccess.run();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(UndoCancellationView.this,
                        "Invalid Flight ID format", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(UndoCancellationView.this,
                        "Undo failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(UndoCancellationView.this,
                        "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setOnUndoSuccess(Runnable callback) {
        this.onUndoSuccess = callback;
    }
}
