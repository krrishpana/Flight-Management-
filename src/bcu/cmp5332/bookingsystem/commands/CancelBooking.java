package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.data.CancellationDataManager;
import java.io.IOException;
import java.time.LocalDateTime;

public class CancelBooking implements Command {

    private final int customerId;
    private final int flightId;
    private final CancellationDataManager cancellationDM;

    public CancelBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.cancellationDM = new CancellationDataManager();
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        try {
            Customer customer = flightBookingSystem.getCustomerByID(customerId);
            Flight flight = flightBookingSystem.getFlightByID(flightId);

            // Find the booking
            Booking bookingToCancel = null;
            for (Booking booking : customer.getBookings()) {
                if (booking.getFlight().getId() == flightId) {
                    bookingToCancel = booking;
                    break;
                }
            }

            if (bookingToCancel == null) {
                throw new FlightBookingSystemException(
                        "No booking found for customer #" + customerId + " on flight #" + flightId
                );
            }

            // Log cancellation before removing
            cancellationDM.logCancellation(bookingToCancel, LocalDateTime.now());

            // Remove booking from customer
            customer.cancelBookingForFlight(flight);

            // Remove customer from flight
            flight.removePassenger(customer);

            // Display success with undo info
            System.out.println("\n" + "".repeat(20));
            System.out.println("BOOKING CANCELLED");
            System.out.println("=" .repeat(40));
            System.out.println("Customer: " + customer.getName());
            System.out.println("Flight: " + flight.getFlightNumber());
            System.out.println("Route: " + flight.getOrigin() + " -> " + flight.getDestination());
            System.out.println("\n Cancellation Details:");
            System.out.println("  - Cancellation Fee: £" + String.format("%.2f", bookingToCancel.getCancellationFee()));
            System.out.println("  - Cancellation Time: " + LocalDateTime.now());
            System.out.println("  - Undo Available For: 24 hours");
            System.out.println("\n To undo cancellation:");
            System.out.println("  Command: undocancel " + customerId + " " + flightId);
            System.out.println("\n" + "".repeat(20));

        } catch (IOException e) {
            throw new FlightBookingSystemException("Error logging cancellation: " + e.getMessage());
        }
    }
}