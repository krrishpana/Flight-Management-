package bcu.cmp5332.bookingsystem.model;

import java.util.ArrayList;
import java.util.List;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

/**
 * Represents a customer in the flight booking system.
 * Customers can make bookings and have login credentials for the system.
 */
public class Customer {

    private int id;
    private String name;
    private String phone;
    private String email;
    private int age;
    private String username;
    private String password;
    private UserRole role;
    private boolean deleted = false;
    private final List<Booking> bookings = new ArrayList<>();

    /**
     * Creates a customer with basic details and default registration values.
     * Used for backward compatibility with older data formats.
     * @param id unique customer identifier
     * @param name customer's full name
     * @param phone customer's phone number
     * @throws FlightBookingSystemException if validation fails
     */
    public Customer(int id, String name, String phone) throws FlightBookingSystemException {
        this(id, name, phone,
                "default@gmail.com",  // email
                18,                   // age
                "user" + id,         // username
                "password123",       // password
                UserRole.CUSTOMER);  // role
    }

    /**
     * Creates a fully specified customer with all registration details.
     * @param id unique customer identifier
     * @param name customer's full name
     * @param phone customer's phone number
     * @param email customer's email (must be Gmail)
     * @param age customer's age (must be 18+)
     * @param username login username
     * @param password login password
     * @param role user role (CUSTOMER or ADMIN)
     * @throws FlightBookingSystemException if validation fails
     */
    public Customer(int id, String name, String phone, String email, int age,
                    String username, String password, UserRole role) throws FlightBookingSystemException {
        if (id <= 0) {
            throw new FlightBookingSystemException("Customer ID must be positive.");
        }
        if (name == null || name.isBlank()) {
            throw new FlightBookingSystemException("Customer name cannot be empty.");
        }
        if (phone == null || phone.isBlank()) {
            throw new FlightBookingSystemException("Customer phone cannot be empty.");
        }

        validateRegistrationData(email, age, username, password);

        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.username = username;
        this.password = password; // For now store plain, we'll hash later
        this.role = role;
    }

    /**
     * Validates customer registration data against business rules.
     * @param email must be @gmail.com
     * @param age must be 18 or older
     * @param username must be at least 3 characters
     * @param password must be at least 6 characters
     * @throws FlightBookingSystemException if any validation fails
     */
    private void validateRegistrationData(String email, int age, String username,
                                         String password)
            throws FlightBookingSystemException {

        // Email validation
        if (email == null || email.isBlank()) {
            throw new FlightBookingSystemException("Email cannot be empty.");
        }
        if (!email.endsWith("@gmail.com")) {
            throw new FlightBookingSystemException("Email must be a Gmail address (@gmail.com).");
        }

        // Age validation
        if (age < 0) {
            throw new FlightBookingSystemException("Age cannot be negative.");
        }
        if (age < 18) {
            throw new FlightBookingSystemException("Customer must be at least 18 years old.");
        }

        // Username validation
        if (username == null || username.isBlank()) {
            throw new FlightBookingSystemException("Username cannot be empty.");
        }
        if (username.length() < 3) {
            throw new FlightBookingSystemException("Username must be at least 3 characters long.");
        }

        // Password validation
        if (password == null || password.isBlank()) {
            throw new FlightBookingSystemException("Password cannot be empty.");
        }
        if (password.length() < 6) {
            throw new FlightBookingSystemException("Password must be at least 6 characters long.");
        }

    }

