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
 * Provides options to register or login.
 */
public class RoleSelectionWindow extends JFrame {

    private final FlightBookingSystem fbs;
    private final AuthenticationService authService;
    private Image backgroundImage;

    public RoleSelectionWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        this.authService = new AuthenticationService(fbs);

        try {
            backgroundImage = new ImageIcon(
                    getClass().getResource("/images/bg2.png")
            ).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }

        initializeUI();

        Image icon = Toolkit.getDefaultToolkit().getImage(
                getClass().getClassLoader().getResource("images/logo.png")
        );
        setIconImage(icon);

        setTitle("Phe Airlines");
        setSize(420, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }

    private void initializeUI() {

        // Background panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setPaint(new GradientPaint(
                            0, 0, new Color(41, 128, 185),
                            getWidth(), getHeight(), new Color(86, 204, 242)
                    ));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ================= Buttons =================
        JButton registerBtn = createStyledButton("Register", new Color(46, 204, 113, 200));
        registerBtn.addActionListener(e ->
                new RegisterWindow(fbs, authService, this)
        );

        JButton loginBtn = createStyledButton("Login", new Color(52, 152, 219, 200));
        loginBtn.addActionListener(e ->
                new LoginWindow(fbs, authService, this)
        );

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(registerBtn);
        buttonPanel.add(loginBtn);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 30, 40));
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        // ================= Footer =================
        JLabel footerLabel = new JLabel("© 2026 Flight Booking System", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        footerLabel.setForeground(new Color(255, 255, 255, 150));

        bottomPanel.add(footerLabel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setPreferredSize(new Dimension(1, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void handleWindowClosing() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the application?\nAll data will be saved automatically.",
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
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}

