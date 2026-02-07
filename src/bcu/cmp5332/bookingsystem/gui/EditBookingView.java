package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.gui.SeatSelectionDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

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
    private Image backgroundImage;

    public EditBookingView(FlightBookingSystem fbs, Customer currentUser) {
        this.fbs = fbs;
        this.currentUser = currentUser;

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/airplane_bg.png")).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }

        initializeUI();
    }

    private void initializeUI() {
        // Use a transparent layered layout
        setLayout(new BorderLayout());
        setOpaque(false); // important for background to show
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Edit Booking", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel (transparent)
        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Current Flight ID ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblCurrent = new JLabel("Current Flight ID:");
        lblCurrent.setForeground(Color.BLACK);
        formPanel.add(lblCurrent, gbc);
        gbc.gridx = 1;
        currentFlightIdField = new JTextField(20);
        makeFieldWhite(currentFlightIdField);
        formPanel.add(currentFlightIdField, gbc);

        // --- New Flight ID ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblNew = new JLabel("New Flight ID:");
        lblNew.setForeground(Color.BLACK);
        formPanel.add(lblNew, gbc);
        gbc.gridx = 1;
        newFlightIdField = new JTextField(20);
        makeFieldWhite(currentFlightIdField);
        formPanel.add(newFlightIdField, gbc);

        // --- New Booking Date ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblDate = new JLabel("New Booking Date (YYYY-MM-DD):");
        lblDate.setForeground(Color.BLACK);
        formPanel.add(lblDate, gbc);
        gbc.gridx = 1;
        bookingDateField = new JTextField(fbs.getSystemDate().toString(), 20);
        makeFieldWhite(currentFlightIdField);
        formPanel.add(bookingDateField, gbc);

        // --- Infant Check ---
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblInfant = new JLabel("Update Infant Status:");
        lblInfant.setForeground(Color.BLACK);
        formPanel.add(lblInfant, gbc);
        gbc.gridx = 1;
        updateInfantCheck = new JCheckBox("Change infant status");
        makeCheckBoxTransparent(updateInfantCheck);
        formPanel.add(updateInfantCheck, gbc);

        gbc.gridy = 4;
        newInfantCheckBox = new JCheckBox("Travelling with infant");
        newInfantCheckBox.setEnabled(false);
        makeCheckBoxTransparent(newInfantCheckBox);
        formPanel.add(newInfantCheckBox, gbc);

        // --- Vegetarian Check ---
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel lblVeg = new JLabel("Update Vegetarian:");
        lblVeg.setForeground(Color.BLACK);
        formPanel.add(lblVeg, gbc);
        gbc.gridx = 1;
        updateVegetarianCheck = new JCheckBox("Change vegetarian status");
        makeCheckBoxTransparent(updateVegetarianCheck);
        formPanel.add(updateVegetarianCheck, gbc);

        gbc.gridy = 6;
        newVegetarianCheckBox = new JCheckBox("Vegetarian meal");
        newVegetarianCheckBox.setEnabled(false);
        makeCheckBoxTransparent(newVegetarianCheckBox);
        formPanel.add(newVegetarianCheckBox, gbc);

        // --- Edit Button ---
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton editBtn = new JButton("Edit Booking");
        styleButton(editBtn, new Color(241, 196, 15));
        editBtn.addActionListener(new EditAction());
        formPanel.add(editBtn, gbc);

        // Enable/disable listeners
        updateInfantCheck.addActionListener(e -> newInfantCheckBox.setEnabled(updateInfantCheck.isSelected()));
        updateVegetarianCheck.addActionListener(e -> newVegetarianCheckBox.setEnabled(updateVegetarianCheck.isSelected()));

        add(formPanel, BorderLayout.CENTER);
    }

    private void makeFieldWhite(JTextField field) {
        field.setOpaque(true);
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        field.setCaretColor(Color.BLACK);
    }


    private void makeCheckBoxTransparent(JCheckBox checkBox) {
        checkBox.setOpaque(false);
        checkBox.setForeground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw full background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
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

    private void clearForm() {
        currentFlightIdField.setText("");
        newFlightIdField.setText("");
        bookingDateField.setText(fbs.getSystemDate().toString());
        updateInfantCheck.setSelected(false);
        newInfantCheckBox.setSelected(false);
        newInfantCheckBox.setEnabled(false);
        updateVegetarianCheck.setSelected(false);
        newVegetarianCheckBox.setSelected(false);
        newVegetarianCheckBox.setEnabled(false);
    };

    private class EditAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int currentFlightId = Integer.parseInt(currentFlightIdField.getText().trim());
                int newFlightId = Integer.parseInt(newFlightIdField.getText().trim());

                // 1. Find the booking to edit
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

                Flight oldFlight = bookingToEdit.getFlight();
                Flight newFlight = fbs.getFlightByID(newFlightId);

                // 2. Seat Selection for the new flight
                SeatSelectionDialog dialog = new SeatSelectionDialog(
                        (JFrame) SwingUtilities.getWindowAncestor(EditBookingView.this),
                        newFlight
                );

                dialog.setVisible(true);
                String newSeat = dialog.getSelectedSeat();
                if (newSeat == null) return;


                if (newFlight.isFull() && currentFlightId != newFlightId) {
                    JOptionPane.showMessageDialog(EditBookingView.this,
                            "New flight is fully booked!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                // 3. Process the change
                // Release old seat
                oldFlight.freeSeat(bookingToEdit.getSeatNumber()); // Make sure your Flight class has freeSeat()
                currentUser.cancelBookingForFlight(oldFlight);
                oldFlight.removePassenger(currentUser);

                // Reserve new seat
                newFlight.bookSeat(newSeat);

                // Get status details
                boolean finalHasInfant = updateInfantCheck.isSelected() ?
                        newInfantCheckBox.isSelected() : bookingToEdit.hasInfant();
                boolean finalIsVegetarian = updateVegetarianCheck.isSelected() ?
                        newVegetarianCheckBox.isSelected() : bookingToEdit.isVegetarian();

                double newPrice = newFlight.getCurrentPrice(fbs.getSystemDate());
                double newCancellationFee = newFlight.getCancellationFee();

                // 4. Create new booking with the seat parameter
                Booking newBooking = new Booking(currentUser, newFlight, fbs.getSystemDate(),
                        newPrice, newCancellationFee, finalHasInfant, finalIsVegetarian, newSeat);

                currentUser.addBooking(newBooking);
                newFlight.addPassenger(currentUser);

                JOptionPane.showMessageDialog(EditBookingView.this,
                        "Booking updated successfully!\n" +
                                "Flight: " + newFlight.getFlightNumber() + "\n" +
                                "Seat: " + newSeat + "\n" +
                                "New Price: Rs." + String.format("%.2f", newPrice),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Reset form (existing code...)
                clearForm();

                if (onEditSuccess != null) {
                    onEditSuccess.run();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(EditBookingView.this, "Invalid Flight ID format", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(EditBookingView.this, "Edit failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setOnEditSuccess(Runnable callback) {
        this.onEditSuccess = callback;
    }
}
