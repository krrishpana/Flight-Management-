package bcu.cmp5332.bookingsystem.commands;

import java.time.LocalDate;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to add a new flight to the system.
 * Validates flight details and ensures unique flight number/date combination.
 */
public class AddFlight implements  Command {

    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final double basePrice;
    private final double cancellationFee;

    private final int capacity;

    /**
     * Creates an add flight command with all flight details.
     * @param flightNumber unique flight identifier (e.g., BA123)
     * @param origin departure location
     * @param destination arrival location
     * @param departureDate date of departure (cannot be in past)
     * @param basePrice standard ticket price
     * @param cancellationFee fee for cancelling booking
     * @param capacity maximum number of passengers
     */
    public AddFlight(String flightNumber, String origin, String destination, LocalDate departureDate, double basePrice, double cancellationFee, int capacity) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.basePrice = basePrice;
        this.cancellationFee = cancellationFee;
        this.capacity = capacity;
    }

    /**
     * Executes flight creation.
     * Generates new ID and adds flight to system.
     * @param flightBookingSystem the system to operate on
     * @throws FlightBookingSystemException if flight creation fails
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        int maxId = 0;
        if (flightBookingSystem.getFlights().size() > 0) {
            int lastIndex = flightBookingSystem.getFlights().size() - 1;
            maxId = flightBookingSystem.getFlights().get(lastIndex).getId();
        }

        LocalDate systemDate = flightBookingSystem.getSystemDate();
        if (departureDate.isBefore(systemDate)) {
            throw new FlightBookingSystemException(
                    "Cannot create flight with past departure date. " +
                            "Departure: " + departureDate + ", System Date: " + systemDate
            );
        }

        Flight flight = new Flight(++maxId, flightNumber, origin, destination,
                departureDate, capacity, basePrice, cancellationFee);
        flightBookingSystem.addFlight(flight);
        System.out.println("Flight #" + flight.getId() + " added.");
        System.out.println("Capacity: " + capacity + ", Base Price: Rs." + basePrice +
                ", Cancellation Fee: Rs." + cancellationFee);
    }
}
