package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditBookingView extends JPanel {
    private FlightBookingSystem fbs;
    private Customer currentUser;

    private JTextField currentFlightIdField;
    private JTextField newFlightIdField;
    private JTextField bookingDateField;
    private JCheckBox updateInfantCheck;
    private JCheckBox newInfantCheckBox;
    private JCheckBox updateVegetarianCheck;
    private JCheckBox newVegetarianCheckBox;

    private Runnable onEditSuccess;

    public EditBookingView(FlightBookingSystem fbs, Customer currentUser) {
        this.fbs = fbs;
        this.currentUser = currentUser;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Edit Booking", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Current Flight ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Current Flight ID:"), gbc);
        gbc.gridx = 1;
        currentFlightIdField = new JTextField(20);
        formPanel.add(currentFlightIdField, gbc);

        // New Flight ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("New Flight ID:"), gbc);
        gbc.gridx = 1;
        newFlightIdField = new JTextField(20);
        formPanel.add(newFlightIdField, gbc);

        // New Booking Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("New Booking Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        bookingDateField = new JTextField(fbs.getSystemDate().toString(), 20);
        formPanel.add(bookingDateField, gbc);

        // Update Infant
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Update Infant Status:"), gbc);
        gbc.gridx = 1;
        updateInfantCheck = new JCheckBox("Change infant status");
        formPanel.add(updateInfantCheck, gbc);
        gbc.gridy = 4;
        gbc.gridx = 1;
        newInfantCheckBox = new JCheckBox("Travelling with infant");
        newInfantCheckBox.setEnabled(false);
        formPanel.add(newInfantCheckBox, gbc);

        // Update Vegetarian
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Update Vegetarian:"), gbc);
        gbc.gridx = 1;
        updateVegetarianCheck = new JCheckBox("Change vegetarian status");
        formPanel.add(updateVegetarianCheck, gbc);
        gbc.gridy = 6;
        gbc.gridx = 1;
        newVegetarianCheckBox = new JCheckBox("Vegetarian meal");
        newVegetarianCheckBox.setEnabled(false);
        formPanel.add(newVegetarianCheckBox, gbc);

        // Edit Button
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton editBtn = new JButton("Edit Booking");
        styleButton(editBtn, new Color(241, 196, 15));
        editBtn.addActionListener(new EditAction());
        formPanel.add(editBtn, gbc);

        // Add listeners to enable/disable checkboxes
        updateInfantCheck.addActionListener(e ->
                newInfantCheckBox.setEnabled(updateInfantCheck.isSelected()));
        updateVegetarianCheck.addActionListener(e ->
                newVegetarianCheckBox.setEnabled(updateVegetarianCheck.isSelected()));

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

    private class EditAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int currentFlightId = Integer.parseInt(currentFlightIdField.getText());
                int newFlightId = Integer.parseInt(newFlightIdField.getText());

                // Find the booking to edit
                Booking bookingToEdit = null;
                for (Booking booking : currentUser.getBookings()) {
                    if (booking.getFlight().getId() == currentFlightId) {
                        bookingToEdit = booking;
                        break;
                    }
                }

                if (bookingToEdit == null) {
                    JOptionPane.showMessageDialog(EditBookingView.this,
                            "No booking found for flight #" + currentFlightId, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Flight newFlight = fbs.getFlightByID(newFlightId);

                if (newFlight.isFull()) {
                    JOptionPane.showMessageDialog(EditBookingView.this,
                            "New flight #" + newFlightId + " is fully booked!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get new booking details
                boolean finalHasInfant = updateInfantCheck.isSelected() ?
                        newInfantCheckBox.isSelected() : bookingToEdit.hasInfant();
                boolean finalIsVegetarian = updateVegetarianCheck.isSelected() ?
                        newVegetarianCheckBox.isSelected() : bookingToEdit.isVegetarian();

                double newPrice = newFlight.getCurrentPrice(fbs.getSystemDate());
                double newCancellationFee = newFlight.getCancellationFee();

                // Cancel old booking
                currentUser.cancelBookingForFlight(bookingToEdit.getFlight());
                bookingToEdit.getFlight().removePassenger(currentUser);

                // Create new booking
                Booking newBooking = new Booking(currentUser, newFlight, fbs.getSystemDate(),
                        newPrice, newCancellationFee, finalHasInfant, finalIsVegetarian);

                currentUser.addBooking(newBooking);
                newFlight.addPassenger(currentUser);

                JOptionPane.showMessageDialog(EditBookingView.this,
                        "Booking updated successfully!\n" +
                                "New Flight: " + newFlight.getFlightNumber() + "\n" +
                                "New Price: £" + String.format("%.2f", newPrice) + "\n" +
                                "Cancellation Fee Charged: £" + String.format("%.2f", bookingToEdit.getCancellationFee()),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear form
                currentFlightIdField.setText("");
                newFlightIdField.setText("");
                bookingDateField.setText(fbs.getSystemDate().toString());
                updateInfantCheck.setSelected(false);
                newInfantCheckBox.setSelected(false);
                newInfantCheckBox.setEnabled(false);
                updateVegetarianCheck.setSelected(false);
                newVegetarianCheckBox.setSelected(false);
                newVegetarianCheckBox.setEnabled(false);

                // Notify parent of success
                if (onEditSuccess != null) {
                    onEditSuccess.run();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(EditBookingView.this,
                        "Invalid Flight ID format", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(EditBookingView.this,
                        "Edit failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setOnEditSuccess(Runnable callback) {
        this.onEditSuccess = callback;
    }
}