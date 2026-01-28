package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class AddBooking implements Command {

    private final int customerId;
    private final int flightId;

    public AddBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Get the customer and flight by ID (may throw FlightBookingSystemException)
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight flight = flightBookingSystem.getFlightByID(flightId);

        // Create a new Booking with current system date
        Booking booking = new Booking(customer, flight, flightBookingSystem.getSystemDate());

        // Add booking to customer's list of bookings
        customer.addBooking(booking);

        // Add customer to flight's passengers
        flight.addPassenger(customer);

        // Display success message
        System.out.println("Booking issued successfully to customer #" + customerId +
                " for flight #" + flightId);
    }
}