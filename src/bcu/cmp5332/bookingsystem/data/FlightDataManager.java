package bcu.cmp5332.bookingsystem.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class FlightDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/flights.txt";

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        File file = new File(RESOURCE);

        // If file doesn't exist, create empty file and return
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return;
        }

        try (Scanner sc = new Scanner(new File(RESOURCE))) {
            int line_idx = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    line_idx++;
                    continue;
                }

                String[] properties = line.split(SEPARATOR, -1);

                try {
                    // New format: Expect exactly 8 fields (id, flightNumber, origin, destination, departureDate, capacity, basePrice, cancellationFee)
                    if (properties.length < 8) {
                        throw new FlightBookingSystemException(
                                "Invalid flight data format on line " + line_idx +
                                        ". Expected 8 fields, found " + properties.length);
                    }

                    // Parse all required fields
                    int id = Integer.parseInt(properties[0]);
                    String flightNumber = properties[1];
                    String origin = properties[2];
                    String destination = properties[3];
                    LocalDate departureDate = LocalDate.parse(properties[4]);
                    int capacity = Integer.parseInt(properties[5]);
                    double basePrice = Double.parseDouble(properties[6]);
                    double cancellationFee = Double.parseDouble(properties[7]);

                    // Create Flight using new constructor
                    Flight flight = new Flight(id, flightNumber, origin, destination,
                            departureDate, capacity, basePrice, cancellationFee);

                    fbs.addFlight(flight);

                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException(
                            "Unable to parse number on line " + line_idx + ": " + ex.getMessage());
                } catch (ArrayIndexOutOfBoundsException ex) {
                    throw new FlightBookingSystemException(
                            "Missing required flight fields on line " + line_idx);
                }
                line_idx++;
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        File file = new File(RESOURCE);
        file.getParentFile().mkdirs();

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (Flight flight : fbs.getFlights()) {
                // Write all 8 fields
                out.print(flight.getId() + SEPARATOR);
                out.print(flight.getFlightNumber() + SEPARATOR);
                out.print(flight.getOrigin() + SEPARATOR);
                out.print(flight.getDestination() + SEPARATOR);
                out.print(flight.getDepartureDate() + SEPARATOR);
                out.print(flight.getCapacity() + SEPARATOR);
                out.print(flight.getBasePrice() + SEPARATOR);
                out.print(flight.getCancellationFee() + SEPARATOR);
                out.println();
            }
        }
    }
}