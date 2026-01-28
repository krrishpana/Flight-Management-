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

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split("::");
                if (parts.length != 3) {
                    throw new FlightBookingSystemException(
                            "Invalid customer data format: " + line);
                }

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String phone = parts[2];

                Customer customer = new Customer(id, name, phone);
                fbs.addCustomer(customer);
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        File file = new File(RESOURCE);
        file.getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (Customer customer : fbs.getCustomers()) {
                pw.println(
                        customer.getId() + "::"
                      + customer.getName() + "::"
                      + customer.getPhone()
                );
            }
        }
    }

}
