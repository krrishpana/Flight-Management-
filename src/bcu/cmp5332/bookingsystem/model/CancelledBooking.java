package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a cancelled booking that can potentially be undone.
 * Stores all original booking data to allow restoration within 24 hours.
 */
public class CancelledBooking {
    private final int customerId;
    private final int flightId;
    private final LocalDate bookingDate;
    private final double paidPrice;
    private final double cancellationFee;
    private final LocalDateTime cancellationTime;
    private final LocalDateTime undoDeadline;
    private final boolean hadInfant;
    private final boolean wasVegetarian;

    /**
     * Creates a record of a cancelled booking.
     * @param customerId ID of customer who cancelled
     * @param flightId ID of cancelled flight
     * @param bookingDate original booking date
     * @param paidPrice price originally paid
     * @param cancellationFee fee charged for cancellation
     * @param cancellationTime when cancellation occurred
     * @param undoDeadline deadline to undo cancellation
     * @param hadInfant whether booking included an infant
     * @param wasVegetarian whether vegetarian meal was requested
     */
    public CancelledBooking(int customerId, int flightId, LocalDate bookingDate,
                            double paidPrice, double cancellationFee,
                            LocalDateTime cancellationTime, LocalDateTime undoDeadline, boolean hadInfant, boolean wasVegetarian) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.bookingDate = bookingDate;
        this.paidPrice = paidPrice;
        this.cancellationFee = cancellationFee;
        this.cancellationTime = cancellationTime;
        this.undoDeadline = undoDeadline;
        this.hadInfant = hadInfant;
        this.wasVegetarian = wasVegetarian;
    }

    // Getters
    public int getCustomerId() { return customerId; }
    public int getFlightId() { return flightId; }
    public LocalDate getBookingDate() { return bookingDate; }
    public double getPaidPrice() { return paidPrice; }
    public double getCancellationFee() { return cancellationFee; }
    public LocalDateTime getCancellationTime() { return cancellationTime; }
    public LocalDateTime getUndoDeadline() { return undoDeadline; }
    public boolean hadInfant() { return hadInfant; }
    public boolean wasVegetarian() { return wasVegetarian; }

    /**
     * Checks if cancellation can still be undone.
     * @return true if current time is before undo deadline
     */
    public boolean canUndo() {
        return LocalDateTime.now().isBefore(undoDeadline);
    }

    /**
     * Calculates how much time remains to undo cancellation.
     * @return remaining time in hours and minutes, or "Expired"
     */
    public String getTimeRemaining() {
        if (!canUndo()) return "Expired";

        java.time.Duration remaining = java.time.Duration.between(LocalDateTime.now(), undoDeadline);
        long hours = remaining.toHours();
        long minutes = remaining.minusHours(hours).toMinutes();

        return String.format("%dh %dm", hours, minutes);
    }
}