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

    private final int bookingId;
    private final int newFlightId;

    public EditBooking(int bookingId, int newFlightId) {
        this.bookingId = bookingId;
        this.newFlightId = newFlightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException, IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("New Booking Date (YYYY-MM-DD): ");
        String dateInput = reader.readLine();

        LocalDate newBookingDate;
        try {
            newBookingDate = LocalDate.parse(dateInput);
        } catch (DateTimeParseException e) {
            throw new FlightBookingSystemException("Invalid date format. Please use YYYY-MM-DD");
        }

        // Get the customer (using bookingId as customerId)
        Customer customer = flightBookingSystem.getCustomerByID(bookingId);
        Flight newFlight = flightBookingSystem.getFlightByID(newFlightId);

        // Find the old booking (we need to know which flight to cancel)
        // Since we don't have booking IDs, we'll cancel all bookings and create new one
        // In a real system, we would identify the specific booking

        // For this implementation, we'll cancel the first booking and create a new one
        if (customer.getBookings().isEmpty()) {
            throw new FlightBookingSystemException("Customer has no bookings to edit");
        }

        // Cancel old booking (first one)
        Flight oldFlight = customer.getBookings().get(0).getFlight();
        customer.cancelBookingForFlight(oldFlight);
        oldFlight.removePassenger(customer);

        // Create new booking
        Booking newBooking = new Booking(customer, newFlight, newBookingDate);
        customer.addBooking(newBooking);
        newFlight.addPassenger(customer);

        System.out.println("Booking updated successfully for customer #" + bookingId);
        System.out.println("Changed from flight #" + oldFlight.getId() + " to flight #" + newFlightId);
        System.out.println("New booking date: " + newBookingDate);
    }
}