package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

public class Booking {
    
    private Customer customer;
    private Flight flight;
    private LocalDate bookingDate;
    private double paidPrice;
    private double cancellationFee;

    public Booking(Customer customer, Flight flight, LocalDate bookingDate,
                   double paidPrice, double cancellationFee) {
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.paidPrice = paidPrice;
        this.cancellationFee = cancellationFee;
        
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

    public String getDetails() {
        return String.format("Booking for Flight #%d (%s to %s on %s) - Price Paid: £%.2f, Cancellation Fee: £%.2f",
                flight.getId(), flight.getOrigin(), flight.getDestination(),
                flight.getDepartureDate(), paidPrice, cancellationFee);
    }
}

