package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.CancellationDataManager;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;

import java.io.IOException;
import java.time.LocalDateTime;

public class UndoCancelBooking implements Command {

    private final int customerId;
    private final int flightId;
    private final CancellationDataManager cancellationDM;

    public UndoCancelBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.cancellationDM = new CancellationDataManager();
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        try {
            CancelledBooking cancelled = cancellationDM.findCancellation(customerId, flightId);

            if (cancelled == null) {
                throw new FlightBookingSystemException(
                        "No recent cancellation found for customer #" + customerId +
                                " on flight #" + flightId
                );
            }

            if (!cancelled.canUndo()) {
                throw new FlightBookingSystemException(
                        "Undo period has expired. Cancelled at: " +
                                cancelled.getCancellationTime() +
                                " (24-hour limit)"
                );
            }

            Customer customer = flightBookingSystem.getCustomerByID(customerId);
            Flight flight = flightBookingSystem.getFlightByID(flightId);

            if (flight.isFull()) {
                throw new FlightBookingSystemException(
                        "Cannot undo cancellation: Flight #" + flightId +
                                " is now fully booked."
                );
            }

            for (Booking existing : customer.getBookings()) {
                if (existing.getFlight().getId() == flightId) {
                    throw new FlightBookingSystemException(
                            "Customer already has an active booking for this flight."
                    );
                }
            }

            // Create new booking with original details
            Booking restoredBooking = new Booking(
                    customer, flight, cancelled.getBookingDate(),
                    cancelled.getPaidPrice(), cancelled.getCancellationFee()
            );

            // Add booking to customer
            customer.addBooking(restoredBooking);

            // Add customer to flight
            flight.addPassenger(customer);

            // Remove from cancellations file
            cancellationDM.removeCancellation(customerId, flightId);

            // Display success
            System.out.println("\n" + " ".repeat(20));
            System.out.println("CANCELLATION UNDONE");
            System.out.println("=" .repeat(40));
            System.out.println(" Booking successfully restored!");
            System.out.println("\n Restored Details:");
            System.out.println("  - Customer: " + customer.getName());
            System.out.println("  - Flight: " + flight.getFlightNumber());
            System.out.println("  - Original Price: £" + String.format("%.2f", cancelled.getPaidPrice()));
            System.out.println("  - Cancellation Fee: £" + String.format("%.2f", cancelled.getCancellationFee()));
            System.out.println("  - Time Remaining: " + cancelled.getTimeRemaining() + " (when cancelled)");
            System.out.println("\n" + " ".repeat(20));

        } catch (IOException e) {
            throw new FlightBookingSystemException("Error processing undo: " + e.getMessage());
        }
    }
}