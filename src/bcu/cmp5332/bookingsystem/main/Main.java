package bcu.cmp5332.bookingsystem.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Main entry point for the Flight Booking System application.
 * Handles command-line interface and system initialization.
 */
public class Main {

    /**
     * Main method that starts the flight booking system.
     * Loads data, processes user commands, and saves data on exit.
     * @param args command line arguments (not used)
     * @throws IOException if data loading/saving fails
     * @throws FlightBookingSystemException if system initialization fails
     */
    public static void main(String[] args) throws IOException, FlightBookingSystemException {

        FlightBookingSystem fbs = FlightBookingSystemData.load();

        AuthenticationService authService = new AuthenticationService(fbs);
        authService.registerDefaultAdmin();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Flight Booking System");
        System.out.println("Enter 'help' to see a list of available commands.");

        try {
            while (true) {
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
        } finally {
            System.out.println("Saving data before exit...");
            FlightBookingSystemData.store(fbs);
            System.out.println("Data saved successfully.");
        }

        System.exit(0);
    }
}