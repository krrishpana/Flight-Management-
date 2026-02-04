package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Placeholder command to show current user information.
 * Not fully implemented in current version.
 */
public class CurrentUserCommand implements Command {

    /**
     * Executes current user display (placeholder).
     * @param flightBookingSystem the system to operate on
     * @throws FlightBookingSystemException not thrown in current implementation
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        System.out.println("Show current user functionality to be implemented");
    }
}