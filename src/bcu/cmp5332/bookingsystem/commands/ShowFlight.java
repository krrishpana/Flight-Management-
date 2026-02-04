package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to show detailed information about a specific flight.
 * Includes passenger list and pricing details.
 */
public class ShowFlight implements Command {

    private final int flightId;

    /**
     * Creates a show flight command for specific flight.
     * @param flightId ID of flight to display
     */
    public ShowFlight(int flightId) {
        this.flightId = flightId;
    }

    /**
     * Executes flight details display.
     * Shows comprehensive information about the flight.
     * @param flightBookingSystem the system to operate on
     * @throws FlightBookingSystemException if flight not found
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Get the flight by ID (this may throw FlightBookingSystemException if not found)
        Flight flight = flightBookingSystem.getFlightByID(flightId);

        // Print flight details using getDetailsLong() for comprehensive view
        System.out.println(flight.getDetailsLong());

        System.out.println("\n" + flight.getPricingSummary(flightBookingSystem.getSystemDate()));
    }
}