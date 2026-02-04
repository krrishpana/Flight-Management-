package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    public List<Flight> getFlights() {
        List<Flight> out = new ArrayList<>(flights.values());
        return Collections.unmodifiableList(out);
    }

    /**
     * Finds a flight by its unique ID.
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
     * Gets an unmodifiable list of all customers.
     * @return list of customers
     */
    public List<Customer> getCustomers() {
        List<Customer> out = new ArrayList<>(customers.values());
        return Collections.unmodifiableList(out);
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
}
