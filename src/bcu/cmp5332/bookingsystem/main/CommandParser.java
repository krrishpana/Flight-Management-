package bcu.cmp5332.bookingsystem.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.commands.EditBooking;
import bcu.cmp5332.bookingsystem.commands.Help;
import bcu.cmp5332.bookingsystem.commands.ListCustomers;
import bcu.cmp5332.bookingsystem.commands.ListFlights;
import bcu.cmp5332.bookingsystem.commands.LoadGUI;
import bcu.cmp5332.bookingsystem.commands.ShowCustomer;
import bcu.cmp5332.bookingsystem.commands.ShowFlight;
import bcu.cmp5332.bookingsystem.commands.LoginCommand;
import bcu.cmp5332.bookingsystem.commands.LogoutCommand;


public class CommandParser {
    
    public static Command parse(String line, AuthenticationService authService) throws IOException, FlightBookingSystemException {
        try {
            String[] parts = line.split(" ", 3);
            String cmd = parts[0];

            
            if (cmd.equals("addflight")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Flight Number: ");
                String flighNumber = reader.readLine();
                System.out.print("Origin: ");
                String origin = reader.readLine();
                System.out.print("Destination: ");
                String destination = reader.readLine();

                LocalDate departureDate = parseDateWithAttempts(reader);

                return new AddFlight(flighNumber, origin, destination, departureDate);
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

                System.out.print("Food Preference (Veg/Non-Veg): ");
                String foodPreference = reader.readLine();

                System.out.print("Traveling with child under 2? (yes/no): ");
                String childInput = reader.readLine();
                boolean hasChildUnderTwo = childInput.equalsIgnoreCase("yes");

                return new AddCustomer(name, phone, email, age, username, password,
                        foodPreference, hasChildUnderTwo);

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
                    }
            }
        } catch (NumberFormatException ex) {
            throw new FlightBookingSystemException("Invalid number format for ID.");
        }

        throw new FlightBookingSystemException("Invalid command.");
    }
    
    private static LocalDate parseDateWithAttempts(BufferedReader br, int attempts) throws IOException, FlightBookingSystemException {
        if (attempts < 1) {
            throw new IllegalArgumentException("Number of attempts should be higher that 0");
        }
        while (attempts > 0) {
            attempts--;
            System.out.print("Departure Date (\"YYYY-MM-DD\" format): ");
            try {
                LocalDate departureDate = LocalDate.parse(br.readLine());
                return departureDate;
            } catch (DateTimeParseException dtpe) {
                System.out.println("Date must be in YYYY-MM-DD format. " + attempts + " attempts remaining...");
            }
        }
        
        throw new FlightBookingSystemException("Incorrect departure date provided. Cannot create flight.");
    }
    
    private static LocalDate parseDateWithAttempts(BufferedReader br) throws IOException, FlightBookingSystemException {
        return parseDateWithAttempts(br, 3);
    }
}
