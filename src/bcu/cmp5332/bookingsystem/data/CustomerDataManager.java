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
 * Handles the new customer format with 9 fields (including authentication data and deleted flag).
 */
public class CustomerDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/customers.txt";

    /**
     * Loads customer data from file and populates the flight booking system.
     * Expects exactly 9 fields per line including the deleted flag.
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
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split("::", -1); // Use -1 to keep empty fields

                // NEW FORMAT: Expect exactly 9 fields
                if (parts.length != 9) {
                    throw new FlightBookingSystemException(
                            "Invalid customer data format on line " + lineNumber +
                                    ". Expected 9 fields, found " + parts.length +
                                    ". Please delete old data file and restart.");
                }

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String phone = parts[2];
                String email = parts[3];
                int age = Integer.parseInt(parts[4]);
                String username = parts[5];
                String password = parts[6];
                UserRole role = UserRole.valueOf(parts[7]);
                boolean deleted = Boolean.parseBoolean(parts[8]);

                Customer customer = new Customer(id, name, phone, email, age,
                        username, password, role);

                // Set deletion status
                customer.setDeleted(deleted);

                fbs.addCustomer(customer);
            }
        } catch (NumberFormatException e) {
            throw new FlightBookingSystemException(
                    "Invalid number format in customer data: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new FlightBookingSystemException(
                    "Invalid enum value in customer data: " + e.getMessage());
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
            for (Customer customer : fbs.getAllCustomers()) {  // Need to add getAllCustomers() method
                pw.println(
                        customer.getId() + SEPARATOR
                                + customer.getName() + SEPARATOR
                                + customer.getPhone() + SEPARATOR
                                + customer.getEmail() + SEPARATOR
                                + customer.getAge() + SEPARATOR
                                + customer.getUsername() + SEPARATOR
                                + customer.getPassword() + SEPARATOR
                                + customer.getRole() + SEPARATOR
                                + customer.isDeleted()  // 9th field: deletion status
                );
            }
        }
    }
}