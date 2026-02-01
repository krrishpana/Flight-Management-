package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ShowFlight implements Command {

    private final int flightId;

    public ShowFlight(int flightId) {
        this.flightId = flightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Get the flight by ID (this may throw FlightBookingSystemException if not found)
        Flight flight = flightBookingSystem.getFlightByID(flightId);

        // Print flight details using getDetailsLong() for comprehensive view
        System.out.println(flight.getDetailsLong());

        System.out.println("\n" + flight.getPricingSummary(flightBookingSystem.getSystemDate()));
    }
}