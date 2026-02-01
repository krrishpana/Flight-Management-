package bcu.cmp5332.bookingsystem.model;

import java.util.ArrayList;
import java.util.List;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

public class Customer {

    private int id;
    private String name;
    private String phone;
    private String email;
    private int age;
    private String username;
    private String password;
    private UserRole role;
    private final List<Booking> bookings = new ArrayList<>();

    public Customer(int id, String name, String phone) throws FlightBookingSystemException {
        this(id, name, phone,
                "default@gmail.com",  // email
                18,                   // age
                "user" + id,         // username
                "password123",       // password
                UserRole.CUSTOMER);  // role
    }

    // Constructor
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

//    Validation method
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
    // Getters and Setters
    public int getId() {
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

    public void setAge(int age) throws FlightBookingSystemException {
        if (age < 18) {
            throw new FlightBookingSystemException("Customer must be at least 18 years old.");
        }
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

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

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public String getDetailsShort() {
        return "Customer #" + id + " Name: " + name + " Phone: " + phone +
                " Username: " + username + " Role: " + role;
    }

    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer #").append(id);
        sb.append("\nName: ").append(name);
        sb.append("\nPhone: ").append(phone);
        sb.append("\nEmail: ").append(email);
        sb.append("\nAge: ").append(age);
        sb.append("\nUsername: ").append(username);
        sb.append("\nRole: ").append(role);
        sb.append("\n\nBookings:\n");

        for (Booking booking : bookings) {
            sb.append("* Booking date: ");
            sb.append(booking.getBookingDate());
            sb.append(" for ");
            sb.append(booking.getFlight().getDetailsShort());
            sb.append("\n");
        }

        sb.append(bookings.size());
        sb.append(" booking(s)");

        return sb.toString();
    }

    public boolean verifyPassword(String inputPassword) {
        // For now, simple string comparison
        // Later we'll implement password hashing
        return this.password.equals(inputPassword);
    }

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
}