package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.main.AuthenticationService;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Main customer dashboard window.
 * Provides navigation to all customer features and displays user information.
 */
public class CustomerFrame extends JFrame {
    private FlightBookingSystem fbs;
    private AuthenticationService authService;
    private Customer currentUser;

    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // Panels
    private DashboardView dashboardView;
    private BookFlightView bookFlightView;
    private MyBookingsView myBookingsView;
    private ListFlightsView listFlightsView;
    private EditBookingView editBookingView;
    private CancelBookingView cancelBookingView;
    private UndoCancellationView undoCancellationView;

    /**
     * Creates the main customer interface window.
     * @param fbs the flight booking system for data operations
     * @param authService authentication service for logout
     */
    public CustomerFrame(FlightBookingSystem fbs, AuthenticationService authService) {
        this.fbs = fbs;
        this.authService = authService;
        this.currentUser = fbs.getCurrentUser();

        initializeUI();
        Image icon = Toolkit.getDefaultToolkit().getImage(
                getClass().getClassLoader().getResource("images/logo.png")
        );
        setIconImage(icon);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Handle closing manually

        // Add window listener to save data before closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });

        setSize(1200, 700);
        setLocationRelativeTo(null);
        setTitle("Customer Dashboard - " + currentUser.getName());
        setVisible(true);

        // Load initial data
        refreshAllViews();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        styleButton(logoutBtn, new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> logout());

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Navigation Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(52, 73, 94));
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setLayout(new GridLayout(10, 1, 0, 10));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String[] menuItems = {"Dashboard", "Book Flight", "My Bookings",
                "List Flights", "Edit Booking", "Cancel Booking",
                "Undo Cancellation"};

        for (String item : menuItems) {
            JButton menuBtn = createMenuButton(item);
            sidebarPanel.add(menuBtn);
        }

        add(sidebarPanel, BorderLayout.WEST);

        // Initialize all views
        initializeViews();

        // Main Content Area (CardLayout)
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(Color.WHITE);

        // Add all views to CardLayout
        mainContentPanel.add(dashboardView, "Dashboard");
        mainContentPanel.add(bookFlightView, "Book Flight");
        mainContentPanel.add(myBookingsView, "My Bookings");
        mainContentPanel.add(listFlightsView, "List Flights");
        mainContentPanel.add(editBookingView, "Edit Booking");
        mainContentPanel.add(cancelBookingView, "Cancel Booking");
        mainContentPanel.add(undoCancellationView, "Undo Cancellation");

        add(mainContentPanel, BorderLayout.CENTER);

        // Show dashboard initially
        cardLayout.show(mainContentPanel, "Dashboard");
    }

    private void initializeViews() {
        dashboardView = new DashboardView(fbs, currentUser);
        bookFlightView = new BookFlightView(fbs, currentUser);
        myBookingsView = new MyBookingsView(fbs, currentUser);
        listFlightsView = new ListFlightsView(fbs);
        editBookingView = new EditBookingView(fbs, currentUser);
        cancelBookingView = new CancelBookingView(fbs, currentUser);
        undoCancellationView = new UndoCancellationView(fbs, currentUser);

        // Set up refresh callbacks
        bookFlightView.setOnBookingSuccess(() -> {
            myBookingsView.refresh();
            dashboardView.refresh();
        });

        editBookingView.setOnEditSuccess(() -> {
            myBookingsView.refresh();
            dashboardView.refresh();
        });

        cancelBookingView.setOnCancelSuccess(() -> {
            myBookingsView.refresh();
            dashboardView.refresh();
        });

        undoCancellationView.setOnUndoSuccess(() -> {
            myBookingsView.refresh();
            dashboardView.refresh();
        });
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 90, 110));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(86, 101, 115));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 90, 110));
            }
        });

        button.addActionListener(e -> {
            cardLayout.show(mainContentPanel, text);
            refreshView(text);
        });

        return button;
    }

    private void refreshView(String viewName) {
        switch (viewName) {
            case "Dashboard":
                dashboardView.refresh();
                break;
            case "My Bookings":
                myBookingsView.refresh();
                break;
            case "List Flights":
                listFlightsView.refresh();
                break;
        }
    }

    private void refreshAllViews() {
        dashboardView.refresh();
        myBookingsView.refresh();
        listFlightsView.refresh();
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

    private void handleWindowClosing() {
        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?\nAll data will be saved automatically.",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            try {
                // Save data before exiting
                FlightBookingSystemData.store(fbs);
                dispose();
                System.exit(0);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error saving data: " + e.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?\nAll data will be saved automatically.",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            try {
                // Save data before logging out
                FlightBookingSystemData.store(fbs);
                authService.logout();
                fbs.setCurrentUser(null);
                dispose();
                new RoleSelectionWindow(fbs);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error saving data: " + e.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}