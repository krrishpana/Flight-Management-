package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.gui.SeatSelectionDialog;
/**
 * Represents a flight in the booking system.
 * Stores flight details, manages passengers, and calculates dynamic pricing.
 */
public class Flight {

    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private int capacity;
    private double basePrice;
    private double cancellationFee;
    private boolean deleted = false;
    private boolean active = true;
    private final Set<Customer> passengers;
    private final Set<String> bookedSeats = new HashSet<>();

    private final Map<String, Customer> seatMap = new HashMap<>();

    /**
     * Creates a new flight with all details.
     * @param id unique flight identifier
     * @param flightNumber flight number (e.g., BA123)
     * @param origin departure airport/city
     * @param destination arrival airport/city
     * @param departureDate date of departure (cannot be in past)
     * @param capacity maximum number of passengers
     * @param basePrice standard ticket price
     * @param cancellationFee fee charged for cancellations
     * @throws IllegalArgumentException if any parameter is invalid
     */

    private static final char[] SEAT_LETTERS = {'A','B','C','D','E','F'};
    private int getMaxRows() {
        return (int) Math.ceil((double) capacity / SEAT_LETTERS.length);
    }
    public Flight(int id, String flightNumber, String origin, String destination,
                  LocalDate departureDate, int capacity, double basePrice, double cancellationFee) {

        if (departureDate.isBefore(LocalDate.now())) {
            System.err.println("Warning: Flight #" + id + " has past departure date: " + departureDate);
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
    /**
     * Gets the flight's deletion status.
     * @return true if flight is marked as deleted
     */
    public boolean isDeleted() {return deleted;}
    /**
     * Sets the flight's deletion status.
     * @param deleted true to mark as deleted, false to restore
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Checks if flight has departed relative to a given date.
     * @param currentDate the date to compare against
     * @return true if flight departure date is before or equal to currentDate
     */
    public boolean hasDeparted(LocalDate currentDate) {
        return departureDate.isBefore(currentDate) || departureDate.isEqual(currentDate);
    }
    /**
     * Soft-deletes the flight (marks as deleted without removing).
     */
    public void softDelete() {
        this.deleted = true;
    }
    /**
     * Restores a soft-deleted flight.
     */
    public void restore() {
        this.deleted = false;
    }

    /**
     * Checks if flight is active for booking.
     * @param currentDate the current system date
     * @return true if flight is not deleted, not departed, and not full
     */
    public boolean isAvailableForBooking(LocalDate currentDate) {
        return !deleted && !hasDeparted(currentDate) && !isFull();
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getFlightNumber() {return flightNumber;}
    public void setFlightNumber(String flightNumber) {this.flightNumber = flightNumber;}
    public String getOrigin() {return origin;}
    public void setOrigin(String origin) {this.origin = origin;}
    public String getDestination() {return destination;}
    public void setDestination(String destination) {this.destination = destination;}
    public LocalDate getDepartureDate() {return departureDate;}
    public void setDepartureDate(LocalDate departureDate) {this.departureDate = departureDate;}
    public List<Customer> getPassengers() {return new ArrayList<>(passengers);}

    /**
     * Frees a seat on the flight, making it available for other passengers.
     * @param seat the seat label (e.g., "12A") to be released
     */
    public void freeSeat(String seat) {
        // Assuming you have a Map or Array representing your seats
        // This logic should be the opposite of your bookSeat(seat) method
        if (seat != null) {bookedSeats.remove(seat.toUpperCase().trim());}
    }

    public int getSeatRows() {
        return getMaxRows();
    }

    /**
     * Provides a short summary of flight details.
     * @return formatted string with flight ID, number, route, and date
     */
    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return "Flight #" + id + " - " + flightNumber + " - " + origin + " to "
                + destination + " on " + departureDate.format(dtf);
    }

    /**
     * Provides detailed flight information including passenger list.
     * @return formatted string with all flight details
     */
    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();

        sb.append("Flight #").append(id);
        sb.append(" Flight No: ").append(flightNumber);
        sb.append(" Origin: ").append(origin);
        sb.append(" Destination: ").append(destination);
        sb.append(" Departure Date: ").append(departureDate);
        sb.append("\nCapacity: ").append(capacity);
        sb.append(", Base Price: ").append(String.format("%.2f", basePrice));
        sb.append(", Cancellation Fee: ").append(String.format("%.2f", cancellationFee));
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

    /**
     * Adds a passenger to this flight.
     * @param passenger the customer to add
     * @throws FlightBookingSystemException if flight is full or passenger already booked
     */
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

    /**
     * Removes a passenger from this flight.
     * @param passenger the customer to remove
     * @throws FlightBookingSystemException if passenger not found on flight
     */
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

        boolean hasTimeAdjustment = daysUntilDeparture < 30;
        boolean hasAvailabilityAdjustment = remainingSeats <= 50;

        summary.append(" PRICE SUMMARY\n");
        summary.append(String.format("Base Price: %.2f\n", basePrice));

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

        summary.append(String.format("Final Price: Rs%.2f", finalPrice));

        return summary.toString();
    }

    public boolean isValidSeat(String seat) {
        if (seat == null || seat.length() < 2) return false;

        try {
            int row = Integer.parseInt(seat.substring(0, seat.length() - 1));
            char letter = seat.charAt(seat.length() - 1);

            // Use dynamic calculation instead of hardcoded ROWS
            if (row < 1 || row > getMaxRows()) return false;

            for (char c : SEAT_LETTERS) {
                if (c == letter) return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    public boolean isSeatAvailable(String seat) {
        return isValidSeat(seat) && !bookedSeats.contains(seat);
    }
    public void bookSeat(String seat) throws FlightBookingSystemException {
        if (!isValidSeat(seat)) {
            throw new FlightBookingSystemException("Invalid seat number.");
        }
        if (!isSeatAvailable(seat)) {
            throw new FlightBookingSystemException("Seat already booked.");
        }
        bookedSeats.add(seat);
    }

    public void displaySeatMap() {
        System.out.println("\n SEAT MAP (X = Booked)");

        int maxRows = getMaxRows();

        for (int row = 1; row <= maxRows; row++) {
            System.out.printf("%2d ", row);

            for (char seat : SEAT_LETTERS) {
                String seatId = row + String.valueOf(seat);
                if (bookedSeats.contains(seatId)) {
                    System.out.print(" X ");
                } else {
                    System.out.print(" " + seat + " ");
                }
            }
            System.out.println();
        }
    }
}