    // Getters
    public int getId()
    {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public String getEmail() {
        return email;
    }


    /**
     * Sets customer's email with validation.
     * @param email must be a valid Gmail address
     * @throws FlightBookingSystemException if email is invalid
     */
    public void setEmail(String email) throws FlightBookingSystemException {
        if (email == null || email.isBlank()) {
            throw new FlightBookingSystemException("Email cannot be empty.");
        }
        if (!email.endsWith("@gmail.com")) {
            throw new FlightBookingSystemException("Email must be a Gmail address (@gmail.com).");
        }
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    /**
     * Sets customer's age with validation.
     * @param age must be 18 or older
     * @throws FlightBookingSystemException if age is less than 18
     */
    public void setAge(int age) throws FlightBookingSystemException {
        if (age < 18) {
            throw new FlightBookingSystemException("Customer must be at least 18 years old.");
        }
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Sets customer's username with validation.
     * @param username must be at least 3 characters long
     * @throws FlightBookingSystemException if username is invalid
     */
    public void setUsername(String username) throws FlightBookingSystemException {
        if (username == null || username.isBlank()) {
            throw new FlightBookingSystemException("Username cannot be empty.");
        }
        if (username.length() < 3) {
            throw new FlightBookingSystemException("Username must be at least 3 characters long.");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Sets customer's password with validation.
     * @param password must be at least 6 characters long
     * @throws FlightBookingSystemException if password is invalid
     */
    public void setPassword(String password) throws FlightBookingSystemException {
        if (password == null || password.isBlank()) {
            throw new FlightBookingSystemException("Password cannot be empty.");
        }
        if (password.length() < 6) {
            throw new FlightBookingSystemException("Password must be at least 6 characters long.");
        }
        this.password = password; // We'll add hashing later
    }
    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Checks if this customer has admin privileges.
     * @return true if customer's role is ADMIN
     */
    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    /**
     * Provides a short summary of customer details.
     * @return string with ID, name, phone, username, and role
     */
    public String getDetailsShort() {
        return "Customer #" + id + " Name: " + name + " Phone: " + phone +
                " Username: " + username + " Role: " + role;
    }

    /**
     * Provides a detailed view of customer information and bookings.
     * @return formatted string with all customer details and booking list
     */
    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDetailsShort());
        sb.append("Customer #").append(id);
        sb.append("\nName: ").append(name);
        sb.append("\nPhone: ").append(phone);
        sb.append("\nEmail: ").append(email);
        sb.append("\nAge: ").append(age);
        sb.append("\nUsername: ").append(username);
        sb.append("\n\nBookings:\n");

        for (Booking booking : bookings) {
            sb.append("* Booking date: ");
            sb.append(booking.getBookingDate());
            sb.append(" for ");
            sb.append(booking.getFlight().getDetailsShort());
            sb.append(" - Price Paid: Rs.").append(String.format("%.2f", booking.getPaidPrice()));
            sb.append(", Cancellation Fee: Rs.").append(String.format("%.2f", booking.getCancellationFee()));
            sb.append(" | Infant: ").append(booking.hasInfant() ? "Yes" : "No");
            sb.append(" | Vegetarian: ").append(booking.isVegetarian() ? "Yes" : "No");
            sb.append("\n");
        }

        sb.append(bookings.size());
        sb.append(" booking(s)");

        return sb.toString();
    }

    /**
     * Verifies if the provided password matches the customer's password.
     * @param inputPassword password to check
     * @return true if passwords match
     */
    public boolean verifyPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    /**
     * Adds a booking to this customer's list.
     * Prevents duplicate bookings for the same flight.
     * @param booking the booking to add
     * @throws FlightBookingSystemException if booking already exists for this flight
     */
    public void addBooking(Booking booking) throws FlightBookingSystemException {
        // Check for duplicate booking for the same flight
        for (Booking existingBooking : bookings) {
            if (existingBooking.getFlight().getId() == booking.getFlight().getId()) {
                throw new FlightBookingSystemException(
                        "Customer already has a booking for flight #" + booking.getFlight().getId()
                );
            }
        }

        bookings.add(booking);
    }

    /**
     * Cancels a booking for a specific flight.
     * @param flight the flight to cancel booking for
     * @throws FlightBookingSystemException if no booking found for the flight
     */
    public void cancelBookingForFlight(Flight flight) throws FlightBookingSystemException {
        Booking bookingToRemove = null;
        for (Booking booking : bookings) {
            if (booking.getFlight().getId() == flight.getId()) {
                bookingToRemove = booking;
                break;
            }
        }

        if (bookingToRemove == null) {
            throw new FlightBookingSystemException(
                    "No booking found for flight #" + flight.getId()
            );
        }

        bookings.remove(bookingToRemove);
    }

    /**
     * Gets the customer's deletion status.
     * @return true if customer is marked as deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Sets the customer's deletion status.
     * @param deleted true to mark as deleted, false to restore
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Soft-deletes the customer (marks as deleted without removing).
     */
    public void softDelete() {
        this.deleted = true;
        // Optionally, invalidate active sessions
        this.username = "DELETED_" + this.username;
    }

    public void restore() {
        this.deleted = false;
        // Remove the "DELETED_timestamp_" prefix
        if (this.username.startsWith("DELETED_")) {
            // Find the third underscore (DELETED_timestamp_originalUsername)
            int firstUnderscore = username.indexOf('_');
            int secondUnderscore = username.indexOf('_', firstUnderscore + 1);
            if (secondUnderscore != -1) {
                this.username = username.substring(secondUnderscore + 1);
            }
        }
    }
}