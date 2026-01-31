package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.UserRole;

/**
 * Service class for handling user authentication and registration.
 * This class manages login, registration, and session state.
 */
public class AuthenticationService {

    private Customer currentUser = null;
    private FlightBookingSystem fbs;

    public AuthenticationService(FlightBookingSystem fbs) {
        this.fbs = fbs;
    }


    /**
     * Register a new customer in the system.
     * @param name Full name
     * @param phone Phone number
     * @param email Gmail address
     * @param age Age (must be >= 18)
     * @param username Unique username
     * @param password Password (will be stored)
     * @param foodPreference "Veg" or "Non-Veg"
     * @param hasChildUnderTwo Whether traveling with child under 2
     * @return The newly created Customer object
     * @throws FlightBookingSystemException if validation fails or username/email exists
     */
    public Customer registerCustomer(String name, String phone, String email, int age,
                                     String username, String password, String foodPreference,
                                     boolean hasChildUnderTwo) throws FlightBookingSystemException {

        // Check if username already exists
        for (Customer existing : fbs.getCustomers()) {
            if (existing.getUsername().equalsIgnoreCase(username)) {
                throw new FlightBookingSystemException("Username '" + username + "' already exists.");
            }
            if (existing.getEmail().equalsIgnoreCase(email)) {
                throw new FlightBookingSystemException("Email '" + email + "' already registered.");
            }
        }

        // Generate new ID (same logic as AddCustomer command)
        int nextCustomerId = fbs.getCustomers().size() + 1;

        // Create new customer
        Customer customer = new Customer(nextCustomerId, name, phone, email, age,
                username, password, foodPreference,
                hasChildUnderTwo, UserRole.CUSTOMER);

        // Add to system
        fbs.addCustomer(customer);

        return customer;
    }

    /**
     * Register a default admin user if no admin exists.
     * This should be called once during system initialization.
     * @throws FlightBookingSystemException if admin creation fails
     */
    public void registerDefaultAdmin() throws FlightBookingSystemException {
        // Check if admin already exists
        boolean adminExists = false;
        for (Customer user : fbs.getCustomers()) {
            if (user.isAdmin()) {
                adminExists = true;
                break;
            }
        }

        if (!adminExists) {
            // Create default admin
            int adminId = fbs.getCustomers().size() + 1;
            Customer admin = new Customer(adminId, "System Admin", "00000000000",
                    "admin@gmail.com", 30, "admin", "admin123",
                    "Non-Veg", false, UserRole.ADMIN);
            fbs.addCustomer(admin);
            System.out.println("Default admin created: username='admin', password='admin123'");
        }
    }

    /**
     * Authenticate a user with username and password.
     * @param username Username
     * @param password Password
     * @return true if authentication successful, false otherwise
     */
    public boolean login(String username, String password) {
        for (Customer user : fbs.getCustomers()) {
            if (user.getUsername().equals(username) && user.verifyPassword(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    /**
     * Logout the current user.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Get the currently logged-in user.
     * @return Current Customer object, or null if no user is logged in
     */
    public Customer getCurrentUser() {
        return currentUser;
    }

    /**
     * Check if a user is currently logged in.
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Check if the current user is an admin.
     * @return true if current user is admin, false otherwise or if no user is logged in
     */
    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    /**
     * Check if the current user is a regular customer.
     * @return true if current user is customer, false otherwise or if no user is logged in
     */
    public boolean isCustomer() {
        return currentUser != null && !currentUser.isAdmin();
    }
}
