package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public boolean canUndo() {
        return LocalDateTime.now().isBefore(undoDeadline);
    }

    public String getTimeRemaining() {
        if (!canUndo()) return "Expired";

        java.time.Duration remaining = java.time.Duration.between(LocalDateTime.now(), undoDeadline);
        long hours = remaining.toHours();
        long minutes = remaining.minusHours(hours).toMinutes();

        return String.format("%dh %dm", hours, minutes);
    }
}