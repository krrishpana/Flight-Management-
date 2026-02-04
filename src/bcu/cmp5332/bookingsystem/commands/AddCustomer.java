package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.UserRole;

/**
 * Command to register a new customer in the system.
 * Validates input and creates customer with CUSTOMER role.
 */
public class AddCustomer implements Command {

    private final String name;
    private final String phone;
    private final String email;
    private final int age;
    private final String username;
    private final String password;

    /**
     * Creates an add customer command with all required details.
     * @param name customer's full name
     * @param phone customer's phone number
     * @param email customer's email (must be Gmail)
     * @param age customer's age (must be 18+)
     * @param username unique username for login
     * @param password password (at least 6 characters)
     */
    public AddCustomer(String name, String phone, String email, int age,
                       String username, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.username = username;
        this.password = password;
    }

    /**
     * Executes customer registration.
     * Validates data and adds customer to system.
     * @param flightBookingSystem the system to operate on
     * @throws FlightBookingSystemException if registration fails validation
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
            int nextCustomerId = flightBookingSystem.getCustomers().size() + 1;

            Customer customer = new Customer(nextCustomerId, name, phone, email, age,
                username, password, UserRole.CUSTOMER);
            flightBookingSystem.addCustomer(customer);

            System.out.println("Customer registered successfully:");
            System.out.println("ID: " + customer.getId() +
                    ", Name: " + customer.getName() +
                    ", Username: " + customer.getUsername() +
                    ", Email: " + customer.getEmail());
    }
}
