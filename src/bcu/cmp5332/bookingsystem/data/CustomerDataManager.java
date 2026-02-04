package bcu.cmp5332.bookingsystem.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.UserRole;

/**
 * Manages loading and saving customer data to/from text files.
 * Handles the new customer format with 8 fields (including authentication data).
 */
public class CustomerDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/customers.txt";

    /**
     * Loads customer data from file and populates the flight booking system.
     * Only supports the new 8-field format (with authentication data).
     * @param fbs the flight booking system to populate
     * @throws IOException if the file cannot be read
     * @throws FlightBookingSystemException if data format is invalid
     */
    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
    	File file = new File(RESOURCE);
        if (!file.exists()) {
            return; // No customers yet – this is NOT an error
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split("::");
                if (parts.length != 8) {
                    throw new FlightBookingSystemException(
                            "Invalid customer data format: " + line);
                }
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String phone = parts[2];
                String email = parts[3];
                int age = Integer.parseInt(parts[4]);
                String username = parts[5];
                String password = parts[6];
                UserRole role = UserRole.valueOf(parts[7]);

                Customer customer = new Customer(id, name, phone, email, age, username,
                        password, role);
                fbs.addCustomer(customer);
            }
        }
    }

    /**
     * Saves all customer data from the system to file.
     * @param fbs the flight booking system containing customers to save
     * @throws IOException if the file cannot be written
     */
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        File file = new File(RESOURCE);
        file.getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (Customer customer : fbs.getCustomers()) {
                pw.println(
                        customer.getId() + SEPARATOR
                                + customer.getName() + "::"
                                + customer.getPhone() + "::"
                                + customer.getEmail() + "::"
                                + customer.getAge() + "::"
                                + customer.getUsername() + "::"
                                + customer.getPassword() + "::"
                                + customer.getRole()
                );
            }
        }
    }

}
