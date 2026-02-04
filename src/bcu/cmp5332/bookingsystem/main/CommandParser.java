package bcu.cmp5332.bookingsystem.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import bcu.cmp5332.bookingsystem.commands.*;

/**
 * Parses user input commands and creates corresponding command objects.
 * Handles interactive input for commands that require additional information.
 */
public class CommandParser {

    /**
     * Parses a command line and creates the appropriate command object.
     * @param line the command line entered by user
     * @param authService authentication service for login/logout commands
     * @return Command object corresponding to the input
     * @throws IOException if there's an error reading additional input
     * @throws FlightBookingSystemException if command is invalid or parsing fails
     */
    public static Command parse(String line, AuthenticationService authService) throws IOException, FlightBookingSystemException {
        try {
            String[] parts = line.split(" ", 3);
            String cmd = parts[0];


            if (cmd.equals("addflight")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Flight Number: ");
                String flightNumber = reader.readLine();
                System.out.print("Origin: ");
                String origin = reader.readLine();
                System.out.print("Destination: ");
                String destination = reader.readLine();

                LocalDate departureDate = parseDateWithAttempts(reader);

                System.out.print("Capacity: ");
                int capacity;
                while (true) {
                    try {
                        capacity = Integer.parseInt(reader.readLine());
                        if (capacity <= 0) {
                            System.out.print("Capacity must be positive. Please enter again: ");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid number. Please enter capacity as integer: ");
                    }
                }

                System.out.print("Base Price (£): ");
                double basePrice;
                while (true) {
                    try {
                        basePrice = Double.parseDouble(reader.readLine());
                        if (basePrice < 0) {
                            System.out.print("Base price cannot be negative. Please enter again: ");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid amount. Please enter base price as number: ");
                    }
                }

                System.out.print("Cancellation Fee (£): ");
                double cancellationFee;
                while (true) {
                    try {
                        cancellationFee = Double.parseDouble(reader.readLine());
                        if (cancellationFee < 0) {
                            System.out.print("Cancellation fee cannot be negative. Please enter again: ");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid amount. Please enter cancellation fee as number: ");
                    }
                }

                return new AddFlight(flightNumber, origin, destination, departureDate,
                        basePrice, cancellationFee, capacity);
            } else if (cmd.equals("addcustomer")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                System.out.print("Name: ");
                String name = reader.readLine();

                System.out.print("Phone: ");
                String phone = reader.readLine();

                System.out.print("Email (must be @gmail.com): ");
                String email = reader.readLine();

                System.out.print("Age (must be 18+): ");
                int age = Integer.parseInt(reader.readLine());

                System.out.print("Username: ");
                String username = reader.readLine();

                System.out.print("Password (min 6 chars): ");
                String password = reader.readLine();


                return new AddCustomer(name, phone, email, age, username, password
                );
            }
            else if (cmd.equals("login")) {
                {
                    BufferedReader readerLogin = new BufferedReader(new InputStreamReader(System.in));

                    System.out.print("Username: ");
                    String loginUsername = readerLogin.readLine();

                    System.out.print("Password: ");
                    String loginPassword = readerLogin.readLine();

                    return new LoginCommand(loginUsername, loginPassword, authService);
                }
            }
            else if (cmd.equals("logout")) {
                return new LogoutCommand(authService);
            }
            else if (cmd.equals("loadgui")) {
                return new LoadGUI();
            } else if (parts.length == 1) {
                if (line.equals("listflights")) {
                    return new ListFlights();
                } else if (line.equals("listcustomers")) {
                    return new ListCustomers();
                } else if (line.equals("help")) {
                    return new Help();
                }
            } else if (parts.length == 2) {
                int id = Integer.parseInt(parts[1]);

                if (cmd.equals("showflight")) {
                    return new ShowFlight(id);
                } else if (cmd.equals("showcustomer")) {
                    return new ShowCustomer(id);
                }
            } else if (parts.length == 3) {
                int id1 = Integer.parseInt(parts[1]);
                int id2 = Integer.parseInt(parts[2]);

                if (cmd.equals("addbooking")) {
                    return new AddBooking(id1, id2);
                } else if (cmd.equals("editbooking")) {
                    
                } else if (cmd.equals("cancelbooking")) {
                    return new CancelBooking(id1, id2);
                } else if (cmd.equals("undocancel")) {
                    return new UndoCancelBooking(id1, id2);
                }
            }
        } catch (NumberFormatException ex) {
            throw new FlightBookingSystemException("Invalid number format for ID.");
        }

        throw new FlightBookingSystemException("Invalid command.");
    }

    /**
     * Attempts to parse a date with multiple retries for invalid input.
     * @param br BufferedReader for user input
     * @param attempts number of attempts allowed
     * @return parsed LocalDate object
     * @throws IOException if reading input fails
     * @throws FlightBookingSystemException if all attempts fail
     */
    private static LocalDate parseDateWithAttempts(BufferedReader br, int attempts) throws IOException, FlightBookingSystemException {
        if (attempts < 1) {
            throw new IllegalArgumentException("Number of attempts should be higher that 0");
        }
        LocalDate systemDate = LocalDate.now();
        while (attempts > 0) {
            attempts--;
            System.out.print("Departure Date (\"YYYY-MM-DD\" format): ");
            try {
                LocalDate departureDate = LocalDate.parse(br.readLine());
                if (departureDate.isBefore(systemDate)) {
                    System.out.println("Departure date cannot be in the past. " +
                            attempts + " attempts remaining...");
                    continue;
                }
                return departureDate;
            } catch (DateTimeParseException dtpe) {
                System.out.println("Date must be in YYYY-MM-DD format. " + attempts + " attempts remaining...");
            }
        }
        
        throw new FlightBookingSystemException("Incorrect departure date provided. Cannot create flight.");
    }

    /**
     * Attempts to parse a date with 3 retries for invalid input.
     * @param br BufferedReader for user input
     * @return parsed LocalDate object
     * @throws IOException if reading input fails
     * @throws FlightBookingSystemException if all attempts fail
     */
    private static LocalDate parseDateWithAttempts(BufferedReader br) throws IOException, FlightBookingSystemException {
        return parseDateWithAttempts(br, 3);
    }
}
