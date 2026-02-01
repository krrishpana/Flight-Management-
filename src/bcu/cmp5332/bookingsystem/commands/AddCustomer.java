package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.UserRole;

public class AddCustomer implements Command {

    private final String name;
    private final String phone;
    private final String email;
    private final int age;
    private final String username;
    private final String password;

    // Constructor with ALL 8 parameters
    public AddCustomer(String name, String phone, String email, int age,
                       String username, String password, String foodPreference,
                       boolean hasChildUnderTwo) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.username = username;
        this.password = password;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        int nextCustomerId = flightBookingSystem.getCustomers().size() + 1;

        // Create customer with all 8 fields
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