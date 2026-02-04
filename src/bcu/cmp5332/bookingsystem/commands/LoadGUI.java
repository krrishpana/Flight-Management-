package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.gui.RoleSelectionWindow;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to launch the graphical user interface version of the system.
 */
public class LoadGUI implements Command {

    /**
     * Executes GUI launch.
     * Creates and displays the role selection window.
     * @param flightBookingSystem the system to operate on
     * @throws FlightBookingSystemException if GUI cannot be launched
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        new RoleSelectionWindow(flightBookingSystem);
    }
}