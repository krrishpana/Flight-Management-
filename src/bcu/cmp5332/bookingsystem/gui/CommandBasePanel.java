package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;

public abstract class CommandBasePanel extends JPanel {
    protected FlightBookingSystem flightBookingSystem;
    protected JTextArea outputArea;

    public CommandBasePanel(FlightBookingSystem flightBookingSystem) {
        this.flightBookingSystem = flightBookingSystem;
        initializeComponents();
    }

    protected void initializeComponents() {
        setLayout(new BorderLayout());

        // Create output area
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));

        add(scrollPane, BorderLayout.SOUTH);
    }

    protected void appendOutput(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    protected void clearOutput() {
        outputArea.setText("");
    }

    protected void showError(String message) {
        appendOutput("ERROR: " + message);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showSuccess(String message) {
        appendOutput("SUCCESS: " + message);
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}