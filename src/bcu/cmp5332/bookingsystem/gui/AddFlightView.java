package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddFlightView extends JPanel {
    private FlightBookingSystem fbs;

    private JTextField flightNumberField;
    private JTextField originField;
    private JTextField destinationField;
    private JTextField departureDateField;
    private JSpinner capacitySpinner;
    private JTextField basePriceField;
    private JTextField cancellationFeeField;

    private Runnable onFlightAdded;

    public AddFlightView(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Add New Flight", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Flight Number
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Flight Number:"), gbc);
        gbc.gridx = 1;
        flightNumberField = new JTextField(20);
        formPanel.add(flightNumberField, gbc);

        // Origin
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Origin:"), gbc);
        gbc.gridx = 1;
        originField = new JTextField(20);
        formPanel.add(originField, gbc);

        // Destination
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Destination:"), gbc);
        gbc.gridx = 1;
        destinationField = new JTextField(20);
        formPanel.add(destinationField, gbc);

        // Departure Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Departure Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        departureDateField = new JTextField(20);
        formPanel.add(departureDateField, gbc);

        // Capacity
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1;
        capacitySpinner = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 1));
        formPanel.add(capacitySpinner, gbc);

        // Base Price
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Base Price (£):"), gbc);
        gbc.gridx = 1;
        basePriceField = new JTextField(20);
        formPanel.add(basePriceField, gbc);

        // Cancellation Fee
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Cancellation Fee (£):"), gbc);
        gbc.gridx = 1;
        cancellationFeeField = new JTextField(20);
        formPanel.add(cancellationFeeField, gbc);

        // Add Button
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addBtn = new JButton("Add Flight");
        styleButton(addBtn, new Color(46, 204, 113));
        addBtn.addActionListener(new AddFlightAction());
        formPanel.add(addBtn, gbc);

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

    private class AddFlightAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                LocalDate departureDate = LocalDate.parse(departureDateField.getText());
                double basePrice = Double.parseDouble(basePriceField.getText());
                double cancellationFee = Double.parseDouble(cancellationFeeField.getText());
                int capacity = (Integer) capacitySpinner.getValue();

                // Create AddFlight command
                AddFlight addFlightCommand = new AddFlight(
                        flightNumberField.getText(),
                        originField.getText(),
                        destinationField.getText(),
                        departureDate,
                        basePrice,
                        cancellationFee,
                        capacity
                );

                addFlightCommand.execute(fbs);

                JOptionPane.showMessageDialog(AddFlightView.this,
                        "Flight added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear form
                flightNumberField.setText("");
                originField.setText("");
                destinationField.setText("");
                departureDateField.setText("");
                capacitySpinner.setValue(100);
                basePriceField.setText("");
                cancellationFeeField.setText("");

                // Notify parent
                if (onFlightAdded != null) {
                    onFlightAdded.run();
                }

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(AddFlightView.this,
                        "Invalid date format. Please use YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(AddFlightView.this,
                        "Invalid number format for price", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(AddFlightView.this,
                        "Failed to add flight: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setOnFlightAdded(Runnable callback) {
        this.onFlightAdded = callback;
    }
}