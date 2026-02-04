package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

/**
 * Command to create a new booking for a customer on a specific flight.
 * Collects passenger preferences and calculates current price.
 */
public class AddBooking implements Command {

    private final int customerId;
    private final int flightId;

    /**
     * Creates an add booking command for a specific customer and flight.
     * @param customerId ID of the customer making the booking
     * @param flightId ID of the flight to book
     */
    public AddBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
    }

    /**
     * Executes the booking creation process.
     * Checks availability, collects preferences, and creates booking.
     * @param flightBookingSystem the system to operate on
     * @throws FlightBookingSystemException if booking cannot be created
     * @throws IOException if there's an error reading user input
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException, IOException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight flight = flightBookingSystem.getFlightByID(flightId);

        if (flight.isFull()) {
            throw new FlightBookingSystemException("Flight #" + flightId + " is fully booked.");
        }

        LocalDate systemDate = flightBookingSystem.getSystemDate();
        if (flight.getDepartureDate().isBefore(systemDate)) {
            throw new FlightBookingSystemException(
                    "Cannot book a flight that has already departed. " +
                            "Flight departed on: " + flight.getDepartureDate()
            );
        }
        double currentPrice = flight.getCurrentPrice(flightBookingSystem.getSystemDate());
        double flightCancellationFee = flight.getCancellationFee();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Travelling with infant under 2? (y/n): ");
        String infantResponse = reader.readLine().trim().toLowerCase();
        boolean hasInfant = infantResponse.equals("y") || infantResponse.equals("yes");

        System.out.print("Vegetarian meal? (y/n): ");
        String vegResponse = reader.readLine().trim().toLowerCase();
        boolean isVegetarian = vegResponse.equals("y") || vegResponse.equals("yes");

        // Get simplified pricing summary
        String pricingSummary = flight.getPricingSummary(flightBookingSystem.getSystemDate());

        Booking booking = new Booking(customer, flight, flightBookingSystem.getSystemDate(),
                currentPrice, flightCancellationFee, hasInfant, isVegetarian);

        customer.addBooking(booking);
        flight.addPassenger(customer);

        // Show minimal pricing info
        System.out.println("\n" + pricingSummary);
        System.out.println("Cancellation Fee: £" + String.format("%.2f", flightCancellationFee));
        System.out.println("Infant: " + (hasInfant ? "Yes" : "No"));
        System.out.println("Vegetarian: " + (isVegetarian ? "Yes" : "No"));
        System.out.println("\n Booking confirmed for customer #" + customerId);
    }
}