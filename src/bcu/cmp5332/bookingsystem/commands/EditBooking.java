package bcu.cmp5332.bookingsystem.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class EditBooking implements Command {

    private final int customerId;
    private final int newFlightId;

    public EditBooking(int customerId, int newFlightId) {
        this.customerId = customerId;
        this.newFlightId = newFlightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException, IOException {
        // Get the customer and new flight
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight newFlight = flightBookingSystem.getFlightByID(newFlightId);

        // Check if new flight is full
        if (newFlight.isFull()) {
            throw new FlightBookingSystemException(
                    "New flight #" + newFlightId + " is fully booked. Cannot edit booking."
            );
        }

        // Check if customer has any bookings
        if (customer.getBookings().isEmpty()) {
            throw new FlightBookingSystemException("Customer #" + customerId + " has no bookings to edit");
        }

        // Find the first booking (for simplicity, we edit the first booking)
        // In a real system, we'd ask which booking to edit
        Booking oldBooking = customer.getBookings().get(0);
        Flight oldFlight = oldBooking.getFlight();
        double cancellationFee = oldBooking.getCancellationFee();

        // Prompt for new booking date
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("New Booking Date (YYYY-MM-DD): ");
        String dateInput = reader.readLine();

        LocalDate newBookingDate;
        try {
            newBookingDate = LocalDate.parse(dateInput);
        } catch (DateTimeParseException e) {
            throw new FlightBookingSystemException("Invalid date format. Please use YYYY-MM-DD");
        }

        // Cancel old booking (charges cancellation fee)
        customer.cancelBookingForFlight(oldFlight);
        oldFlight.removePassenger(customer);

        System.out.println("Old booking cancelled. Cancellation fee: £" + String.format("%.2f", cancellationFee));

        // Calculate price for new flight
        double newPrice = newFlight.getCurrentPrice(flightBookingSystem.getSystemDate());
        double newCancellationFee = newFlight.getCancellationFee();

        // Create new booking with all parameters
        Booking newBooking = new Booking(customer, newFlight, newBookingDate,
                newPrice, newCancellationFee);
        customer.addBooking(newBooking);
        newFlight.addPassenger(customer);

        System.out.println("\nBooking updated successfully!");
        System.out.println("Changed from flight #" + oldFlight.getId() + " to flight #" + newFlightId);
        System.out.println("New booking price: £" + String.format("%.2f", newPrice));
        System.out.println("New cancellation fee: £" + String.format("%.2f", newCancellationFee));
        System.out.println("Total cost: £" + String.format("%.2f", (cancellationFee + newPrice)));
    }
}