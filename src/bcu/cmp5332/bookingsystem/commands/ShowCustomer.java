package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ShowCustomer implements Command {

    private final int customerId;

    public ShowCustomer(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Get the customer by ID (this may throw FlightBookingSystemException if not found)
        Customer customer = flightBookingSystem.getCustomerByID(customerId);

        // Print customer details using getDetailsLong() for comprehensive view
        System.out.println(customer.getDetailsLong());
    }
}