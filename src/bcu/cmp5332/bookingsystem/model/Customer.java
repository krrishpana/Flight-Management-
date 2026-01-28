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


    public String getDetailsShort() {
        return "Customer #" + id + " Name: " + name + " Phone: " + phone;
    }

    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDetailsShort());
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