package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

/**
 * Main system class that manages flights, customers, and user sessions.
 * Acts as the central hub for all booking system operations.
 */
public class FlightBookingSystem {

    private Customer currentUser;

    /**
     * Gets the currently logged-in user.
     * @return current customer or null if no user is logged in
     */
    public Customer getCurrentUser() {
        return currentUser;
    }
    /**
     * Sets the currently logged-in user.
     * @param user customer to set as current user
     */
    public void setCurrentUser(Customer user) {
        this.currentUser = user;
    }

    /**
     * Checks if a user is currently logged in.
     * @return true if a user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Checks if current user has admin privileges.
     * @return true if current user is an admin
     */
    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    /**
     * Gets the fixed system date for pricing calculations.
     * @return current system date
     */
    private final LocalDate systemDate = LocalDate.parse("2026-01-26");
    
    private final Map<Integer, Customer> customers = new TreeMap<>();
    private final Map<Integer, Flight> flights = new TreeMap<>();

    public LocalDate getSystemDate() {
        return systemDate;
    }

    /**
     * Gets only active, non-deleted flights.
     * @return list of flights available for booking
     */
    public List<Flight> getFlights() {
        return flights.values().stream()
                .filter(flight -> !flight.isDeleted() && !flight.hasDeparted(systemDate))
                .collect(Collectors.toList());
    }

    /**
     * Gets flights for admin view (includes departed but not deleted flights).
     * @return all non-deleted flights
     */
    public List<Flight> getFlightsForAdmin() {
        return flights.values().stream()
                .filter(flight -> !flight.isDeleted())
                .collect(Collectors.toList());
    }

    /**
     * Gets a flight by ID and validates it's available for booking by a specific customer.
     * @param id flight ID to search for
     * @param customer the customer trying to book (can be null for general availability check)
     * @return the flight with matching ID
     * @throws FlightBookingSystemException if flight not available for booking
     */
    public Flight getAvailableFlightByID(int id, Customer customer) throws FlightBookingSystemException {
        Flight flight = getFlightByID(id);

        // Check deletion status
        if (flight.isDeleted()) {
            throw new FlightBookingSystemException("Flight #" + id + " has been deleted.");
        }

        // Check departure date
        if (flight.hasDeparted(systemDate)) {
            throw new FlightBookingSystemException("Flight #" + id + " has already departed on " +
                    flight.getDepartureDate());
        }

        // Check capacity
        if (flight.isFull()) {
            throw new FlightBookingSystemException("Flight #" + id + " is fully booked.");
        }

        // Check duplicate booking if customer is provided
        if (customer != null) {
            for (Booking booking : customer.getBookings()) {
                if (booking.getFlight().getId() == id) {
                    throw new FlightBookingSystemException(
                            "You already have a booking for flight #" + id);
                }
            }
        }

        return flight;
    }

    /**
     * Gets a flight by ID for general availability (no customer check).
     * @param id flight ID to search for
     * @return the flight with matching ID
     * @throws FlightBookingSystemException if flight not available
     */
    public Flight getAvailableFlightByID(int id) throws FlightBookingSystemException {
        return getAvailableFlightByID(id, null);
    }
    /**
     * Finds a flight by ID regardless of deletion status.
     * @param id flight ID to search for
     * @return the flight with matching ID
     * @throws FlightBookingSystemException if flight not found
     */
    public Flight getFlightByID(int id) throws FlightBookingSystemException {
        if (!flights.containsKey(id)) {
            throw new FlightBookingSystemException("There is no flight with that ID.");
        }
        return flights.get(id);
    }

    /**
     * Gets only non-deleted customers.
     * @return list of active customers
     */
    public List<Customer> getCustomers() {
        return customers.values().stream()
                .filter(customer -> !customer.isDeleted())
                .collect(Collectors.toList());
    }
    /**
     * Finds a customer by their unique ID.
     * @param id customer ID to search for
     * @return the customer with matching ID
     * @throws FlightBookingSystemException if customer not found
     */
    public Customer getCustomerByID(int id) throws FlightBookingSystemException {
    	if (!customers.containsKey(id)) {
            throw new FlightBookingSystemException("There is no customer with that ID.");
        }
        return customers.get(id);
    }

