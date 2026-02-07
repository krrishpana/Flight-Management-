package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.CancellationDataManager;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;

import java.io.IOException;

/**
 * Command to undo a cancellation within 24-hour window.
 * Restores booking using saved cancellation data.
 */
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
                                cancelled.getCancellationTime() + " (24-hour limit)"
                );
            }

            Customer customer = flightBookingSystem.getCustomerByID(customerId);
            Flight flight = flightBookingSystem.getFlightByID(flightId);

            if (flight.isFull()) {
                throw new FlightBookingSystemException(
                        "Cannot undo cancellation: Flight #" + flightId + " is now fully booked."
                );
            }

            // Prevent duplicate active booking
            for (Booking existing : customer.getBookings()) {
                if (existing.getFlight().getId() == flightId) {
                    throw new FlightBookingSystemException(
                            "Customer already has an active booking for this flight."
                    );
                }
            }

            // ---- SEAT RESTORATION ----
            String seat = cancelled.getSeatNumber();

            if (!flight.isSeatAvailable(seat)) {
                throw new FlightBookingSystemException(
                        "Cannot undo: Original seat " + seat + " is no longer available."
                );
            }

            flight.bookSeat(seat);

            // ---- RECREATE BOOKING ----
            Booking restoredBooking = new Booking(
                    customer,
                    flight,
                    cancelled.getBookingDate(),
                    cancelled.getPaidPrice(),
                    cancelled.getCancellationFee(),
                    cancelled.hadInfant(),
                    cancelled.wasVegetarian(),
                    seat
            );

            customer.addBooking(restoredBooking);
            flight.addPassenger(customer);

            cancellationDM.removeCancellation(customerId, flightId);

            System.out.println("\nCANCELLATION UNDONE");
            System.out.println("Booking successfully restored!");
            System.out.println("Customer: " + customer.getName());
            System.out.println("Flight: " + flight.getFlightNumber());
            System.out.println("Seat: " + seat);
            System.out.println("Price: Rs." + String.format("%.2f", cancelled.getPaidPrice()));

        } catch (IOException e) {
            throw new FlightBookingSystemException("Error processing undo: " + e.getMessage());
        }
    }
}
