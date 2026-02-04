package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.main.AuthenticationService;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The main entry window for the Flight Booking System.
 * Provides options to register or login as different user types.
 */
public class RoleSelectionWindow extends JFrame {

    private FlightBookingSystem fbs;
    private AuthenticationService authService;

    /**
     * Creates the role selection window as the application entry point.
     * @param fbs the flight booking system to use
     */
    public RoleSelectionWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        this.authService = new AuthenticationService(fbs);

        initializeUI();

        Image icon = Toolkit.getDefaultToolkit().getImage(
                getClass().getClassLoader().getResource("images/logo.png")
        );
        setIconImage(icon);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(420, 420);
        setLocationRelativeTo(null);
        setTitle("Phe Airlines");
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel titleLabel = new JLabel("Phe Airlines");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Flight Booking System");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon logoIcon = new ImageIcon(
                getClass().getClassLoader().getResource("images/logo.png")
        );

        Image scaledImage = logoIcon.getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH);

        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(logoLabel);

        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(236, 240, 241));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton registerBtn = new JButton("Register");
        styleButton(registerBtn, new Color(46, 204, 113));
        registerBtn.setPreferredSize(new Dimension(150, 40));

        JButton loginBtn = new JButton("Login");
        styleButton(loginBtn, new Color(52, 152, 219));
        loginBtn.setPreferredSize(new Dimension(150, 40));

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(registerBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(loginBtn, gbc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(52, 73, 94));

        JLabel footerLabel = new JLabel("© 2026  Phe Airlines");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);

        add(footerPanel, BorderLayout.SOUTH);

        registerBtn.addActionListener(e ->
                new RegisterWindow(fbs, authService, RoleSelectionWindow.this));

        loginBtn.addActionListener(e ->
                new LoginWindow(fbs, authService, RoleSelectionWindow.this));
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
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

    private void handleWindowClosing() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?\nAll data will be saved.",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            try {
                FlightBookingSystemData.store(fbs);
                dispose();
                System.exit(0);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error saving data: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