    /**
     * Adds a new flight to the system.
     * Prevents duplicate flight numbers on same departure date.
     * @param flight the flight to add
     * @throws FlightBookingSystemException if flight number/date combination exists
     */
    public void addFlight(Flight flight) throws FlightBookingSystemException {
        if (flights.containsKey(flight.getId())) {
            throw new IllegalArgumentException("Duplicate flight ID.");
        }
        for (Flight existing : flights.values()) {
            if (existing.getFlightNumber().equals(flight.getFlightNumber()) 
                && existing.getDepartureDate().isEqual(flight.getDepartureDate())) {
                throw new FlightBookingSystemException("There is a flight with same "
                        + "number and departure date in the system");
            }
        }
        flights.put(flight.getId(), flight);
    }


    /**
     * Adds a new customer to the system.
     * @param customer the customer to add
     * @throws FlightBookingSystemException if customer ID already exists
     */
    public void addCustomer(Customer customer) throws FlightBookingSystemException {
        if (customers.containsKey(customer.getId())) {
            throw new FlightBookingSystemException("Duplicate customer ID.");
        }
        customers.put(customer.getId(), customer);
    }

    /**
     * Gets all customers including deleted ones.
     * @return list of all customers regardless of deletion status
     */
    public List<Customer> getAllCustomers() {
        List<Customer> out = new ArrayList<>(customers.values());
        return Collections.unmodifiableList(out);
    }

    /**
     * Gets all flights including deleted ones.
     * @return list of all flights regardless of deletion status
     */
    public List<Flight> getAllFlights() {
        List<Flight> out = new ArrayList<>(flights.values());
        return Collections.unmodifiableList(out);
    }

    /**
     * Gets flights including departed ones (for admin/history).
     * @return all non-deleted flights including departed ones
     */
    public List<Flight> getFlightsWithHistory() {
        return flights.values().stream()
                .filter(flight -> !flight.isDeleted())
                .collect(Collectors.toList());
    }

    /**
     * Soft-deletes a flight (marks as deleted).
     * @param flightId ID of flight to delete
     * @throws FlightBookingSystemException if flight not found or has active bookings
     */
    public void deleteFlight(int flightId) throws FlightBookingSystemException {
        Flight flight = getFlightByID(flightId);

        if (flight.isDeleted()) {
            throw new FlightBookingSystemException("Flight #" + flightId + " is already deleted.");
        }

        // Check if flight has active bookings
        if (!flight.getPassengers().isEmpty()) {
            throw new FlightBookingSystemException(
                    "Cannot delete flight #" + flightId + " because it has active bookings.");
        }

        flight.softDelete();
    }

    /**
     * Restores a soft-deleted flight.
     * @param id ID of flight to restore
     * @throws FlightBookingSystemException if flight not found
     */
    public void restoreFlight(int id) throws FlightBookingSystemException {
        Flight flight = getFlightByID(id);
        flight.restore();
    }

    /**
     * Soft-deletes a customer (marks as deleted).
     * @param customerId ID of customer to delete
     * @throws FlightBookingSystemException if customer not found or has active bookings
     */
    public void deleteCustomer(int customerId) throws FlightBookingSystemException {
        Customer customer = getCustomerByID(customerId);

        if (customer.isDeleted()) {
            throw new FlightBookingSystemException("Customer #" + customerId + " is already deleted.");
        }

        // Check if customer has active bookings
        if (!customer.getBookings().isEmpty()) {
            throw new FlightBookingSystemException(
                    "Cannot delete customer #" + customerId + " because they have active bookings.");
        }

        customer.softDelete();
    }

    /**
     * Restores a soft-deleted customer.
     * @param customerId ID of customer to restore
     * @throws FlightBookingSystemException if customer not found
     */
    public void restoreCustomer(int customerId) throws FlightBookingSystemException {
        Customer customer = getCustomerByID(customerId);

        if (!customer.isDeleted()) {
            throw new FlightBookingSystemException("Customer #" + customerId + " is not deleted.");
        }

        customer.restore();
    }

}
