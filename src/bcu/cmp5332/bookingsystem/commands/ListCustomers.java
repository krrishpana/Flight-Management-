package bcu.cmp5332.bookingsystem.commands;

import java.util.List;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to list all customers in the system.
 * Shows brief details of each customer.
 */
public class ListCustomers implements Command {

    /**
     * Executes customer listing.
     * Displays short details of all customers.
     * @param flightBookingSystem the system to operate on
     * @throws FlightBookingSystemException if listing fails
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Get the list of customers from the system
        List<Customer> customers = flightBookingSystem.getCustomers();

        // Iterate and print each customer using getDetailsShort()
        for (Customer customer : customers) {
            System.out.println(customer.getDetailsShort());
        }

        // Print the total number of customers
        System.out.println(customers.size() + " customer(s)");
    }
}