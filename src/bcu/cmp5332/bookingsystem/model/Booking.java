package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

/**
 * Represents a booking made by a customer for a specific flight.
 * Stores booking details including passenger preferences and cancellation fee.
 */
public class Booking {
    
    private Customer customer;
    private Flight flight;
    private LocalDate bookingDate;
    private double paidPrice;
    private double cancellationFee;
    private boolean hasInfant;
    private boolean isVegetarian;

    /**
     * Creates a new booking with all booking details.
     * @param customer the customer making the booking
     * @param flight the flight being booked
     * @param bookingDate the date when booking was made
     * @param paidPrice the price paid for the booking
     * @param cancellationFee fee charged if booking is cancelled
     * @param hasInfant whether booking includes an infant
     * @param isVegetarian whether vegetarian meal is requested
     */
    public Booking(Customer customer, Flight flight, LocalDate bookingDate,
                   double paidPrice, double cancellationFee, boolean hasInfant, boolean isVegetarian) {
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.paidPrice = paidPrice;
        this.cancellationFee = cancellationFee;
        this.hasInfant = hasInfant;
        this.isVegetarian = isVegetarian;
        
    }
    public Customer getCustomer() {
        return customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public double getPaidPrice() {
        return paidPrice;
    }

    public double getCancellationFee() {
        return cancellationFee;
    }

    public boolean hasInfant() {
        return hasInfant;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    // -------- Setters --------

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setPaidPrice(double paidPrice) {
        this.paidPrice = paidPrice;
    }

    public void setCancellationFee(double cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public void setHasInfant(boolean hasInfant) {
        this.hasInfant = hasInfant;
    }

    public void setIsVegetarian(boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
    }

    /**
     * Provides a formatted summary of booking details.
     * @return string with flight ID, price, and passenger preferences
     */
    public String getDetails() {
        return String.format("Booking for Flight #%d - Price: £%.2f | Infant: %s | Vegetarian: %s",
                flight.getId(), paidPrice,
                hasInfant ? "Yes" : "No",
                isVegetarian ? "Yes" : "No");
    }
}

