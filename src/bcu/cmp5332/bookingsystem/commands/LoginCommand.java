package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.AuthenticationService;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to log in a user with username and password.
 * Sets current user in the system if authentication succeeds.
 */
public class LoginCommand implements Command {

    private final String username;
    private final String password;
    private final AuthenticationService authService;

    /**
     * Creates a login command with credentials.
     * @param username username to authenticate
     * @param password password to verify
     * @param authService authentication service to use
     */
    public LoginCommand(String username, String password, AuthenticationService authService) {
        this.username = username;
        this.password = password;
        this.authService = authService;
    }

    /**
     * Executes login process.
     * Attempts authentication and sets current user if successful.
     * @param fbs the system to operate on
     * @throws FlightBookingSystemException if authentication fails
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        if (authService.login(username, password)) {
            System.out.println("Login successful! Welcome " + authService.getCurrentUser().getName());
        } else {
            throw new FlightBookingSystemException("Invalid username or password.");
        }
    }
}