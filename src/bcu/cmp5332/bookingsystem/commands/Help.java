package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to display help information about available commands.
 */
public class Help implements Command {

    /**
     * Executes help display.
     * Shows list of commands and their usage.
     * @param flightBookingSystem the system to operate on (not used)
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) {
        System.out.println(Command.HELP_MESSAGE);
    }
}
