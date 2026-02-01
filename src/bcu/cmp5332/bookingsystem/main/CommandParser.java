package bcu.cmp5332.bookingsystem.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import bcu.cmp5332.bookingsystem.commands.*;

public class CommandParser {
    
    public static Command parse(String line) throws IOException, FlightBookingSystemException {
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

                return new AddCustomer(name, phone);
            } else if (cmd.equals("loadgui")) {
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
                } else if (cmd.equals("undocancel")) {  // ADD THIS LINE
                    return new UndoCancelBooking(id1, id2);  // ADD THIS LINE
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
        LocalDate systemDate = LocalDate.now();
        while (attempts > 0) {
            attempts--;
            System.out.print("Departure Date (\"YYYY-MM-DD\" format): ");
            try {
                LocalDate departureDate = LocalDate.parse(br.readLine());
                if (departureDate.isBefore(systemDate)) {
                    System.out.println("Departure date cannot be in the past. " +
                            attempts + " attempts remaining...");
                    continue; // Don't count this as a failed attempt for format
                }
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
