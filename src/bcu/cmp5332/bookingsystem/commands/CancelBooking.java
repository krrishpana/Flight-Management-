package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.data.CancellationDataManager;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Command to cancel a booking and log it for potential undo.
 * Charges cancellation fee and stores cancellation record.
 */
public class CancelBooking implements Command {

    private final int customerId;
    private final int flightId;
    private final CancellationDataManager cancellationDM;


    /**
     * Creates a cancel booking command for specific customer and flight.
     * @param customerId ID of customer cancelling booking
     * @param flightId ID of flight to cancel booking for
     */
    public CancelBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.cancellationDM = new CancellationDataManager();
    }

    /**
     * Executes booking cancellation.
     * Logs cancellation, removes booking, and informs user about undo option.
     * @param flightBookingSystem the system to operate on
     * @throws FlightBookingSystemException if cancellation fails
     */
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

            // Log cancellation BEFORE removing (so we have the data)
            cancellationDM.logCancellation(bookingToCancel, LocalDateTime.now());

            // Remove booking from customer
            customer.cancelBookingForFlight(flight);

            // Remove customer from flight
            flight.removePassenger(customer);

            System.out.println("\n" + "=".repeat(50));
            System.out.println("BOOKING CANCELLED SUCCESSFULLY!");
            System.out.println("=".repeat(50));
            System.out.println("Customer: " + customer.getName());
            System.out.println("Flight: " + flight.getFlightNumber());
            System.out.println("Cancellation Fee Charged: £" +
                    String.format("%.2f", bookingToCancel.getCancellationFee()));
            System.out.println("\nYou have 24 hours to undo this cancellation.");
            System.out.println("Command to undo: undocancel " + customerId + " " + flightId);
            System.out.println("=".repeat(50));

        } catch (IOException e) {
            throw new FlightBookingSystemException(
                    "Error logging cancellation: " + e.getMessage()
            );
        }
    }
}