package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookFlightView extends JPanel {
    private FlightBookingSystem fbs;
    private Customer currentUser;

    private JTextField flightIdField;
    private JCheckBox infantCheckBox;
    private JCheckBox vegetarianCheckBox;

    private Runnable onBookingSuccess;

    public BookFlightView(FlightBookingSystem fbs, Customer currentUser) {
        this.fbs = fbs;
        this.currentUser = currentUser;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Book a Flight", SwingConstants.CENTER);
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
        formPanel.add(new JLabel("Flight ID:"), gbc);
        gbc.gridx = 1;
        flightIdField = new JTextField(20);
        formPanel.add(flightIdField, gbc);

        // Infant
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Travelling with infant under 2?"), gbc);
        gbc.gridx = 1;
        infantCheckBox = new JCheckBox();
        formPanel.add(infantCheckBox, gbc);

        // Vegetarian
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Vegetarian meal?"), gbc);
        gbc.gridx = 1;
        vegetarianCheckBox = new JCheckBox();
        formPanel.add(vegetarianCheckBox, gbc);

        // Book Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton bookBtn = new JButton("Book Flight");
        styleButton(bookBtn, new Color(46, 204, 113));
        bookBtn.addActionListener(new BookAction());
        formPanel.add(bookBtn, gbc);

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

    private class BookAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int flightId = Integer.parseInt(flightIdField.getText());
                Flight flight = fbs.getFlightByID(flightId);

                // Check if flight is full
                if (flight.isFull()) {
                    JOptionPane.showMessageDialog(BookFlightView.this,
                            "Flight #" + flightId + " is fully booked!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if customer already booked this flight
                for (Booking booking : currentUser.getBookings()) {
                    if (booking.getFlight().getId() == flightId) {
                        JOptionPane.showMessageDialog(BookFlightView.this,
                                "You already have a booking for flight #" + flightId, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Calculate price
                double currentPrice = flight.getCurrentPrice(fbs.getSystemDate());
                double cancellationFee = flight.getCancellationFee();

                // Create booking
                Booking booking = new Booking(currentUser, flight, fbs.getSystemDate(),
                        currentPrice, cancellationFee, infantCheckBox.isSelected(), vegetarianCheckBox.isSelected());

                currentUser.addBooking(booking);
                flight.addPassenger(currentUser);

                JOptionPane.showMessageDialog(BookFlightView.this,
                        "Booking successful!\n" +
                                "Flight: " + flight.getFlightNumber() + "\n" +
                                "Price: £" + String.format("%.2f", currentPrice) + "\n" +
                                "Cancellation Fee: £" + String.format("%.2f", cancellationFee),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear form
                flightIdField.setText("");
                infantCheckBox.setSelected(false);
                vegetarianCheckBox.setSelected(false);

                // Notify parent of success
                if (onBookingSuccess != null) {
                    onBookingSuccess.run();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BookFlightView.this,
                        "Invalid Flight ID format", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(BookFlightView.this,
                        "Booking failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setOnBookingSuccess(Runnable callback) {
        this.onBookingSuccess = callback;
    }
}