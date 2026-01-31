package bcu.cmp5332.bookingsystem.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class Main {

    public static void main(String[] args) throws IOException, FlightBookingSystemException {
        
        FlightBookingSystem fbs = FlightBookingSystemData.load();

        AuthenticationService authService = new AuthenticationService(fbs);
        authService.registerDefaultAdmin();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Flight Booking System");
        System.out.println("Enter 'help' to see a list of available commands.");
        while (true) {
            // Show prompt with username if logged in
            if (fbs.isLoggedIn()) {
                System.out.print(fbs.getCurrentUser().getUsername() + "> ");
            } else {
                System.out.print("> ");
            }

            String line = br.readLine();
            if (line.equals("exit")) break;

            try {
                Command command = CommandParser.parse(line, authService);
                command.execute(fbs);
            } catch (FlightBookingSystemException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        FlightBookingSystemData.store(fbs);
        System.exit(0);
    }
}
