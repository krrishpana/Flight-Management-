package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

public class Booking {
    
    private Customer customer;
    private Flight flight;
    private LocalDate bookingDate;
    private double paidPrice;
    private double cancellationFee;
    private boolean hasInfant;
    private boolean isVegetarian;

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

    public String getDetails() {
        return String.format("Booking for Flight #%d - Price: £%.2f | Infant: %s | Vegetarian: %s",
                flight.getId(), paidPrice,
                hasInfant ? "Yes" : "No",
                isVegetarian ? "Yes" : "No");
    }
}

