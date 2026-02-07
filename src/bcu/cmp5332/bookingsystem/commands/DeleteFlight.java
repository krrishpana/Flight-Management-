package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to soft-delete a flight from the system.
 * Marks flight as deleted instead of removing it physically.
 */
public class DeleteFlight implements Command {

    private final int flightId;

    public DeleteFlight(int flightId) {
        this.flightId = flightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Flight flight = flightBookingSystem.getFlightByID(flightId);

        if (flight.isDeleted()) {
            throw new FlightBookingSystemException("Flight #" + flightId + " is already deleted.");
        }

        // Check if flight has active bookings
        if (!flight.getPassengers().isEmpty()) {
            throw new FlightBookingSystemException(
                    "Cannot delete flight #" + flightId + " because it has active bookings.");
        }

        flight.softDelete();
        System.out.println("Flight #" + flightId + " has been soft-deleted.");
    }
}