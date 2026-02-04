package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to show detailed information about a specific customer.
 * Includes personal details and booking history.
 */
public class ShowCustomer implements Command {

    private final int customerId;

    /**
     * Creates a show customer command for specific customer.
     * @param customerId ID of customer to display
     */
    public ShowCustomer(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Executes customer details display.
     * Shows comprehensive information including bookings.
     * @param flightBookingSystem the system to operate on
     * @throws FlightBookingSystemException if customer not found
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Get the customer by ID (this may throw FlightBookingSystemException if not found)
        Customer customer = flightBookingSystem.getCustomerByID(customerId);

        // Print customer details using getDetailsLong() for comprehensive view
        System.out.println(customer.getDetailsLong());
    }
}