package bcu.cmp5332.bookingsystem.model;

import java.util.ArrayList;
import java.util.List;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

public class Customer {

    private int id;
    private String name;
    private String phone;
    private final List<Booking> bookings = new ArrayList<>();

    // Constructor
    public Customer(int id, String name, String phone) throws FlightBookingSystemException {
        if (id <= 0) {
            throw new FlightBookingSystemException("Customer ID must be positive.");
        }
        if (name == null || name.isBlank()) {
            throw new FlightBookingSystemException("Customer name cannot be empty.");
        }
        if (phone == null || phone.isBlank()) {
            throw new FlightBookingSystemException("Customer phone cannot be empty.");
        }

        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    // Getters
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

    // Setters
    public void setName(String name) throws FlightBookingSystemException {
        if (name == null || name.isBlank()) {
            throw new FlightBookingSystemException("Customer name cannot be empty.");
        }
        this.name = name;
    }

    public void setPhone(String phone) throws FlightBookingSystemException {
        if (phone == null || phone.isBlank()) {
            throw new FlightBookingSystemException("Customer phone cannot be empty.");
        }
        this.phone = phone;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
}