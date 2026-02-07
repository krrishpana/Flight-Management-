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

    private Image backgroundImage;

    public AddFlightView(FlightBookingSystem fbs) {
        this.fbs = fbs;

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/airplane_bg.png")).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }

        setOpaque(false); // panel transparent to show background
        initializeUI();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Add New Flight", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);

        // === Form Panel Transparent ===
        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            public boolean isOpaque() {
                return false; // transparent
            }
        };
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Flight Number
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblFlight = new JLabel("Flight Number:");
        lblFlight.setForeground(Color.WHITE);
        formPanel.add(lblFlight, gbc);
        gbc.gridx = 1;
        flightNumberField = new JTextField(20);
        makeFieldTransparent(flightNumberField);
        formPanel.add(flightNumberField, gbc);

        // Origin
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblOrigin = new JLabel("Origin:");
        lblOrigin.setForeground(Color.WHITE);
        formPanel.add(lblOrigin, gbc);
        gbc.gridx = 1;
        originField = new JTextField(20);
        makeFieldTransparent(originField);
        formPanel.add(originField, gbc);

        // Destination
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblDest = new JLabel("Destination:");
        lblDest.setForeground(Color.WHITE);
        formPanel.add(lblDest, gbc);
        gbc.gridx = 1;
        destinationField = new JTextField(20);
        makeFieldTransparent(destinationField);
        formPanel.add(destinationField, gbc);

        // Departure Date
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblDate = new JLabel("Departure Date (YYYY-MM-DD):");
        lblDate.setForeground(Color.WHITE);
        formPanel.add(lblDate, gbc);
        gbc.gridx = 1;
        departureDateField = new JTextField(20);
        makeFieldTransparent(departureDateField);
        formPanel.add(departureDateField, gbc);

        // Capacity
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblCapacity = new JLabel("Capacity:");
        lblCapacity.setForeground(Color.WHITE);
        formPanel.add(lblCapacity, gbc);
        gbc.gridx = 1;
        capacitySpinner = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 1));
        ((JSpinner.DefaultEditor) capacitySpinner.getEditor()).getTextField().setOpaque(false);
        ((JSpinner.DefaultEditor) capacitySpinner.getEditor()).getTextField().setForeground(Color.WHITE);
        capacitySpinner.setOpaque(false);
        formPanel.add(capacitySpinner, gbc);

        // Base Price
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblPrice = new JLabel("Base Price (Rs.):");
        lblPrice.setForeground(Color.WHITE);
        formPanel.add(lblPrice, gbc);
        gbc.gridx = 1;
        basePriceField = new JTextField(20);
        makeFieldTransparent(basePriceField);
        formPanel.add(basePriceField, gbc);

        // Cancellation Fee
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblFee = new JLabel("Cancellation Fee (Rs.):");
        lblFee.setForeground(Color.WHITE);
        formPanel.add(lblFee, gbc);
        gbc.gridx = 1;
        cancellationFeeField = new JTextField(20);
        makeFieldTransparent(cancellationFeeField);
        formPanel.add(cancellationFeeField, gbc);

        // Add Button (opaque)
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton addBtn = new JButton("Add Flight");
        styleButton(addBtn, new Color(46, 204, 113));
        addBtn.addActionListener(new AddFlightAction());
        formPanel.add(addBtn, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void makeFieldTransparent(JTextField field) {
        field.setOpaque(true);               // solid background
        field.setBackground(Color.WHITE);    // white background
        field.setForeground(Color.BLACK);    // black text
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // optional: gray border
        field.setCaretColor(Color.BLACK);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true); // button solid

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

                flightNumberField.setText("");
                originField.setText("");
                destinationField.setText("");
                departureDateField.setText("");
                capacitySpinner.setValue(100);
                basePriceField.setText("");
                cancellationFeeField.setText("");

                if (onFlightAdded != null) onFlightAdded.run();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(AddFlightView.this,
                        "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setOnFlightAdded(Runnable callback) {
        this.onFlightAdded = callback;
    }
}
