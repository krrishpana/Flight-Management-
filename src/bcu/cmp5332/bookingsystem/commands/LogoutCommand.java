package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.AuthenticationService;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to log out the current user.
 * Clears the current user session.
 */
public class LogoutCommand implements Command {

    private final AuthenticationService authService;

    /**
     * Creates a logout command.
     * @param authService authentication service to use
     */
    public LogoutCommand(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * Executes logout process.
     * Clears current user and confirms logout.
     * @param fbs the system to operate on (not used)
     */
    @Override
    public void execute(FlightBookingSystem fbs) {
        authService.logout();
        System.out.println("Logged out successfully.");
    }
}