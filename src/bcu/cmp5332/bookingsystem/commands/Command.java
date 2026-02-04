package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Interface for all commands in the flight booking system.
 * Defines the contract for command execution and provides help text.
 */
import java.io.IOException;

public interface Command {

    /**
     * Help message listing all available commands and their usage.
     */
    public static final String HELP_MESSAGE = "Commands:\n"
        + "\tlistflights                               print all flights\n"
        + "\tlistcustomers                             print all customers\n"
        + "\taddflight                                 add a new flight\n"
        + "\taddcustomer                               add a new customer\n"
        + "\tshowflight [flight id]                    show flight details\n"
        + "\tshowcustomer [customer id]                show customer details\n"
        + "\taddbooking [customer id] [flight id]      add a new booking\n"
        + "\tcancelbooking [customer id] [flight id]   cancel a booking\n"
        + "\tundocancel [customer id] [flight id]      undo a cancellation (24h limit)\n"
        + "\teditbooking [customer id] [flight id]      update a booking\n"
        + "\tloadgui                                   loads the GUI version of the app\n"
        + "\thelp                                      prints this help message\n"
        + "\texit                                      exits the program";


    /**
     * Executes this command on the flight booking system.
     * @param flightBookingSystem the system to operate on
     * @throws FlightBookingSystemException if command execution fails
     * @throws IOException if there's an I/O error during execution
     */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException, IOException;
    
}
