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

public class CustomerDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/customers.txt";
    
    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
    	File file = new File(RESOURCE);
        if (!file.exists()) {
            return; // No customers yet – this is NOT an error
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(SEPARATOR, -1);

                // Check format for old vs new data format
                if (parts.length == 3) {
                    // OLD FORMAT: id::name::phone
                    loadLegacyCustomer(parts, fbs, lineNumber);
                } else if (parts.length == 10) {
                    // NEW FORMAT: id::name::phone::email::age::username::password::foodPreference::hasChildUnderTwo::role
                    loadNewCustomer(parts, fbs, lineNumber);
                } else {
                    throw new FlightBookingSystemException(
                            "Invalid customer data format on line " + lineNumber + ": " + line);
                }
            }
        }
    }

    private void loadLegacyCustomer(String[] parts, FlightBookingSystem fbs, int lineNumber)
            throws FlightBookingSystemException {
        try {
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            String phone = parts[2];

            // Create customer with old constructor (which sets defaults for other fields)
            Customer customer = new Customer(id, name, phone);
            fbs.addCustomer(customer);
        } catch (NumberFormatException e) {
            throw new FlightBookingSystemException(
                    "Invalid ID format on line " + lineNumber + ": " + parts[0]);
        }
    }

    private void loadNewCustomer(String[] parts, FlightBookingSystem fbs, int lineNumber)
            throws FlightBookingSystemException {
        try {
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            String phone = parts[2];
            String email = parts[3];
            int age = Integer.parseInt(parts[4]);
            String username = parts[5];
            String password = parts[6];
            String foodPreference = parts[7];
            boolean hasChildUnderTwo = Boolean.parseBoolean(parts[8]);
            UserRole role = UserRole.valueOf(parts[9]);

            Customer customer = new Customer(id, name, phone, email, age, username,
                    password, foodPreference, hasChildUnderTwo, role);
            fbs.addCustomer(customer);
        } catch (NumberFormatException e) {
            throw new FlightBookingSystemException(
                    "Invalid number format on line " + lineNumber + ": " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new FlightBookingSystemException(
                    "Invalid enum value on line " + lineNumber + ": " + e.getMessage());
        }
    }


    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        File file = new File(RESOURCE);
        file.getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (Customer customer : fbs.getCustomers()) {
                // Store in NEW format with all fields
                pw.println(
                        customer.getId() + SEPARATOR
                                + customer.getName() + SEPARATOR
                                + customer.getPhone() + SEPARATOR
                                + customer.getEmail() + SEPARATOR
                                + customer.getAge() + SEPARATOR
                                + customer.getUsername() + SEPARATOR
                                + customer.getPassword() + SEPARATOR
                                + customer.getFoodPreference() + SEPARATOR
                                + customer.hasChildUnderTwo() + SEPARATOR
                                + customer.getRole()
                );
            }
        }
    }

}
