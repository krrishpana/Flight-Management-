package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to soft-delete a customer from the system.
 * Marks customer as deleted instead of removing them physically.
 */
public class DeleteCustomer implements Command {

    private final int customerId;

    public DeleteCustomer(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);

        if (customer.isDeleted()) {
            throw new FlightBookingSystemException("Customer #" + customerId + " is already deleted.");
        }

        // Check if customer has active bookings
        if (!customer.getBookings().isEmpty()) {
            throw new FlightBookingSystemException(
                    "Cannot delete customer #" + customerId + " because they have active bookings.");
        }

        customer.softDelete();
        System.out.println("Customer #" + customerId + " has been soft-deleted.");
    }
}