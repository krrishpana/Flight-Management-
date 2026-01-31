package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.AuthenticationService;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class LoginCommand implements Command {

    private final String username;
    private final String password;
    private final AuthenticationService authService;

    public LoginCommand(String username, String password, AuthenticationService authService) {
        this.username = username;
        this.password = password;
        this.authService = authService;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        if (authService.login(username, password)) {
            System.out.println("Login successful! Welcome " + authService.getCurrentUser().getName());
        } else {
            throw new FlightBookingSystemException("Invalid username or password.");
        }
    }
}

