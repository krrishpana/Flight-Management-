package bcu.cmp5332.bookingsystem.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class BookingDataManager implements DataManager {

    public final String RESOURCE = "./resources/data/bookings.txt";

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        File file = new File(RESOURCE);
        if (!file.exists()) {
            return; // No bookings yet
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split("::");

                // NEW: Expect exactly 7 fields (no backward compatibility)
                if (parts.length != 7) {
                    throw new FlightBookingSystemException(
                            "Invalid booking data format. Expected 7 fields, found " +
                                    parts.length + ": " + line
                    );
                }

                int customerId = Integer.parseInt(parts[0]);
                int flightId = Integer.parseInt(parts[1]);
                LocalDate bookingDate = LocalDate.parse(parts[2]);
                double paidPrice = Double.parseDouble(parts[3]);
                double cancellationFee = Double.parseDouble(parts[4]);
                boolean hasInfant = Boolean.parseBoolean(parts[5]);
                boolean isVegetarian = Boolean.parseBoolean(parts[6]);

                Customer customer = fbs.getCustomerByID(customerId);
                Flight flight = fbs.getFlightByID(flightId);

                // Use new constructor with all 7 parameters
                Booking booking = new Booking(customer, flight, bookingDate,
                        paidPrice, cancellationFee,
                        hasInfant, isVegetarian);

                customer.addBooking(booking);
                flight.addPassenger(customer);
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        File file = new File(RESOURCE);
        file.getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {

            for (Customer customer : fbs.getCustomers()) {
                for (Booking booking : customer.getBookings()) {
                    // Write all 7 fields
                    pw.println(
                            customer.getId() + "::" +
                                    booking.getFlight().getId() + "::" +
                                    booking.getBookingDate() + "::" +
                                    booking.getPaidPrice() + "::" +
                                    booking.getCancellationFee() + "::" +
                                    booking.hasInfant() + "::" +
                                    booking.isVegetarian()
                    );
                }
            }
        }
    }
}