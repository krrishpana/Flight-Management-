package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

public class Flight {

    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private int capacity;
    private double basePrice;
    private double cancellationFee;

    private final Set<Customer> passengers;

    public Flight(int id, String flightNumber, String origin, String destination,
                  LocalDate departureDate, int capacity, double basePrice, double cancellationFee) {

        if (departureDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Departure date cannot be in the past: " + departureDate);
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive: " + capacity);
        }
        if (basePrice < 0) {
            throw new IllegalArgumentException("Base price cannot be negative: " + basePrice);
        }
        if (cancellationFee < 0) {
            throw new IllegalArgumentException("Cancellation fee cannot be negative: " + cancellationFee);
        }

        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;

        this.capacity = capacity;
        this.basePrice = basePrice;
        this.cancellationFee = cancellationFee;

        passengers = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public List<Customer> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return "Flight #" + id + " - " + flightNumber + " - " + origin + " to "
                + destination + " on " + departureDate.format(dtf);
    }

    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();

        sb.append("Flight #").append(id);
        sb.append(" Flight No: ").append(flightNumber);
        sb.append(" Origin: ").append(origin);
        sb.append(" Destination: ").append(destination);
        sb.append(" Departure Date: ").append(departureDate);
        sb.append("\nCapacity: ").append(capacity);
        sb.append(", Base Price: £").append(String.format("%.2f", basePrice));
        sb.append(", Cancellation Fee: £").append(String.format("%.2f", cancellationFee));
        sb.append(", Remaining Seats: ").append(getRemainingSeats());
        sb.append("\n\nPassengers:\n");

        if (passengers.isEmpty()) {
            sb.append("  No passengers booked.");
        } else {
            for (Customer c : passengers) {
                sb.append("* Id: ").append(c.getId())
                        .append(" - ").append(c.getName())
                        .append(" - ").append(c.getPhone()).append("\n");
            }
            sb.append(passengers.size()).append(" passenger(s)");
        }
        return sb.toString();
    }

    public void addPassenger(Customer passenger) throws FlightBookingSystemException {
        if (isFull()) {
            throw new FlightBookingSystemException(
                    "Flight #" + id + " (" + flightNumber + ") is fully booked. No more seats available."
            );
        }

        // Check by customer ID
        for (Customer existing : passengers) {
            if (existing.getId() == passenger.getId()) {
                throw new FlightBookingSystemException(
                        "Passenger #" + passenger.getId() + " is already booked on this flight"
                );
            }
        }

        passengers.add(passenger);
    }

    public void removePassenger(Customer passenger) throws FlightBookingSystemException {
        Customer toRemove = null;
        for (Customer existing : passengers) {
            if (existing.getId() == passenger.getId()) {
                toRemove = existing;
                break;
            }
        }

        if (toRemove == null) {
            throw new FlightBookingSystemException(
                    "Passenger #" + passenger.getId() + " is not booked on this flight"
            );
        }
        passengers.remove(toRemove);
    }

    /**
     * Gets the maximum capacity (number of seats) for this flight.
     * @return the flight capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the maximum capacity for this flight.
     * @param capacity the new capacity (must be positive)
     * @throws IllegalArgumentException if capacity is negative
     */
    public void setCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        this.capacity = capacity;
    }

    /**
     * Gets the base price for a ticket on this flight.
     * @return the base price
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Sets the base price for this flight.
     * @param basePrice the new base price (must be non-negative)
     * @throws IllegalArgumentException if basePrice is negative
     */
    public void setBasePrice(double basePrice) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("Base price cannot be negative");
        }
        this.basePrice = basePrice;
    }

    /**
     * Gets the cancellation fee for this flight.
     * @return the cancellation fee
     */
    public double getCancellationFee() {
        return cancellationFee;
    }

    /**
     * Sets the cancellation fee for this flight.
     * @param cancellationFee the new cancellation fee (must be non-negative)
     * @throws IllegalArgumentException if cancellationFee is negative
     */
    public void setCancellationFee(double cancellationFee) {
        if (cancellationFee < 0) {
            throw new IllegalArgumentException("Cancellation fee cannot be negative");
        }
        this.cancellationFee = cancellationFee;
    }

    public boolean isFull() {
        return passengers.size() >= capacity;
    }

    /**
     * Gets the number of remaining seats.
     * @return the number of available seats
     */
    public int getRemainingSeats() {
        return capacity - passengers.size();
    }

    /**
     * Calculates the current price based on departure date and seat availability.
     * Dynamic pricing rules:
     * 1. Time-based multipliers:
     *    - If departure < 7 days: ×1.2
     *    - If departure < 30 days: ×1.1
     * 2. Availability-based multipliers:
     *    - If remaining seats ≤ 10: ×0.8
     *    - If 10 < remaining seats ≤ 50: ×0.9
     * Rules stack (multiply both multipliers)
     *
     * @param currentDate the current system date
     * @return the calculated price
     */
    public double getCurrentPrice(LocalDate currentDate) {
        double priceMultiplier = 1.0;

        // 1. Time-based multiplier
        long daysUntilDeparture = ChronoUnit.DAYS.between(currentDate, departureDate);

        if (daysUntilDeparture < 7) {
            priceMultiplier *= 1.2;  // Less than 7 days: 20% increase
        } else if (daysUntilDeparture < 30) {
            priceMultiplier *= 1.1;  // Less than 30 days: 10% increase
        }

        int remainingSeats = getRemainingSeats();
        if (remainingSeats <= 10) {
            priceMultiplier *= 0.8;  // Few seats left: 20% discount
        } else if (remainingSeats <= 50) {
            priceMultiplier *= 0.9;  // Limited seats: 10% discount
        }

        double finalPrice = basePrice * priceMultiplier;

        return Math.round(finalPrice * 100.0) / 100.0;
    }


    /**
     * Returns a human-readable explanation of the current pricing.
     * @param currentDate the current system date
     * @return formatted string explaining the pricing
     */
    /**
     * Returns a concise pricing explanation.
     * @param currentDate the current system date
     * @return simplified pricing explanation
     */
    public String getPricingSummary(LocalDate currentDate) {
        StringBuilder summary = new StringBuilder();

        long daysUntilDeparture = ChronoUnit.DAYS.between(currentDate, departureDate);
        int remainingSeats = getRemainingSeats();
        double finalPrice = getCurrentPrice(currentDate);

        // Only show adjustments if they apply
        boolean hasTimeAdjustment = daysUntilDeparture < 30;
        boolean hasAvailabilityAdjustment = remainingSeats <= 50;

        summary.append(" PRICE SUMMARY\n");
        summary.append(String.format("Base Price: £%.2f\n", basePrice));

        if (hasTimeAdjustment || hasAvailabilityAdjustment) {
            summary.append("Adjustments:\n");

            if (daysUntilDeparture < 7) {
                summary.append("  +20% (last-minute booking)\n");
            } else if (daysUntilDeparture < 30) {
                summary.append("  +10% (short-term booking)\n");
            }

            if (remainingSeats <= 10) {
                summary.append("  -20% (low availability)\n");
            } else if (remainingSeats <= 50) {
                summary.append("  -10% (limited seats)\n");
            }
        } else {
            summary.append("(Standard pricing)\n");
        }

        summary.append(String.format("Final Price: £%.2f", finalPrice));

        return summary.toString();
    }
}