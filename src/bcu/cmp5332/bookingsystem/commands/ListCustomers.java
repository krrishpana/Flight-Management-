package bcu.cmp5332.bookingsystem.commands;

import java.util.List;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ListCustomers implements Command {

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