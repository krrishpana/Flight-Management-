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

/**
 * Manages loading and saving flight data to/from text files.
 * Handles the flights.txt file with 9 fields per line (including deleted flag).
 */
public class FlightDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/flights.txt";

    /**
     * Loads flight data from file and populates the flight booking system.
     * Expects exactly 9 fields per line including the deleted flag.
     * @param fbs the flight booking system to populate
     * @throws IOException if the file cannot be read
     * @throws FlightBookingSystemException if data format is invalid
     */
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
                    // NEW FORMAT: Expect exactly 9 fields
                    if (properties.length != 9) {
                        throw new FlightBookingSystemException(
                                "Invalid flight data format on line " + line_idx +
                                        ". Expected 9 fields, found " + properties.length +
                                        ". Please delete old data file and restart.");
                    }

                    // Parse all 9 required fields
                    int id = Integer.parseInt(properties[0]);
                    String flightNumber = properties[1];
                    String origin = properties[2];
                    String destination = properties[3];
                    LocalDate departureDate = LocalDate.parse(properties[4]);
                    int capacity = Integer.parseInt(properties[5]);
                    double basePrice = Double.parseDouble(properties[6]);
                    double cancellationFee = Double.parseDouble(properties[7]);
                    boolean deleted = Boolean.parseBoolean(properties[8]);

                    // Create Flight using constructor
                    Flight flight = new Flight(id, flightNumber, origin, destination,
                            departureDate, capacity, basePrice, cancellationFee);

                    // Set deletion status
                    flight.setDeleted(deleted);

                    fbs.addFlight(flight);

                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException(
                            "Unable to parse number on line " + line_idx + ": " + ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    throw new FlightBookingSystemException(
                            "Invalid data on line " + line_idx + ": " + ex.getMessage());
                }
                line_idx++;
            }
        }
    }

    /**
     * Saves all flight data from the system to file.
     * Writes 9 fields per line including the deleted flag.
     * @param fbs the flight booking system containing flights to save
     * @throws IOException if the file cannot be written
     */
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        File file = new File(RESOURCE);
        file.getParentFile().mkdirs();

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (Flight flight : fbs.getAllFlights()) {  // Use getAllFlights() to include deleted ones
                // Write all 9 fields
                out.print(flight.getId() + SEPARATOR);
                out.print(flight.getFlightNumber() + SEPARATOR);
                out.print(flight.getOrigin() + SEPARATOR);
                out.print(flight.getDestination() + SEPARATOR);
                out.print(flight.getDepartureDate() + SEPARATOR);
                out.print(flight.getCapacity() + SEPARATOR);
                out.print(flight.getBasePrice() + SEPARATOR);
                out.print(flight.getCancellationFee() + SEPARATOR);
                out.print(flight.isDeleted());  // 9th field: deletion status
                out.println();
            }
        }
    }
}